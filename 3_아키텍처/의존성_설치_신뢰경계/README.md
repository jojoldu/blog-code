# 의존성 설치를 신뢰 경계로 보기

요즘 npm 공급망 공격은 "이상한 이름의 패키지를 실수로 설치했다" 정도의 문제가 아니다.

정상 프로젝트의 정상 패키지에 악성 버전이 잠깐 배포된다. 그리고 그 짧은 시간에 설치한 개발자 로컬이나 CI 환경에서 credential이 털린다. 공격자는 우리 서비스 코드를 직접 뚫지 않는다. 우리가 믿고 설치하는 패키지 생태계를 오염시킨다.

이 차이가 크다.

서비스 코드는 리뷰라도 한다. 배포 전 테스트도 돌린다. 그런데 의존성은 상대적으로 쉽게 통과한다. lockfile 변경을 꼼꼼히 보는 팀은 생각보다 많지 않고, CI는 매번 너무 자연스럽게 install을 수행한다.

공격자 입장에서는 이만큼 좋은 진입점도 없다.

최근 TanStack npm 공급망 사고가 딱 그런 사례였다.

TanStack 공식 포스트모템에 따르면 2026년 5월 11일 19:20부터 19:26 UTC 사이, 42개의 `@tanstack/*` npm 패키지에 총 84개의 악성 버전이 배포됐다. 영향을 받은 버전을 설치하면 악성 `optionalDependencies`가 GitHub fork의 commit을 가져오고, lifecycle script를 통해 AWS, GCP, Kubernetes, Vault, GitHub, npm, SSH 관련 credential을 수집하는 구조였다.

공격이 몇 시간씩 이어진 것도 아니었다. 몇 분 사이에 배포됐고, 20분 안팎으로 외부 연구자에게 탐지됐다. 그런데도 그 짧은 시간에 해당 버전을 설치한 개발자와 CI는 영향을 받을 수 있었다.

이 사건을 보면서 패키지 매니저를 단순한 개발 도구로만 보면 안되겠다는 생각이 들었다.

패키지 매니저는 소스코드와 외부 세계 사이의 경계에 있다.  
우리는 `pnpm install` 한 번으로 수백 개, 많게는 수천 개의 외부 코드를 내부 환경으로 들여온다.  
그리고 그 과정에서 일부 패키지는 install script를 실행한다.

```text
Registry -> Package Manager -> node_modules -> Build / Test / Deploy
```

이 흐름을 보면 `install`은 단순한 다운로드가 아니다.

외부에서 온 코드를 내부 실행 환경으로 편입시키는 단계다.  
아키텍처 관점에서는 명확한 신뢰 경계다.

서비스 API를 설계할 때도 외부 요청을 어디까지 들여보낼지 고민한다. Public API를 애플리케이션 서버까지 보내지 않고 CDN에서 끝낼 수 있는지 본다. 인증되지 않은 요청에는 Rate Limit을 둔다. 내부 API와 외부 API를 같은 정책으로 다루지 않는다.

의존성 설치도 비슷하게 봐야 한다.

모든 패키지를 같은 수준으로 믿을 수 없다.  
방금 배포된 버전과 오래 검증된 버전은 다르다.  
내가 직접 선언한 dependency와 transitive dependency도 다르다.  
설치만 되는 패키지와 install 중 코드를 실행하는 패키지도 다르다.

이 차이를 구분하지 않고 모두 같은 경로로 통과시키면, 패키지 매니저는 외부 코드를 내부로 들여오는 넓은 문이 된다.

pnpm 11에서 의미 있는 변화는 이 문을 기본적으로 조금 좁혔다는 점이다.

가장 먼저 볼 것은 `minimumReleaseAge`다.

pnpm 11에서는 이 값의 기본값이 1440분, 즉 24시간이다. 새로 배포된 패키지 버전은 기본적으로 하루가 지나기 전에는 resolve되지 않는다. 방금 올라온 최신 버전을 바로 설치하지 않는 것이다.

처음 들으면 조금 답답해 보인다. 최신 버전이 나왔는데 왜 하루를 기다려야 하나 싶다. 그런데 공급망 공격의 동작 방식을 생각하면 꽤 합리적인 선택이다.

악성 버전은 대개 배포 직후 몇 시간 안에 발견된다. 그렇다면 굳이 그 위험한 시간대에 들어갈 이유가 없다.

이건 거창한 보안 시스템이 아니다. 그냥 하루 늦게 받는 것이다. 그런데 이 단순한 지연만으로도 공격자가 노리는 감염 창을 상당 부분 피할 수 있다.

나는 이 설정이 아키텍처적으로도 꽤 좋은 결정이라고 본다.

최신성보다 안정성을 우선하는 경계를 둔 것이기 때문이다.  
패키지 매니저가 "항상 최신을 가져오는 도구"가 아니라, "내부로 들여보낼 수 있는 외부 코드를 선별하는 계층"이 되는 것이다.

두 번째는 `blockExoticSubdeps`다.

pnpm 11에서는 이 값도 기본적으로 `true`다. transitive dependency가 git 저장소, GitHub commit, 직접 tarball URL 같은 registry 밖의 source를 마음대로 가져오지 못하게 막는다. root `package.json`에 직접 선언한 dependency는 필요한 경우 이런 source를 쓸 수 있지만, 하위 의존성이 몰래 외부 source를 끌고 오는 것은 제한된다.

TanStack 사고에서도 악성 `optionalDependencies`가 GitHub commit을 가져오는 구조가 핵심이었다. 우리가 직접 고른 의존성도 아닌데, 그 아래 하위 의존성이 registry 밖의 코드를 가져와 설치 과정에 끼어든다. 이 순간부터 추적과 검증은 급격히 어려워진다.

아키텍처에서 중요한 원칙 중 하나는 내가 제어할 수 없는 것에 과하게 의존하지 않는 것이다.

하위 의존성이 임의의 GitHub commit이나 tarball URL을 가져오는 것은 내가 제어할 수 없는 외부 속성에 의존하는 것이다. root dependency에 직접 선언했다면 적어도 팀이 인지하고 리뷰할 수 있다. 하지만 transitive dependency 안에서 발생하면 이야기가 달라진다.

그래서 `blockExoticSubdeps`는 단순한 보안 옵션이 아니다.  
외부 source에 대한 의존을 어느 계층까지 허용할지 정하는 정책이다.

세 번째는 build script와 lifecycle script에 대한 통제다.

공급망 공격에서 `postinstall` 같은 lifecycle script는 자주 악용된다. 패키지를 설치하는 순간 코드가 실행되기 때문이다. pnpm 11에서는 `strictDepBuilds`가 기본적으로 `true`이고, 승인되지 않은 dependency build script가 있으면 설치가 실패한다.

이 동작은 처음에는 귀찮다.

기존에는 install이 그냥 지나가던 패키지가 실패할 수 있다. 개발자는 "왜 갑자기 설치가 안되지?"라고 느낄 수 있다.

하지만 install 단계에서 코드 실행을 허용하는 일은 생각보다 큰 권한이다. 지금까지는 많은 프로젝트가 이 권한을 너무 쉽게 열어뒀다.

```text
Install only package  -> node_modules에 파일 배치
Install script package -> node_modules에 파일 배치 + 코드 실행
```

이 둘은 같은 의존성이 아니다.

후자는 설치 시점에 개발자 로컬과 CI 환경에서 코드를 실행할 수 있다. 즉, AWS credential, GitHub token, npm token, SSH key에 접근할 수 있는 환경에서 실행될 수 있다.

그렇다면 이 권한은 명시적으로 승인되어야 한다.

pnpm 11의 방향은 "모든 dependency를 믿는다"가 아니다. "설치 중 코드를 실행할 수 있는 dependency만 따로 승인한다"에 가깝다. 그래서 `allowBuilds`가 중요해졌다.

반대로 `dangerouslyAllowAllBuilds`를 `true`로 켜는 것은 피해야 한다. 이 옵션은 direct dependency뿐만 아니라 transitive dependency의 install script까지 자동 실행하게 만든다. 지금 dependency graph가 안전해 보여도, 나중에 하위 패키지 하나가 hijack되면 install 단계에서 악성 코드가 실행될 수 있다.

이름이 과하게 붙은 옵션이 아니다. 정말 위험해서 저 이름이 붙었다고 보는 편이 낫다.

`verifyDepsBeforeRun`도 같은 맥락에서 볼 수 있다.

pnpm 11에서는 `pnpm run`이나 `pnpm exec` 전에 `node_modules` 상태를 확인하고, 맞지 않으면 install을 수행하는 `verifyDepsBeforeRun`의 기본값이 `install`이다. lockfile과 `node_modules`가 어긋난 상태로 스크립트를 실행하는 일을 줄이고, install 단계에서 pnpm의 보안 정책이 다시 적용되도록 만든다.

로컬 개발에서는 이 기본값이 편하다. 반대로 CI에서는 `error`가 더 낫다고 본다. CI에서 예상하지 못한 install이 암묵적으로 실행되는 것보다, 상태가 맞지 않으면 바로 실패하는 편이 원인 파악도 쉽고 정책도 명확하다.

CI는 편의성보다 재현성이 우선이다.

개발자 로컬에서는 자동 복구가 도움이 될 수 있지만, CI에서는 자동 복구가 오히려 문제를 숨길 수 있다. 특히 공급망 보안 관점에서는 "알아서 install해서 맞춰준다"보다 "상태가 다르면 실패한다"가 더 안전하다.

추가로 `trustPolicy: no-downgrade`도 같이 검토할 만하다.

이 설정은 이전 릴리스보다 publish trust evidence가 낮아진 버전을 설치하지 않게 막는다. 예를 들어 예전에는 trusted publisher로 배포되던 패키지가 갑자기 더 낮은 신뢰 수준으로 배포된다면 설치를 실패시킬 수 있다.

다만 이것 하나로 충분하다고 보기는 어렵다. TanStack 사고처럼 trusted publishing 자체가 공격 체인에 포함된 경우도 있기 때문이다. `trustPolicy`는 보조 방어선이고, 핵심은 `minimumReleaseAge`, `blockExoticSubdeps`, `allowBuilds`를 같이 가져가는 것이다.

정리하면 pnpm 11에서 봐야 할 것은 기능 목록이 아니다.

다음과 같은 경계가 생겼다는 점이다.

```text
새로 배포된 버전        -> 24시간이 지나야 통과
하위 의존성의 외부 source -> 기본 차단
install 중 코드 실행     -> 명시 승인
CI의 의존성 불일치       -> 자동 복구보다 실패
```

이 정도면 패키지 매니저 설정이라기보다 의존성 유입 경로에 대한 아키텍처 정책에 가깝다.

프로젝트에 적용할 때는 먼저 Node.js 22 이상을 준비해야 한다. pnpm 11은 Node.js 22 이상을 요구한다. 로컬 개발 환경, CI 이미지, 배포 빌드 환경이 모두 Node.js 22 이상인지 먼저 확인해야 한다.

그다음 `package.json`에서 사용하는 패키지 매니저 버전을 명시한다. 팀 프로젝트라면 이 설정이 특히 중요하다. 사람마다 다른 pnpm 버전으로 install하면서 lockfile이 흔들리는 일을 줄일 수 있다.

```json
{
  "packageManager": "pnpm@11.0.0",
  "engines": {
    "node": ">=22"
  }
}
```

pnpm 11의 `devEngines.packageManager`를 사용한다면 다음처럼 둘 수도 있다.

```json
{
  "devEngines": {
    "packageManager": {
      "name": "pnpm",
      "version": "^11.0.0",
      "onFail": "download"
    },
    "runtime": {
      "name": "node",
      "version": ">=22",
      "onFail": "error"
    }
  }
}
```

기존 프로젝트라면 `.npmrc`도 정리해야 한다. pnpm 11에서는 `.npmrc`를 auth와 registry 설정 중심으로만 사용한다. `hoistPattern`, `nodeLinker`, `shamefullyHoist` 같은 pnpm 전용 설정은 `pnpm-workspace.yaml` 또는 전역 `config.yaml`로 옮기는 것이 맞다.

인증 정보와 registry 주소는 `.npmrc`에 둔다. pnpm 동작 정책은 `pnpm-workspace.yaml`에 둔다. 이렇게 역할을 나누면 된다.

내가 새 프로젝트나 보안을 중요하게 보는 프론트엔드 프로젝트에 기본으로 넣고 싶은 설정은 다음과 같다.

```yaml
# pnpm-workspace.yaml
minimumReleaseAge: 1440
minimumReleaseAgeStrict: true
blockExoticSubdeps: true
strictDepBuilds: true
dangerouslyAllowAllBuilds: false
trustPolicy: no-downgrade
verifyDepsBeforeRun: install

allowBuilds:
  esbuild: true
  sharp: true
  '@swc/core': true
  core-js: false
```

이 설정에서 가장 중요한 것은 세 가지다.

첫째, `minimumReleaseAge: 1440`으로 새로 배포된 버전을 바로 받지 않는다. 보안 정책으로 강제하려면 `minimumReleaseAgeStrict: true`까지 명시하는 편이 좋다.

둘째, `blockExoticSubdeps: true`로 하위 의존성이 registry 밖 source를 가져오지 못하게 한다.

셋째, `strictDepBuilds: true`와 `allowBuilds`로 install 중 실행되는 build script를 승인제로 운영한다.

운영하면서 제일 조심할 것은 `allowBuilds`다. 이 목록은 "설치 중 코드를 실행해도 되는 패키지 목록"이다. 단순히 install이 실패한다고 전부 허용하면 설정의 의미가 사라진다.

예를 들어 `esbuild`, `sharp`, `@swc/core`처럼 설치 과정에서 네이티브 바이너리나 빌드 처리가 필요한 패키지는 승인할 수 있다. 반대로 install script가 필요 없거나 실행을 원하지 않는 패키지는 `false`로 둔다. 중요한 것은 한 번에 열어주는 것이 아니라, 필요한 패키지만 보고 승인하는 방식이다.

CI에서는 `verifyDepsBeforeRun`을 더 엄격하게 가져가는 것도 좋다.

```yaml
# pnpm-workspace.yaml
verifyDepsBeforeRun: error
```

실제 적용 순서는 복잡하지 않다.

먼저 로컬과 CI의 Node.js를 22 이상으로 올린다.

그다음 pnpm을 11로 올리고, `package.json`에 프로젝트가 사용할 pnpm 버전을 명시한다.

```bash
corepack enable
corepack prepare pnpm@11.0.0 --activate
pnpm --version
```

이후 `.npmrc`에 섞여 있던 pnpm 전용 설정을 `pnpm-workspace.yaml`로 옮긴다. `.npmrc`에는 registry와 auth 관련 설정만 남긴다.

그다음 `pnpm-workspace.yaml`에 보안 설정을 추가한다. 처음부터 완벽하게 맞추려고 하기보다, install을 한 번 돌리면서 실제로 어떤 패키지가 build script 승인을 요구하는지 확인하는 편이 낫다.

```bash
pnpm install
pnpm approve-builds
```

여기서 모든 build script를 한 번에 허용하지 않는 것이 중요하다. 필요한 패키지만 `allowBuilds`에 추가한다.

CI에서는 `pnpm install` 대신 `pnpm ci` 또는 frozen lockfile 기반 설치를 사용한다. lockfile이 바뀌면 CI가 실패하도록 두는 것이 좋다. 공급망 공격 대응에서 lockfile은 매우 중요한 방어선이다. lockfile 변경은 코드 변경처럼 리뷰 대상이어야 한다.

```bash
pnpm ci
```

아직 `pnpm ci`로 전환하기 어렵다면 최소한 다음처럼 lockfile 변경을 막아야 한다.

```bash
pnpm install --frozen-lockfile
```

마지막으로 SBOM과 audit도 같이 붙인다. pnpm 11에는 `pnpm sbom` 명령이 추가됐다. 우리 프로젝트가 어떤 오픈소스 의존성을 포함하는지 CycloneDX나 SPDX 형식으로 뽑을 수 있다. 보안 사고가 났을 때 "우리가 영향받는가?"를 빠르게 판단하려면 SBOM이 필요하다. `pnpm audit`도 CI나 정기 점검에 포함하는 것이 좋다.

```bash
pnpm sbom --format cyclonedx-json > sbom.cyclonedx.json
pnpm audit
```

물론 pnpm 11을 쓴다고 모든 공급망 공격이 사라지는 것은 아니다. 이미 lockfile에 악성 버전이 들어간 경우, runtime에서 실행되는 악성 코드, CI secret의 과도한 권한, GitHub Actions cache poisoning 같은 문제는 별도로 다뤄야 한다.

그래서 lockfile review, frozen lockfile 기반 CI, CI secret 최소 권한, SBOM 생성, audit, credential rotation 정책은 여전히 필요하다.

그래도 시작점은 분명하다.

패키지 매니저는 빠른 설치 도구만이 아니다. 외부 코드를 내부 실행 환경으로 들여보내는 경계다.

그 경계를 어디까지 열어둘 것인지,
어떤 source를 허용할 것인지,
설치 중 코드 실행을 누구에게 허용할 것인지,
CI에서 불일치를 복구할 것인지 실패시킬 것인지는 모두 아키텍처 결정이다.

요즘 같은 시기라면 패키지 매니저를 고를 때 속도만 볼 수 없다.

의존성을 얼마나 빠르게 설치하느냐보다, 어떤 의존성을 언제 설치하고, 설치 중 어떤 코드를 실행하게 둘 것인지가 더 중요해졌다.

그런 의미에서 pnpm 11은 단순한 메이저 업그레이드가 아니다. 프론트엔드 프로젝트의 공급망 보안 기준선을 한 단계 올리는 릴리즈에 가깝다.

Node.js 22 이상이 필요하다는 조건은 있지만, 새 프로젝트라면 pnpm 11을 기본 선택지로 두는 것이 좋다. 기존 프로젝트도 업그레이드 계획을 세워볼 만하다.

참고

- TanStack 포스트모템: https://tanstack.com/blog/npm-supply-chain-compromise-postmortem
- pnpm 11.0 릴리즈 노트: https://pnpm.io/blog/releases/11.0
- pnpm 공급망 공격 완화 문서: https://pnpm.io/supply-chain-security
- pnpm 설정 문서: https://pnpm.io/settings
