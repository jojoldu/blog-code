# Spring Boot 환경에서 EntityQL 적용하기

```java
Caused by: java.lang.NoSuchMethodError: com.google.common.collect.Sets$SetView.iterator()Lcom/google/common/collect/UnmodifiableIterator;
```

이럴 경우 `reflections:0.9.11` 에서 필요한 Guava의 버전이 `20.0` 인데, 현재 프로젝트의 다른 의존성 때문에 Guava가 다른 버전으로 의존하고 있을 경우에 발생한다. 
이럴 경우 아래와 같이 강제로 Guava 버전을 고정한다.

```groovy
configurations {
    all {
        resolutionStrategy {

            // 특정 모듈의 버전을 강제 지정(최상위건 이행적 의존성이건 무관함)
            force  'com.google.guava:guava:20.0'
        }
    }
}
```
