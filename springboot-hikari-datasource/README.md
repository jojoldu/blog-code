# Spring Boot & Hikari Datasource 연동하기

안녕하세요? 이번 시간엔 Spring Boot & Hikari Datasource 연동하기 예제를 진행해보려고 합니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/springboot-hikari-datasource)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>

기존에 SpringBoot에선 tomcat-jdbc를 기본 Datasource 로 제공했었는데요.  
**2.0부터 HikariCP가 기본**으로 변경되었습니다. ([참고](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.0-Migration-Guide#configuring-a-datasource))  
HikariCP는 이전 버전에서도 많은 분들이 설정을 변경해서 사용했을정도로 인기가 많았습니다.  
HikariCP의 Datasource 정보를 설정하는것에 막히신 분들 혹은 오해하고 계신분들이 계셔서 한번 정리하게 되었습니다.    
  
## 본문

기존에 application.yml, application.properties에서 Datasource를 설정할때는 다음과 같이 설정했습니다.

```yaml
spring:
  profiles: version1
  datasource:
    url: jdbc:h2:mem://localhost/~/jojoldu;MVCC=TRUE
    username: jojoldu
    password: 1234
    driver-class-name: org.h2.Driver
```

datasource.url을 두고 값을 설정했는데요.  
이렇게 설정하고 별도로 Java Config로 설정하지 않은채로 Datasource를 확인해보면!

![1](./images/1.png)

정상적으로 적용된걸 볼 수 있습니다.  
이건 **기존 Tomcat Jdbc나 HikariCP 둘다 동일하게 적용**됩니다.  
여기서 Datasource 값 세팅을 Java Config로 할때면 문제가 발생하는데요.

> DB를 2개 이상 사용해야할 경우 이렇게 직접 Datasource를 만들어야 합니다.  

![2](./images/2.png)
 
이렇게 ```jdbcUrl is required with driverClassName``` 에러가 발생합니다.  
오류가 난 이유는 간단합니다.  
HikariCP의 Database URL 설정은 **url이 아닌 jdbcUrl**을 사용하기 때문입니다.

![3](./images/3.png)

그래서 이 문제를 해결하기 위해 구글링을 해보면 대부분 datasource.url을 datasource.jdbc-url로 변경하라고 합니다. ([참고](https://stackoverflow.com/a/49141541))

![4](./images/4.png)

이렇게 할 경우 Java Config로 만드는 HikariCP의 Datasource url 문제는 해결되는데요.  
이것 때문에 오해하시는 분들이 제법 계십니다.  
HikariCP를 쓸때는 무조건 jdbc-url로 값을 설정해야하는건 아닙니다.  
자동설정의 경우엔 **datasource.jdbc-url이 아닌 datasource.url이 Datasource의 url이 됩니다**.  
datasource.jdbc-url로 설정된 값은 자동설정에선 인식하지 못합니다.  
  
근데 이 문제가 로컬에선 발견 못할 확률이 높습니다.  
H2에선 **값이 없을 경우 기본값을 세팅해서 실행**시키기 때문인데요.
  
예를 들어 아래와 같이 설정하고, Java Config를 쓰지않고, 자동설정 방법을 쓰게 되면

![5](./images/5.png)

보시는것처럼 설정된 url인 ```localhost/jojoldu```이 아닌 H2 기본값인 ```localhost/testdb``` 가 적용된채로 실행됩니다.  
이러다보니 자세하게 확인하지 않고, 단순 실행 & 테스트 코드만 실행해보면 정상작동하기 때문에 그냥 지나칠때가 많습니다.  
즉, 정리하면

* 자동 설정
  * ```spring.datasource.url```이 모든 Datasource의 url이 된다.
* 수동 설정 (Java Config)
  * ```spring.datasource.jdbc-url```로 해야 HikariCP가 인식한다. 

상황에 따라 application.properties, application.yml 값을 변경하는건 불편하고 오해가 생길 여지가 있습니다.  
그래서 SpringBoot에선 HikariCP의 설정이 추가로 있습니다.  
application.properties, application.yml에 ```spring.datasource.hikari```로 값을 세팅하시면 됩니다.

![6](./images/6.png)

만약 Java Config로 설정한다고 해도 아래처럼 ```spring.datasource.hikari```를 지정하시면 똑같이 작동합니다.

![7](./images/7.png)

즉, HikariCP를 사용할경우 ```spring.datasource```로 값을 설정하기 보다는 ```spring.datasource.hikari```로 하시면 수동/자동 구분없이, 오해없이 설정할 수 있습니다.
