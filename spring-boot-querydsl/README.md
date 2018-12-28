# Spring Boot Data Jpa 프로젝트에 Querydsl 적용하기

안녕하세요?  
이번 시간에는 Spring Boot Data Jpa 프로젝트에 [Querydsl](http://www.querydsl.com/)을 적용하는 방법을 소개 드리겠습니다.  
  
Spring Data Jpa를 써보신 분들은 아시겠지만, 기본으로 제공해주는 ```@Query```로는 다양한 조회 기능에서 사용하기에 한계가 있습니다.  
  
그래서 이 문제를 해결하기 위해 정적 체크가 가능한 조회 프레임워크를 사용하는데요.  
  
Querydsl은 Jooq와 함게 **가장 유명한 조회 프레임워크**입니다.  
이번 포스팅에서는 Querydsl의 장점 혹은 왜 써야하는지 등의 내용은 담지 않습니다.  

> 이건 나중에 한번 각잡고 작성해서 공유드리겠습니다 :)

어떻게 설정해서 사용하는지를 설명드리겠습니다.

## 1. 설정

> 만약 Gradle Multi Module에서 어떻게 사용하는지 궁금하신 분들은 [제 개인프로젝트](https://github.com/jojoldu/bns/blob/master/build.gradle)를 참고해보세요


플러그인 등록

```groovy
buildscript {
    ext {
        ...
        querydslPluginVersion = '1.0.10' // 플러그인 버전
    }
    repositories {
        ...
        maven { url "https://plugins.gradle.org/m2/" } // plugin 저장소
    }
    dependencies {
        ...
        classpath("gradle.plugin.com.ewerk.gradle.plugins:querydsl-plugin:${querydslPluginVersion}") // querydsl 의존성 등록
    }
}
```

의존성 등록

```groovy
dependencies {
    compile("com.querydsl:querydsl-jpa") // querydsl
    compile("com.querydsl:querydsl-apt") // querydsl
    ...
}
```

Gradle 설정

```groovy
// querydsl 적용
apply plugin: "com.ewerk.gradle.plugins.querydsl"
def querydslSrcDir = 'src/main/generated'

querydsl {
    library = "com.querydsl:querydsl-apt"
    jpa = true
    querydslSourcesDir = querydslSrcDir
}

sourceSets {
    main {
        java {
            srcDirs = ['src/main/java', querydslSrcDir]
        }
    }
}
```

전체 코드는 아래와 같습니다.

```groovy
buildscript {
    ext {
        springBootVersion = '2.1.1.RELEASE'
        querydslPluginVersion = '1.0.10'
    }
    repositories {
        mavenCentral()
        maven { url "https://plugins.gradle.org/m2/" } // plugin 저장소
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        classpath("gradle.plugin.com.ewerk.gradle.plugins:querydsl-plugin:${querydslPluginVersion}")
    }
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

group = 'com.jojoldu.blogcode'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.8

repositories {
    mavenCentral()
}


dependencies {
    compile("com.querydsl:querydsl-jpa") // querydsl
    compile("com.querydsl:querydsl-apt") // querydsl

    compile('org.springframework.boot:spring-boot-starter-data-jpa')
    compile('org.springframework.boot:spring-boot-starter-web')

    runtimeOnly('com.h2database:h2')
    compileOnly('org.projectlombok:lombok')
    testImplementation('org.springframework.boot:spring-boot-starter-test')
}

// querydsl 적용
apply plugin: "com.ewerk.gradle.plugins.querydsl"
def querydslSrcDir = 'src/main/generated'

querydsl {
    library = "com.querydsl:querydsl-apt"
    jpa = true
    querydslSourcesDir = querydslSrcDir
}

sourceSets {
    main {
        java {
            srcDirs = ['src/main/java', querydslSrcDir]
        }
    }
}
```

## 2. 기본 사용법

## 3. Spring Data Jpa Custom Repository 적용

* [Spring Data 공식 문서](https://docs.spring.io/spring-data/jpa/docs/2.1.3.RELEASE/reference/html/#repositories.custom-implementations)


## 참고
