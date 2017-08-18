# Spring OAuth + Spring Session시 HTTP URL must not be null 발생시 원인 및 해결

안녕하세요? 이번 시간엔 Spring OAuth + Spring Session시 HTTP URL must not be null 발생시 원인 및 해결방안을 소개드디려 합니다.   
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/spring-yml-import)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>


## 문제 상황

Spring Session과 Spring OAuth2를 조합해서 로그인 시스템을 구축한다고 가정해보겠습니다.  

### Spring OAuth2 적용

가장 먼저 Spring OAuth2를 프로젝트에 적용시켜보겠습니다.  
  
**build.gradle**  

```
buildscript {
	ext {
		springBootVersion = '1.5.6.RELEASE'
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
apply plugin: 'org.springframework.boot'

version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.8

repositories {
	mavenCentral()
}


dependencies {
	compile('org.springframework.boot:spring-boot-starter-web')
	compile('org.springframework.security.oauth:spring-security-oauth2')
	compile
	compileOnly('org.projectlombok:lombok')

	runtime('com.h2database:h2')
	runtime('org.springframework.boot:spring-boot-devtools')

	testCompile('org.springframework.boot:spring-boot-starter-test')
}

```

application.yml

```yml
spring:
  datasource:
    platform: h2
  h2:
    console:
      enabled: true
      path: /h2-console # h2 db 웹 클라이언트 접속 url
  devtools:
    livereload:
      enabled: true # 정적파일들의 실시간 갱신
  jpa:
    hibernate:
      ddl-auto: create-drop


security:
  basic:
    enabled: false # security 기본 인증 옵션 제거
logging:
  level:
    org.hibernate.type: trace  # JPA로 생성되는 쿼리의 파라미터 값 확인

```

보안상 이슈가 될 수 있는 OAuth2 정보는 별도로 google.yml로 분리하겠습니다.  
  
**google.yml**

```yml
google:
  client:
    clientId: //각자의 정보
    clientSecret: //각자의 정보
    accessTokenUri: https://accounts.google.com/o/oauth2/token
    userAuthorizationUri: https://accounts.google.com/o/oauth2/auth
    scope: email, profile

  resource:
    userInfoUri: https://www.googleapis.com/oauth2/v2/userinfo
```

자 이런 설정파일들을 OAuth2와 함께 적용해보겠습니다.  
  
OAuth2.java

```java
@Configuration
@EnableOAuth2Client
public class OAuthConfig {

    private OAuth2ClientContext oauth2ClientContext;
    private BeanFactory beanFactory;

    public OAuthConfig(OAuth2ClientContext oauth2ClientContext, BeanFactory beanFactory) {
        this.oauth2ClientContext = oauth2ClientContext;
        this.beanFactory = beanFactory;
    }

    @Bean
    public Filter ssoFilter() {
        OAuth2ClientAuthenticationProcessingFilter oauth2Filter = new OAuth2ClientAuthenticationProcessingFilter("/login");
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(googleClient(), oauth2ClientContext);
        oauth2Filter.setRestTemplate(oAuth2RestTemplate);
        oauth2Filter.setTokenServices(new UserInfoTokenServices(googleResource().getUserInfoUri(), googleClient().getClientId()));
        oauth2Filter.setAuthenticationSuccessHandler(successHandler()); // 인증 성공시 진행될 Handler 등록
        return oauth2Filter;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler(){
        return (request, response, authentication) -> {
            System.out.println("성공!");
            response.sendRedirect("/");
        };
    }

    @Bean
    @ConfigurationProperties("google.client")
    public OAuth2ProtectedResourceDetails googleClient() {
        System.out.println("google client Call");
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
        System.out.println("google resource Call");
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    // google.yml을 import
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {

        System.out.println("google yml Call");

        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("google.yml"));

        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        return propertySourcesPlaceholderConfigurer;
    }
}
```

이렇게 간단하게 설정후, localhost:8080/login으로 접속해보시면 아주 잘 되는 것을 확인할 수 있습니다.

### Spring Session 적용

Spring OAuth2가 아주 잘 적용되었으니, 곧바로 SpringSession 을 적용해보겠습니다.  
build.gradle에 아래 2가지 의존성을 추가합니다.

```
compile('org.springframework.session:spring-session')
compile('org.springframework.boot:spring-boot-starter-jdbc')
```

그리고 JdbcSession을 활성화 하기 위해 HttpSessionConfig.java를 생성하여 아래와 같이 코드를 추가합니다.

```java
@EnableJdbcHttpSession
public class HttpSessionConfig {
}
```

추가로 어려운 코드가 없기에 

![응?](./images/응.png)

## Trace

그럼 여기서부터 제가 왜 일주일 동안 삽질했는지를 소개드리겠습니다.  

### 가정1. SSO Filter 문제?

### 가정2. 

## 해결책


### 참고

[Github Issue - Third Party PropertySourcesPlaceholderConfigurer](https://github.com/spring-projects/spring-boot/issues/6457)