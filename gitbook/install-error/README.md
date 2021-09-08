# gitbook CLI 실행시 cb.apply 발생할 경우

외부 강의를 위해 gitbook을 처음 사용해보았습니다.  
설치나 사용법 자체는 워낙 다른 블로그들에서 소개를 많이 해주기 때문에 여기서 소개하지는 않겠습니다.  
  
간단하게 다음과 같이 설치와 프로젝트를 구성할 수 있습니다.

```bash
npm install -g gitbook-cli
```

## 문제


설치가 잘되었는지 확인을 해보면 

```bash
gitbook -h
```

다음의 에러를 만나게 됩니다.

```typescript
Installing GitBook 3.2.3
/usr/local/lib/node_modules/gitbook-cli/node_modules/npm/node_modules/graceful-fs/polyfills.js:287
      if (cb) cb.apply(this, arguments)
TypeError: cb.apply is not a function
    at /usr/local/lib/node_modules/gitbook-cli/node_modules/npm/node_modules/graceful-fs/polyfills.js:287:18

```

이 에러의 경우 gitbook cli의 내부 의존성 중 하나인 `graceful-fs` 문제인데요.  
이미 `graceful-fs` 에서는 [문제가 해결 되었지만](https://github.com/isaacs/node-graceful-fs/commit/168bdb8f0bb3174e8499d4bc5878deead4172c39), gitbook cli에서는 `graceful-fs` 업데이트를 하지 않았기 때문입니다.  
  
그래서 다음과 같이 전역 (`-g`) 로 설치된 gitbook CLI node_modules로 이동을 한뒤,

```bash
cd /usr/local/lib/node_modules/gitbook-cli/node_modules/npm/node_modules/
```

> Mac 기준입니다.

해당 위치에서 `graceful-fs` 를 최신 버전으로 업데이트 합니다.

```bash
npm install graceful-fs@latest --save
```

그리고 다시 CLI를 수행해보면?

```bash
$ gitbook -h

  Usage: gitbook [options] [command]


  Options:

    -v, --gitbook [version]  specify GitBook version to use
    -d, --debug              enable verbose error
    -V, --version            Display running versions of gitbook and gitbook-cli
    -h, --help               output usage information


  Commands:

    ls                        List versions installed locally
    current                   Display currently activated version
    ls-remote                 List remote versions available for install
    fetch [version]           Download and install a <version>
    alias [folder] [version]  Set an alias named <version> pointing to <folder>
    uninstall [version]       Uninstall a version
    update [tag]              Update to the latest version of GitBook
    help                      List commands for GitBook
    *                         run a command with a specific gitbook version
```

정상적으로 설치가 완료 된 것을 확인할 수 있습니다.
