# aws-springboot-nginx

### RDS

[보안그룹 접속](https://ap-northeast-2.console.aws.amazon.com/ec2/v2/home?region=ap-northeast-2#SecurityGroups:sort=groupId) 

mysql client 설치

```bash
sudo yum install mysql
```

EC2에서 RDS 접근 확인

```bash
mysql -u rds계정 -p -h rds주소
```


real.application.yml

```yaml

---

spring:
        profiles: real

#Connection
spring.datasource:
        url: jdbc:mariadb://devbeginner.clc9ewnkxgoa.ap-northeast-2.rds.amazonaws.com:3306/devbeginner
        username: rds 계정
        password: rds 비밀번호
        driver-class-name: org.mariadb.jdbc.Driver
        hikari:
                connectionTimeout: 30000 #30s
                idleTimeout: 600000      #10m
                maxLifetime: 1800000     #30m
                maximumPoolSize: 100
```


### 스프링부트 배포

AWS Java8 업데이트

[참고](http://ithub.tistory.com/58)

설치 가능한 버전 확인

```bash
yum list java*jdk-devel
```

Java8 설치

```bash
sudo yum install -y java-1.8.0-openjdk-devel.x86_64
```

Java 버전 변경

```bash
sudo /usr/sbin/alternatives --config java
```

사용하지 않는 java7 삭제

```bash
sudo yum remove java-1.7.0-openjdk
```


임시 실행

```bash
java -jar -Dspring.profiles.active=real ./dev-beginner-group-web-0.0.1-SNAPSHOT.jar
```