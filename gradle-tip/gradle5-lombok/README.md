# Gradle5 에서 Lombok 설정하기

```groovy
plugins {
    id 'io.freefair.lombok' version '4.1.2'
}

dependencies {
    annotationProcessor 'org.projectlombok:lombok'
    implementation 'org.projectlombok:lombok'
}

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}
```

멀티모듈이라면

```groovy
plugins {
    id 'io.freefair.lombok' version '4.1.2'
}

subprojects {
    apply plugin: 'io.freefair.lombok'

    dependencies {
        annotationProcessor 'org.projectlombok:lombok'
        implementation 'org.projectlombok:lombok'
    }
    
    configurations {
        compileOnly {
            extendsFrom annotationProcessor
        }
    }
}

```