# Spock

안녕하세요? [저번시간](http://jojoldu.tistory.com/228)에 이어 Spring Boot & Spock 예제를 진행해보려고 합니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/spring-boot-spock)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>

## 2. SpringBoot + Spock

이제는 실제 SpringBoot 환경에서 Spock을 어떻게 사용할지에 대해 소개드리겠습니다.  
참고로 Spock은 모든 Spring 코드를 Java와 동일하게 사용할 수 있습니다.  
  
(참고로 ```@RunWith(SpringRunner.class)```는 Spock 테스트에서 사용하지 않습니다.)  

![SpringRunner](./images/SpringRunner.png)

보시는 것처럼 ```SpringRunner``` 클래스는 이전에 사용하던 ```SpringJUnit4ClassRunner```를 확장한 클래스이다보니 JUnit을 사용할때 필요하기 때문입니다.  
  


### 2-1. 기본 사용법

간단하게 Repository 기능을 테스트해보겠습니다.  

![springboot_기본테스트](./images/springboot_기본테스트.png)


### 2-2. SpringBatch

### 2-3. @MockBean, @SpyBean