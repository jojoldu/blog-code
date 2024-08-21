# IntelliJ & WebStorm VM Options

## 설정 방법

`Menu Bar` -> `Help` -> `Edit Custom VM Options…` 으로 접근할 수 있다.  
혹은 `command + A` 으로 Action 검색으로 `Edit Custom VM Options` 을 검색해서 접근해서 수정해도 된다.  



## VM Options

```bash
-ea
-server
-Xms1024m
-Xmx4096m
-XX:NewRatio=1
-Xss256k
-XX:+UnlockExperimentalVMOptions
-XX:-UseSerialGC
-XX:-UseParallelGC
-XX:-UseG1GC
-XX:+UseZGC
-XX:+IgnoreUnrecognizedVMOptions
-XX:+HeapDumpOnOutOfMemoryError
-XX:-OmitStackTraceInFastThrow
-XX:ReservedCodeCacheSize=512m
-XX:SoftRefLRUPolicyMSPerMB=50
-XX:+UseStringCache
-XX:+UseStringDeduplication
-XX:+AggressiveOpts
-XX:+AlwaysPreTouch
-XX:+OptimizeStringConcat
-XX:+UseFastAccessorMethods
-Dide.no.platform.update=true
-Djava.net.preferIPv4Stack=true
-Djdk.attach.allowAttachSelf=true
-Djdk.module.illegalAccess.silent=true
-Dkotlinx.coroutines.debug=off
-Dsun.io.useCanonCaches=false
-Dsun.java2d.d3d=false
-Dsun.java2d.metal=true
-Dsun.java2d.opengl=false
-Dawt.java2d.opengl=false
-Dsun.tools.attach.tmp.only=true
-Dsun.awt.mac.a11y.enabled=false
```

`-ea`
- **의미**: Assertion을 활성화하는 옵션이다.
- **특징 및 장점**: IntelliJ 등 JVM 환경에서 코드를 작성할 떄 별도의 추가 옵션 없이 `assert` 문을 사용할 수 있다.
- **단점 및 주의사항**: WebStorm 등 JVM 개발 환경이 아닌 경우 굳이 넣을 필요가 없다.
- 참고: [Where to add compiler options like -ea in IntelliJ IDEA?](https://stackoverflow.com/questions/18168257/where-to-add-compiler-options-like-ea-in-intellij-idea)

`-server`
- **의미**: 가장 자주 사용되는 코드 조각(핫스팟)을 최적화하는 데 더 많은 시간을 할애하도록 JVM에 지시한다.
- **특징 및 장점**: IDE 시작시 오버헤드가 높아질 수 있지만, 성능이 더 좋아진다. 
- 참고: [notes-on-the-java-server-flag](https://web.archive.org/web/20130606001921/http://victorpillac.com/2011/09/11/notes-on-the-java-server-flag/)

`-Xms1024m` /`-Xmx4096m`
- **의미**: JVM의 힙 메모리 초기 크기와 최대 크기를 각각 1024MB와 4096MB로 설정한다.
- **특징 및 장점**: 메모리 부족으로 인한 성능 저하를 방지할 수 있다.
- **단점 및 주의사항**: 너무 크게 설정하면 다른 애플리케이션의 메모리 사용에 영향을 줄 수 있다.

`-XX:NewRatio=1`
- **의미**: 힙에서 Young Generation과 Old Generation 의 크기 간의 비율을 지정한다. 
- **특징 및 장점**
  - 대부분의 경우 2와 4 사이의 비율이 권장한다. 
  - 이렇게 하면 Young Generation의 크기가 그에 따라 Old Generation의 1/2에서 1/4로 설정되므로 **한 번에 하나의 프로젝트와 몇 개의 파일만 작업하는 경우에 좋다**. 
  - 만약 끊임없이 새 파일을 열고 여러 프로젝트 간에 전환하는 경우 `-XX:NewRatio=1` 로 설정하면 성능 개선에 도움이 된다.
- **단점 및 주의사항**: 애플리케이션의 특성에 맞지 않는 비율을 설정하면 메모리 활용도가 떨어질 수 있다.


`-Xss256k`
- **의미**: 각 스레드의 스택 크기를 256KB로 설정한다.
- **특징 및 장점**: 메모리 사용을 절약할 수 있다.
- **단점 및 주의사항**: 스택 크기가 너무 작으면`StackOverflowError`가 발생할 수 있다.

## 고급 JVM 옵션

`-XX:+UnlockExperimentalVMOptions`
- **의미**: 실험적인 JVM 옵션을 사용할 수 있게 해준다.
- **특징 및 장점**: 최신 기능을 테스트할 수 있다.
- **단점 및 주의사항**: 실험적인 기능은 안정성이 보장되지 않으므로 주의가 필요하다.

`-XX:-UseSerialGC`,`-XX:-UseParallelGC`,`-XX:-UseG1GC`,`-XX:+UseZGC`
- **의미**: Serial, Parallel, G1, ZGC 가비지 컬렉터를 각각 비활성화 및 활성화한다.
- **특징 및 장점**: ZGC는 큰 힙에서도 저지연 가비지 컬렉션을 제공한다.
- **단점 및 주의사항**: ZGC는 최신 기술이므로, 모든 환경에서 최적의 성능을 발휘하지 않을 수 있으니, 상황에 따라 적절한 GC를 선택한다.

`-XX:+IgnoreUnrecognizedVMOptions`
- **의미**: JVM이 인식하지 못하는 옵션을 무시하도록 설정한다.
- **특징 및 장점**: 다양한 환경에서 실행될 때 호환성 문제를 방지할 수 있다.
- **단점 및 주의사항**: 잘못된 옵션이 무시되어 예상치 못한 동작이 발생할 수 있다.
- 사례: [How to fix "Unrecognized VM option 'UseConcMarkSweepGC'" error?](https://stackoverflow.com/questions/65546614/how-to-fix-unrecognized-vm-option-useconcmarksweepgc-error)

`-XX:+HeapDumpOnOutOfMemoryError`
- **의미**: OutOfMemoryError 발생 시 힙 덤프를 생성한다.
- **특징 및 장점**: 메모리 문제를 디버깅하는 데 유용하다.
- **단점 및 주의사항**: 덤프 파일이 매우 클 수 있으므로 디스크 공간에 주의해야 한다.

`-XX:-OmitStackTraceInFastThrow`
- **의미**: FastThrow 예외가 발생해도 스택 트레이스를 포함하도록 설정한다.
- **특징 및 장점**: 디버깅을 쉽게 할 수 있다.
- **단점 및 주의사항**: 성능에 영향을 미칠 수 있다.

`-XX:ReservedCodeCacheSize=512m`
- **의미**: 코드 캐시의 크기를 512MB로 설정한다.
- **특징 및 장점**: JIT 컴파일된 메서드를 더 많이 캐싱하여 성능을 향상시킬 수 있다.
- **단점 및 주의사항**: 너무 크게 설정하면 다른 메모리 영역에 영향을 줄 수 있다.

`-XX:SoftRefLRUPolicyMSPerMB=50`
- **의미**: 소프트 참조의 유지 시간을 설정한다.
- **특징 및 장점**: 메모리가 부족할 때 소프트 참조가 더 빨리 해제될 수 있다.
- **단점 및 주의사항**: 소프트 참조 객체가 빨리 해제되어야 할 경우 메모리에서 빠르게 제거되지 않을 수 있다.

`-XX:+UseStringCache`,`-XX:+UseStringDeduplication`
- **의미**: String 객체의 캐싱과 중복 제거를 활성화한다.
- **특징 및 장점**: 메모리 사용량을 줄여 애플리케이션 성능을 향상시킬 수 있다.
- **단점 및 주의사항**: 약간의 CPU 오버헤드를 수반할 수 있다.

`-XX:+AlwaysPreTouch`
- **의미**: JVM이 시작할 때 힙 메모리를 미리 할당한다.
- **특징 및 장점**: 힙 메모리 할당의 일관성을 높여 런타임 성능을 예측 가능하게 만든다.
- **단점 및 주의사항**: 초기화 시간이 길어질 수 있다.

`-XX:+OptimizeStringConcat`
- **의미**: String 객체의 연결을 최적화한다.
- **특징 및 장점**: 문자열 연결 성능이 향상된다.
- **단점 및 주의사항**: 최신 JDK에서는 대부분 기본적으로 활성화되어 있어서 큰 차이를 느끼지 못할 수 있다.

## 환경 설정 관련 옵션

`-Djava.net.preferIPv4Stack=true`
- **의미**: 네트워크 통신 시 IPv4를 우선적으로 사용한다.
- **특징 및 장점**: IPv4 환경에서의 네트워크 문제를 예방할 수 있다.
- **단점 및 주의사항**: IPv6 네트워크에서의 성능이 저하될 수 있다.

`-Dkotlinx.coroutines.debug=off`
- **의미**: Kotlin의 코루틴 디버깅 정보를 비활성화한다.
- **특징 및 장점**: 디버깅 관련 오버헤드를 줄여 성능을 개선할 수 있다.
- **단점 및 주의사항**: 디버깅 시 필요한 정보를 얻지 못할 수 있다.

`-Dsun.io.useCanonCaches=false`
- **의미**: Canonicalized 경로의 캐싱을 비활성화한다.
- **특징 및 장점**: 파일 시스템이 변경될 때 더 정확하게 처리할 수 있다.
- **단점 및 주의사항**: 캐싱 비활성화로 성능이 저하될 수 있다.

`-Dsun.java2d.d3d=false`
- **의미**: Java2D에서 Direct3D 가속을 비활성화한다.
- **특징 및 장점**: 그래픽 가속 기능을 비활성화함으로써 Direct3D와 관련된 문제를 방지할 수 있다. 특히, Direct3D가 잘 지원되지 않는 환경에서 성능을 개선할 수 있다.
- **단점 및 주의사항**: 그래픽 성능이 약간 저하될 수 있다. 그래픽이 복잡한 작업을 수행할 때 Direct3D 가속이 유용할 수 있기 때문에, 특정 환경에서만 이 옵션을 사용하는 것이 좋다.

`-Dsun.java2d.metal=true`
- **의미**: macOS에서 Metal API를 사용하도록 설정한다.
- **특징 및 장점**: Metal API는 macOS에서 그래픽 성능을 크게 향상시킬 수 있는 최신 그래픽 렌더링 기술로, Metal API를 사용하면 렌더링 성능이 최적화되어 부드럽고 빠른 그래픽 처리를 경험할 수 있다.
- **단점 및 주의사항**: Metal API가 지원되지 않는 오래된 macOS 버전에서는 이 옵션이 무시될 수 있다.

`-Dsun.java2d.opengl=false`, `-Dawt.java2d.opengl=false`
- **의미**: OpenGL 렌더링을 활성화 할 것인지에 대한 여부
- **특징 및 장점**: OpenGL 렌더링을 활성화 하여 IDE의 성능을 개선할 수 있다.
- **단점 및 주의사항**: [기본 CPU가 높게 잡히며, IDE가 유휴 상태일때도 CPU & GPU가 높아지는 현상](https://youtrack.jetbrains.com/issue/JBR-5495)이 발생한다.  
  - IDE 성능 차이와 CPU 상황을 보고 `true`와 `false` 를 선택한다.

`-Dsun.awt.mac.a11y.enabled=false`
- **의미**: macOS에서 AWT 접근성 기능을 비활성화한다.
- **특징 및 장점**: 접근성 기능이 필요하지 않은 경우, 불필요한 리소스 사용을 줄일 수 있다. 성능 최적화에도 도움을 줄 수 있다.
- **단점 및 주의사항**: 접근성이 필요한 경우에는 이 옵션을 활성화해야 한다. 접근성 기능이 비활성화되면, 특정 사용자에게는 문제가 될 수 있다.

## Deprecated 옵션

[예전에 적용하던 최적화 옵션들이 이제는 deprecated 되었다.](https://gist.github.com/P7h/4388881?permalink_comment_id=2987898#gistcomment-2987898)  
- [오라클 공식 문서 JVM 옵션](http://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html) 참고

아래 옵션들은 이제 제거한다.

```bash
-XX:+UseParNewGC
-XX:PermSize=512m
-XX:MaxPermSize=512m
-XX:+UseConcMarkSweepGC
-XX:+AggressiveOpts
-XX:+CMSIncrementalMode
-XX:+CMSIncrementalPacing
```

## 옵션 외 성능 개선

### Indexing 범위 제거

IDE를 사용할때 주로 성능 저하가 느껴지는 부분이 Indexing 이다.  
최적화하려면 이 Indexing 과정에서 굳이 대상에 포함시킬 필요가 없는 부분들은 제외시키면 좋다.  
제외시키면 프로젝트에서 볼 수는 있지만, 코드 완성, 탐색, 인덱싱 및 검사에서는 무시되어 IDE의 성능이 개선된다.  
  
보통은 **배포 번들 파일에 포함되면 안될 자동 생성 되는 파일들을 제외하면 된다**.

- node_modules 디렉토리
- out 디렉토리
- logs 디렉토리

적용 방법은 다음과 같이 `Mark Directory as | Excluded` 를 선택하면 된다.

![exclude](./images/exclude.png)

### JBR 변경

IDE를 실행하고 시간이 흘러 메모리가 계속해서 올라가는 메모리 릭 같은 이슈가 발생한다면 아래 링크를 따라 JBR (JetBrains Runtime) 을 교체한다.

- [IntelliJ 등에서 메모리 점유율이 계속 높아질 때 (feat. vanilla JBR)](https://jojoldu.tistory.com/800)

## 참고

- [mahmoudimus/idea-gcg1.vmoptions](https://gist.github.com/mahmoudimus/ce9278d27267e109dda7292cfa1ba253#file-z-9-vmoptions-md)
- [P7h/IntelliJ_IDEA__Perf_Tuning](https://gist.github.com/P7h/4388881)
- [intellij-jvm-options-explained](https://github.com/FoxxMD/intellij-jvm-options-explained?tab=readme-ov-file)