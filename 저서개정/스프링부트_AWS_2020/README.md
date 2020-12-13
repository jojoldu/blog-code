# 스프링 부트와 AWS로 혼자 구현하는 웹 서비스 (2020.12.12)

* Spring Boot 2.4.1
* Gradle 6.7.1
* IntelliJ IDEA 2020.3
* Junit5

## 0. 기존에 구성했다면

## 1. build.gradle

```groovy
plugins {
    id 'org.springframework.boot' version '2.4.1' // RELEASE 삭제
    id 'io.spring.dependency-management' version '1.0.10.RELEASE'
    id 'java'
}

group 'com.jojoldu.book'
version '1.0.4-SNAPSHOT-'+new Date().format("yyyyMMddHHmmss")
sourceCompatibility = 1.8   

repositories {
    mavenCentral()
    jcenter()
}

// for Junit 5
test {
    useJUnitPlatform()
}

dependencies {
    implementation('org.springframework.boot:spring-boot-starter-web')
    implementation('org.springframework.boot:spring-boot-starter-mustache')

    // lombok
    implementation('org.projectlombok:lombok')
    annotationProcessor('org.projectlombok:lombok')
    testImplementation('org.projectlombok:lombok')
    testAnnotationProcessor('org.projectlombok:lombok')

    implementation('org.springframework.boot:spring-boot-starter-data-jpa')
    implementation("org.mariadb.jdbc:mariadb-java-client")
    implementation('com.h2database:h2')

    implementation('org.springframework.boot:spring-boot-starter-oauth2-client')
    implementation('org.springframework.session:spring-session-jdbc')

    testImplementation('org.springframework.boot:spring-boot-starter-test')
    testImplementation("org.springframework.security:spring-security-test")
}
```

## 2. Junit

Mac

* CMD + Shift + R

Windows/Linux

* Ctrl + Shift + R

**ReplaceAll**


### @Test

### @RunWith

### SpringRunner

**as-is**

```java
org.springframework.test.context.junit4.SpringRunner
```

**to-be**

```java
org.springframework.test.context.junit.jupiter.SpringExtension
```

### @After

### @Before

```java
org.junit.Before
```

```java
org.junit.jupiter.api.BeforeEach
```

### 주의

```java
No tests found for given includes
```

![test-fail1](./images/test-fail1.png)

build.gradle에 아래 옵션이 빠진건 아닌지 다시 1번의 build.gradle 옵션을 확인해봅니다.

```groovy
test {
    useJUnitPlatform()
}
```

## JPA

### application.properties

**as-is**

```properties
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
```

**to-be**

```properties
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
spring.jpa.properties.hibernate.dialect.storage_engine=innodb
spring.datasource.hikari.jdbc-url=jdbc:h2:mem:testdb;MODE=MYSQL
spring.datasource.hikari.username=sa
```

* ```spring.datasource.hikari.jdbc-url```
  * real-db를 사용할 경우 override 됩니다.

### application-real-db.properties

**as-is**

```properties
spring.jpa.hibernate.ddl-auto=none

spring.datasource.url=jdbc:mariadb://rds주소:포트명(기본은 3306)/database명
spring.datasource.username=db계정
spring.datasource.password=db계정 비밀번호
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
```

**to-be**

```properties
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show_sql=false

spring.datasource.hikari.jdbc-url=jdbc:mariadb://rds주소:포트명(기본은 3306)/database명
spring.datasource.hikari.username=db계정
spring.datasource.hikari.password=db계정 비밀번호
spring.datasource.hikari.driver-class-name=org.mariadb.jdbc.Driver
```