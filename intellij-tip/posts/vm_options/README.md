# IntelliJ & WebStorm VM Options

```bash
-ea
-server
-Xms1024m
-Xmx5120m
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
--add-exports=java.desktop/com.apple.eawt.event=ALL-UNNAMED
--add-exports=java.desktop/com.apple.eawt=ALL-UNNAMED
--add-exports=java.desktop/com.apple.laf=ALL-UNNAMED
--add-exports=java.desktop/sun.awt.image=ALL-UNNAMED
--add-exports=java.desktop/sun.font=ALL-UNNAMED
--add-opens=java.base/java.io=ALL-UNNAMED
--add-opens=java.base/java.lang.reflect=ALL-UNNAMED
--add-opens=java.base/java.lang=ALL-UNNAMED
--add-opens=java.base/java.net=ALL-UNNAMED
--add-opens=java.base/java.nio.charset=ALL-UNNAMED
--add-opens=java.base/java.text=ALL-UNNAMED
--add-opens=java.base/java.time=ALL-UNNAMED
--add-opens=java.base/java.util.concurrent=ALL-UNNAMED
--add-opens=java.base/java.util=ALL-UNNAMED
--add-opens=java.base/jdk.internal.org.objectweb.asm.tree=ALL-UNNAMED
--add-opens=java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED
--add-opens=java.base/jdk.internal.vm=ALL-UNNAMED
--add-opens=java.base/sun.nio.ch=ALL-UNNAMED
--add-opens=java.desktop/com.apple.eawt.event=ALL-UNNAMED
--add-opens=java.desktop/com.apple.eawt=ALL-UNNAMED
--add-opens=java.desktop/com.apple.laf=ALL-UNNAMED
--add-opens=java.desktop/java.awt.dnd.peer=ALL-UNNAMED
--add-opens=java.desktop/java.awt.event=ALL-UNNAMED
--add-opens=java.desktop/java.awt.image=ALL-UNNAMED
--add-opens=java.desktop/java.awt.peer=ALL-UNNAMED
--add-opens=java.desktop/java.awt=ALL-UNNAMED
--add-opens=java.desktop/javax.swing.plaf.basic=ALL-UNNAMED
--add-opens=java.desktop/javax.swing.text.html=ALL-UNNAMED
--add-opens=java.desktop/javax.swing=ALL-UNNAMED
--add-opens=java.desktop/sun.awt.datatransfer=ALL-UNNAMED
--add-opens=java.desktop/sun.awt.image=ALL-UNNAMED
--add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED
--add-opens=java.desktop/sun.awt.X11=ALL-UNNAMED
--add-opens=java.desktop/sun.awt=ALL-UNNAMED
--add-opens=java.desktop/sun.font=ALL-UNNAMED
--add-opens=java.desktop/sun.java2d=ALL-UNNAMED
--add-opens=java.desktop/sun.lwawt.macosx=ALL-UNNAMED
--add-opens=java.desktop/sun.lwawt=ALL-UNNAMED
--add-opens=java.desktop/sun.swing=ALL-UNNAMED
--add-opens=jdk.attach/sun.tools.attach=ALL-UNNAMED
—add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED
—add-opens=jdk.jdi/com.sun.tools.jdi=ALL-UNNAMED
```

### `-ea`
- **의미**: Assertion을 활성화하는 옵션이다.
- **특징 및 장점**: 코드 내에서 `assert` 문을 사용하여 논리적 오류를 조기에 발견할 수 있다.
- **단점 및 주의사항**: 프로덕션 환경에서는 성능에 영향을 줄 수 있으므로 비활성화하는 것이 좋다.

### `-server`
- **의미**: JVM을 서버 모드로 실행하는 옵션이다.
- **특징 및 장점**: 서버 애플리케이션에 적합한 최적화가 적용된다.
- **단점 및 주의사항**: 짧은 실행 시간의 애플리케이션에서는 이점을 느끼기 어려울 수 있다.

### `-Xms1024m` / `-Xmx5120m`
- **의미**: JVM의 힙 메모리 초기 크기와 최대 크기를 각각 1024MB와 5120MB로 설정한다.
- **특징 및 장점**: 메모리 부족으로 인한 성능 저하를 방지할 수 있다.
- **단점 및 주의사항**: 너무 크게 설정하면 다른 애플리케이션의 메모리 사용에 영향을 줄 수 있다.

### `-Xss256k`
- **의미**: 각 스레드의 스택 크기를 256KB로 설정한다.
- **특징 및 장점**: 메모리 사용을 절약할 수 있다.
- **단점 및 주의사항**: 스택 크기가 너무 작으면 `StackOverflowError`가 발생할 수 있다.

## 고급 JVM 옵션

### `-XX:+UnlockExperimentalVMOptions`
- **의미**: 실험적인 JVM 옵션을 사용할 수 있게 해준다.
- **특징 및 장점**: 최신 기능을 테스트할 수 있다.
- **단점 및 주의사항**: 실험적인 기능은 안정성이 보장되지 않으므로 주의가 필요하다.

### `-XX:-UseSerialGC`, `-XX:-UseParallelGC`, `-XX:-UseG1GC`, `-XX:+UseZGC`
- **의미**: Serial, Parallel, G1, ZGC 가비지 컬렉터를 각각 비활성화 및 활성화한다.
- **특징 및 장점**: ZGC는 큰 힙에서도 저지연 가비지 컬렉션을 제공한다.
- **단점 및 주의사항**: ZGC는 최신 기술이므로, 모든 환경에서 최적의 성능을 발휘하지 않을 수 있다. 상황에 따라 적절한 GC를 선택하는 것이 중요하다.

### `-XX:+IgnoreUnrecognizedVMOptions`
- **의미**: JVM이 인식하지 못하는 옵션을 무시하도록 설정한다.
- **특징 및 장점**: 다양한 환경에서 실행될 때 호환성 문제를 방지할 수 있다.
- **단점 및 주의사항**: 잘못된 옵션이 무시되어 예상치 못한 동작이 발생할 수 있다.

### `-XX:+HeapDumpOnOutOfMemoryError`
- **의미**: OutOfMemoryError 발생 시 힙 덤프를 생성한다.
- **특징 및 장점**: 메모리 문제를 디버깅하는 데 유용하다.
- **단점 및 주의사항**: 덤프 파일이 매우 클 수 있으므로 디스크 공간에 주의해야 한다.

### `-XX:-OmitStackTraceInFastThrow`
- **의미**: FastThrow 예외가 발생해도 스택 트레이스를 포함하도록 설정한다.
- **특징 및 장점**: 디버깅을 쉽게 할 수 있다.
- **단점 및 주의사항**: 성능에 영향을 미칠 수 있다.

### `-XX:ReservedCodeCacheSize=512m`
- **의미**: 코드 캐시의 크기를 512MB로 설정한다.
- **특징 및 장점**: JIT 컴파일된 메서드를 더 많이 캐싱하여 성능을 향상시킬 수 있다.
- **단점 및 주의사항**: 너무 크게 설정하면 다른 메모리 영역에 영향을 줄 수 있다.

### `-XX:SoftRefLRUPolicyMSPerMB=50`
- **의미**: 소프트 참조의 유지 시간을 설정한다.
- **특징 및 장점**: 메모리가 부족할 때 소프트 참조가 더 빨리 해제될 수 있다.
- **단점 및 주의사항**: 소프트 참조 객체가 빨리 해제되어야 할 경우 메모리에서 빠르게 제거되지 않을 수 있다.

### `-XX:+UseStringCache`, `-XX:+UseStringDeduplication`
- **의미**: String 객체의 캐싱과 중복 제거를 활성화한다.
- **특징 및 장점**: 메모리 사용량을 줄여 애플리케이션 성능을 향상시킬 수 있다.
- **단점 및 주의사항**: 약간의 CPU 오버헤드를 수반할 수 있다.

### `-XX:+AggressiveOpts`
- **의미**: 최신의 공격적인 최적화 옵션을 사용한다.
- **특징 및 장점**: JVM의 최신 최적화를 활용해 성능을 극대화할 수 있다.
- **단점 및 주의사항**: 실험적이므로 예상치 못한 문제가 발생할 수 있다.

### `-XX:+AlwaysPreTouch`
- **의미**: JVM이 시작할 때 힙 메모리를 미리 할당한다.
- **특징 및 장점**: 힙 메모리 할당의 일관성을 높여 런타임 성능을 예측 가능하게 만든다.
- **단점 및 주의사항**: 초기화 시간이 길어질 수 있다.

### `-XX:+OptimizeStringConcat`
- **의미**: String 객체의 연결을 최적화한다.
- **특징 및 장점**: 문자열 연결 성능이 향상된다.
- **단점 및 주의사항**: 최신 JDK에서는 대부분 기본적으로 활성화되어 있어서 큰 차이를 느끼지 못할 수 있다.

### `-XX:+UseFastAccessorMethods`
- **의미**: 접근자 메서드를 최적화하여 빠르게 실행되도록 한다.
- **특징 및 장점**: 게터와 세터의 성능이 개선될 수 있다.
- **단점 및 주의사항**: 최신 JVM에서는 기본적으로 적용될 수 있는 최적화다.

## 환경 설정 관련 옵션

### `-Dide.no.platform.update=true`
- **의미**: IntelliJ 플랫폼 업데이트를 비활성화한다.
- **특징 및 장점**: 업데이트로 인한 환경 변화나 중단을 방지할 수 있다.
- **단점 및 주의사항**: 최신 보안 패치나 기능 업데이트를 놓칠 수 있다.

### `-Djava.net.preferIPv4Stack=true`
- **의미**: 네트워크 통신 시 IPv4를 우선적으로 사용한다.
- **특징 및 장점**: IPv4 환경에서의 네트워크 문제를 예방할 수 있다.
- **단점 및 주의사항**: IPv6 네트워크에서의 성능이 저하될 수 있다.

### `-Djdk.attach.allowAttachSelf=true`
- **의미**: 애플리케이션 자체에 대한 JVM의 attach API 사용을 허용한다.
- **특징 및 장점**: 자기 디버깅이나 모니터링 도구를 실행할 수 있다.
- **단점 및 주의사항**: 보안상의 문제가 발생할 수 있다.

### `-Djdk.module.illegalAccess.silent=true`
- **의미**: 불법 접근 시 경고를 무시하고 조용히 실행되도록 설정한다.
- **특징 및 장점**: 모듈화된 코드에서의 호환성 문제를 무시할 수 있다.
- **단점 및 주의사항**: 경고를 무시하면 문제를 발견하기 어려울 수 있다.

### `-Dkotlinx.coroutines.debug=off`
- **의미**: Kotlin의 코루틴 디버깅 정보를 비활성화한다.
- **특징 및 장점**: 디버깅 관련 오버헤드를 줄여 성능을 개선할 수 있다.
- **단점 및 주의사항**: 디버깅 시 필요한 정보를 얻지 못할 수 있다.

### `-Dsun.io.useCanonCaches=false`
- **의미**: Canonicalized 경로의 캐싱을 비활성화한다.
- **특징 및 장점**: 파일 시스템이 변경될 때 더 정확하게 처리할 수 있다.
- **단점 및 주의사항**: 캐싱 비활성화로 성능이 저하될 수 있다.