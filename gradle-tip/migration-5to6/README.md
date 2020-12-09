# Gradle5 -> 6 마이그레이션

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