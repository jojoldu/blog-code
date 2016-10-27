# Spring 캐시 서비스
팀내 신입사원들이 입사하게 되어 간단하게나마 참고할 수 있도록 Spring Cache에 대해 샘플예제와 소개를 정리하게 되었다. <br/>
아주 간단한 예제이기도 하고, 웬만한 경력 웹 개발자분들은 다 아는 이야기라 한번도 캐시를 사용해보지 않은 분들에게 도움이 될것 같다. <br/>
여기서 사용할 CacheManager는 **EhCache** 이다. <br/>
Spring Cache의 대표격이라고 생각했다. <br/>
Redis나 Memcached를 선택하지 않은 이유는 Cache 본연의 기능에 초점을 맞추기가 힘들어 여러가지 한눈팔기 쉬울것 같아서였다. <br/>
(물론 우리회사 시스템은 Ehcache/Redis/Memcached를 다 쓰고 있다.) <br/>
그럼 이제 시작하겠다.

### 소개
동일한 요청이 들어오면 복잡한 작업을 수행해서 결과를 만드는 대신 이미 보관된 결과를 바로 돌려주는 방식을 **캐시** 라고 한다. <br/>
일반적으로 사용자가 만드는 데이터 보다는 **서비스에서 제공하는 컨텐츠(뉴스,허브,실시간 검색어 등)에** 대부분 적용해서 사용 중이다 <br/>

![캐시예제](./images/줌캐시.png)

(이런 데이터의 경우 관리자들로 인해 최소 1분에서 최대 하루까지 같은 데이터가 노출되서 캐시하기 딱 좋다.) <br/>
<br/>
캐시의 경우 모든 상황에서 쓸 수 있는 것은 아니다. 아래의 조건을 만족한다면 캐시 사용을 한번 고려해보는 것이 좋다.<br/>
* 반복적으로 동일한 결과를 돌려주는 작업
* 각 작업의 시간이 오래 걸리거나 서버에 부담을 주는 경우 (외부 API/DB 데이터호출 등)

### 예제
예제 코드는 SpringBoot + Gradle + Ehcache 2.x + Logback 환경이다. <br/>
**캐시** 기능에 집중하기 위해서 불필요한 설정은 제외하기 위해 선택하였다. <br/>
그럼 하나하나 코드를 작성해나가겠다. <br/>
<br/>

**build.gradle**

```
buildscript {
	ext {
		springBootVersion = '1.4.1.RELEASE'
	}
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
	}
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'spring-boot'

jar {
	baseName = 'spring-cache'
	version = '0.0.1-SNAPSHOT'
}
sourceCompatibility = 1.8
targetCompatibility = 1.8

repositories {
	mavenCentral()
}


dependencies {
	compile('org.springframework.boot:spring-boot-starter-cache')
	compile('org.springframework.boot:spring-boot-starter-web')

	compile group: 'net.sf.ehcache', name: 'ehcache', version: '2.10.3'

	testCompile('org.springframework.boot:spring-boot-starter-test')
}
```

spring-boot-starter-cache는 기존의 캐시관련 설정을 편리하게 지원해주는 패키지이다. 덕분에 CacheManager, EhCacheManagerFactoryBean 등의 bean 생성을 직접 안할수 있게 되었다. <br/>
spring-boot-starter-cache는 ConcurrentHashMap을 기본 CacheManager로 하고 있어서 Ehcache 2.x 로 교체하기 위해 직접 의존성을 추가하였다. <br/>

혹시나 SpringBoot 환경이 아니라면 [mykong](https://www.mkyong.com/spring/spring-caching-and-ehcache-example/) 님의 포스팅을 따라 설정하면 된다.
