# IntelliJ & WebStorm VM Options

```bash
-ea
-server
-Xms1024m
-Xmx4096m
-XX:NewRatio=4
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
-Dsun.tools.attach.tmp.only=true
-Dsun.awt.mac.a11y.enabled=false
```

`-ea`
- **소개**: Assertion을 활성화하는 옵션이다.
- **특징 및 장점**: IntelliJ 등 JVM 환경에서 코드를 작성할 떄 별도의 추가 옵션 없이 `assert` 문을 사용할 수 있다.
- **단점 및 주의사항**: WebStorm 등 JVM 개발 환경이 아닌 경우 굳이 넣을 필요가 없다.
- 사례: [Where to add compiler options like -ea in IntelliJ IDEA?](https://stackoverflow.com/questions/18168257/where-to-add-compiler-options-like-ea-in-intellij-idea)

`-server`
- **소개**: JVM을 서버 모드로 실행하는 옵션이다.
- **특징 및 장점**: 서버 애플리케이션에 적합한 최적화가 적용된다.
- **단점 및 주의사항**: 짧은 실행 시간의 애플리케이션에서는 이점을 느끼기 어려울 수 있다.

`-Xms1024m` /`-Xmx4096m`
- **소개**: JVM의 힙 메모리 초기 크기와 최대 크기를 각각 1024MB와 4096MB로 설정한다.
- **특징 및 장점**: 메모리 부족으로 인한 성능 저하를 방지할 수 있다.
- **단점 및 주의사항**: 너무 크게 설정하면 다른 애플리케이션의 메모리 사용에 영향을 줄 수 있다.

`-Xss256k`
- **소개**: 각 스레드의 스택 크기를 256KB로 설정한다.
- **특징 및 장점**: 메모리 사용을 절약할 수 있다.
- **단점 및 주의사항**: 스택 크기가 너무 작으면`StackOverflowError`가 발생할 수 있다.

## 고급 JVM 옵션

`-XX:+UnlockExperimentalVMOptions`
- **소개**: 실험적인 JVM 옵션을 사용할 수 있게 해준다.
- **특징 및 장점**: 최신 기능을 테스트할 수 있다.
- **단점 및 주의사항**: 실험적인 기능은 안정성이 보장되지 않으므로 주의가 필요하다.

`-XX:-UseSerialGC`,`-XX:-UseParallelGC`,`-XX:-UseG1GC`,`-XX:+UseZGC`
- **소개**: Serial, Parallel, G1, ZGC 가비지 컬렉터를 각각 비활성화 및 활성화한다.
- **특징 및 장점**: ZGC는 큰 힙에서도 저지연 가비지 컬렉션을 제공한다.
- **단점 및 주의사항**: ZGC는 최신 기술이므로, 모든 환경에서 최적의 성능을 발휘하지 않을 수 있다. 상황에 따라 적절한 GC를 선택하는 것이 중요하다.

`-XX:+IgnoreUnrecognizedVMOptions`
- **소개**: JVM이 인식하지 못하는 옵션을 무시하도록 설정한다.
- **특징 및 장점**: 다양한 환경에서 실행될 때 호환성 문제를 방지할 수 있다.
- **단점 및 주의사항**: 잘못된 옵션이 무시되어 예상치 못한 동작이 발생할 수 있다.
- 사례: [How to fix "Unrecognized VM option 'UseConcMarkSweepGC'" error?](https://stackoverflow.com/questions/65546614/how-to-fix-unrecognized-vm-option-useconcmarksweepgc-error)

`-XX:+HeapDumpOnOutOfMemoryError`
- **소개**: OutOfMemoryError 발생 시 힙 덤프를 생성한다.
- **특징 및 장점**: 메모리 문제를 디버깅하는 데 유용하다.
- **단점 및 주의사항**: 덤프 파일이 매우 클 수 있으므로 디스크 공간에 주의해야 한다.

`-XX:-OmitStackTraceInFastThrow`
- **소개**: FastThrow 예외가 발생해도 스택 트레이스를 포함하도록 설정한다.
- **특징 및 장점**: 디버깅을 쉽게 할 수 있다.
- **단점 및 주의사항**: 성능에 영향을 미칠 수 있다.

`-XX:ReservedCodeCacheSize=512m`
- **소개**: 코드 캐시의 크기를 512MB로 설정한다.
- **특징 및 장점**: JIT 컴파일된 메서드를 더 많이 캐싱하여 성능을 향상시킬 수 있다.
- **단점 및 주의사항**: 너무 크게 설정하면 다른 메모리 영역에 영향을 줄 수 있다.

`-XX:SoftRefLRUPolicyMSPerMB=50`
- **소개**: 소프트 참조의 유지 시간을 설정한다.
- **특징 및 장점**: 메모리가 부족할 때 소프트 참조가 더 빨리 해제될 수 있다.
- **단점 및 주의사항**: 소프트 참조 객체가 빨리 해제되어야 할 경우 메모리에서 빠르게 제거되지 않을 수 있다.

`-XX:+UseStringCache`,`-XX:+UseStringDeduplication`
- **소개**: String 객체의 캐싱과 중복 제거를 활성화한다.
- **특징 및 장점**: 메모리 사용량을 줄여 애플리케이션 성능을 향상시킬 수 있다.
- **단점 및 주의사항**: 약간의 CPU 오버헤드를 수반할 수 있다.

`-XX:+AlwaysPreTouch`
- **소개**: JVM이 시작할 때 힙 메모리를 미리 할당한다.
- **특징 및 장점**: 힙 메모리 할당의 일관성을 높여 런타임 성능을 예측 가능하게 만든다.
- **단점 및 주의사항**: 초기화 시간이 길어질 수 있다.

`-XX:+OptimizeStringConcat`
- **소개**: String 객체의 연결을 최적화한다.
- **특징 및 장점**: 문자열 연결 성능이 향상된다.
- **단점 및 주의사항**: 최신 JDK에서는 대부분 기본적으로 활성화되어 있어서 큰 차이를 느끼지 못할 수 있다.

## 환경 설정 관련 옵션

`-Djava.net.preferIPv4Stack=true`
- **소개**: 네트워크 통신 시 IPv4를 우선적으로 사용한다.
- **특징 및 장점**: IPv4 환경에서의 네트워크 문제를 예방할 수 있다.
- **단점 및 주의사항**: IPv6 네트워크에서의 성능이 저하될 수 있다.

`-Dkotlinx.coroutines.debug=off`
- **소개**: Kotlin의 코루틴 디버깅 정보를 비활성화한다.
- **특징 및 장점**: 디버깅 관련 오버헤드를 줄여 성능을 개선할 수 있다.
- **단점 및 주의사항**: 디버깅 시 필요한 정보를 얻지 못할 수 있다.

`-Dsun.io.useCanonCaches=false`
- **소개**: Canonicalized 경로의 캐싱을 비활성화한다.
- **특징 및 장점**: 파일 시스템이 변경될 때 더 정확하게 처리할 수 있다.
- **단점 및 주의사항**: 캐싱 비활성화로 성능이 저하될 수 있다.

`-Dsun.java2d.d3d=false`
- **의미**: Java2D에서 Direct3D 가속을 비활성화한다.
- **특징 및 장점**: 그래픽 가속 기능을 비활성화함으로써 Direct3D와 관련된 문제를 방지할 수 있다. 특히, Direct3D가 잘 지원되지 않는 환경에서 성능을 개선할 수 있다.
- **단점 및 주의사항**: 그래픽 성능이 약간 저하될 수 있다. 그래픽이 복잡한 작업을 수행할 때 Direct3D 가속이 유용할 수 있기 때문에, 특정 환경에서만 이 옵션을 사용하는 것이 좋다.

`-Dsun.java2d.metal=true`
- **의미**: macOS에서 Metal API를 사용하도록 설정한다.
- **특징 및 장점**: Metal API는 macOS에서 그래픽 성능을 크게 향상시킬 수 있는 최신 그래픽 렌더링 기술이다. Metal API를 사용하면 렌더링 성능이 최적화되어 부드럽고 빠른 그래픽 처리를 경험할 수 있다.
- **단점 및 주의사항**: Metal API가 지원되지 않는 오래된 macOS 버전에서는 이 옵션이 무시될 수 있다.

`-Dsun.java2d.opengl=false`
- **의미**: Java2D에서 OpenGL 가속을 비활성화한다.
- **특징 및 장점**: OpenGL 가속을 비활성화하여 OpenGL 관련 충돌이나 성능 문제를 예방할 수 있다. OpenGL 드라이버가 불안정하거나 문제가 있는 경우 유용하다.
- **단점 및 주의사항**: OpenGL 가속을 활용할 수 있는 환경에서 그래픽 성능이 저하될 수 있다.

`-Dsun.awt.mac.a11y.enabled=false`
- **의미**: macOS에서 AWT 접근성 기능을 비활성화한다.
- **특징 및 장점**: 접근성 기능이 필요하지 않은 경우, 불필요한 리소스 사용을 줄일 수 있다. 성능 최적화에도 도움을 줄 수 있다.
- **단점 및 주의사항**: 접근성이 필요한 경우에는 이 옵션을 활성화해야 한다. 접근성 기능이 비활성화되면, 특정 사용자에게는 문제가 될 수 있다.

## 기타

[예전에 적용하던 최적화 옵션들이 이제는 deprecated 되었다.](https://gist.github.com/P7h/4388881?permalink_comment_id=2987898#gistcomment-2987898)  
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