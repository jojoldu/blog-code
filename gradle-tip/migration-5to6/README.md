# Gradle5 -> 6 마이그레이션

> 프로젝트 사용하면서 계속 Update 됩니다.

프로젝트의 Gradle 버전업은 다음과 같은 방법으로 진행할 수 있습니다.

```bash
gradle wrapper --gradle-version 6.7.1
```

## compile

### implementation

### api

```groovy

```

## Jacoco

```groovy
executionData = files("${buildDir}/jacoco/jacoco.exec")
```

```groovy
executionData.from = files("${buildDir}/jacoco/jacoco.exec")
```

## Node Plugin

기존에 사용하던 Node Gradle 플러그인이 더이상 개선되지 않는 이슈

* [Github 이슈](https://github.com/srs/gradle-node-plugin/issues/351)

그래서 해당 플러그인을 Fork한 [신규 플러그인](https://github.com/node-gradle/gradle-node-plugin)으로 교체가 필요

```groovy
plugins {
    id 'com.moowork.node' version '1.3.1'
}

apply plugin: 'com.moowork.node'
```

```groovy
plugins {
    id "com.github.node-gradle.node" version "2.2.4"
}

apply plugin: 'com.github.node-gradle.node'
```

## Querydsl Plugin

Honeymon님의 블로그글에서 아주 상세하게 적혀있기 때문에 여기서는 최종 코드만 반영

* [그레이들 Annotation processor 와 Querydsl](http://honeymon.io/tech/2020/07/09/gradle-annotation-processor-with-querydsl.html)

일단 아래처럼 하면 Gradle5에서도 정상작동한다.

```groovy
apply plugin: "com.ewerk.gradle.plugins.querydsl"

def queryDslDir = "src/main/generated"
querydsl {
    library = "com.querydsl:querydsl-apt:4.2.2" // 사용할 AnnotationProcesoor 정의
    jpa = true
    querydslSourcesDir = queryDslDir
}
sourceSets {
    main {
        java {
            srcDir queryDslDir
        }
    }
}

compileQuerydsl {
    options.annotationProcessorPath = configurations.querydsl
}

configurations {
    querydsl.extendsFrom compileClasspath
}
```

Gradle6부터는 다음과 같이 사용한다.

* Querydsl Plugin이 제거된다.

```groovy
configure(querydslProjects) {
    apply plugin: "io.spring.dependency-management"

    dependencies {
        compile "com.querydsl:querydsl-jpa"
        compile "com.querydsl:querydsl-apt"
        annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jpa" // querydsl JPAAnnotationProcessor 사용 지정
        annotationProcessor "jakarta.persistence:jakarta.persistence-api:2.2.3"
        annotationProcessor "jakarta.annotation:jakarta.annotation-api:1.3.5"
    }

    // querydsl 적용
    def generated='src/main/generated'
    sourceSets {
        main.java.srcDirs += [ generated ]
    }

    tasks.withType(JavaCompile) {
        options.annotationProcessorGeneratedSourcesDirectory = file(generated)
    }

    clean.doLast {
        file(generated).deleteDir()
    }
}
```