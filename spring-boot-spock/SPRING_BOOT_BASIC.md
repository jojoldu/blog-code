# Spock

안녕하세요? [저번시간](http://jojoldu.tistory.com/228)에 이어 Spring Boot & Spock 예제를 진행해보려고 합니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/spring-boot-spock)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>

## 2. SpringBoot + Spock

실제 SpringBoot 환경에서 Spock을 어떻게 사용할지에 대해 소개드리겠습니다.  
Spock은 모든 Spring 코드를 JUnit과 동일하게 사용할 수 있습니다.  
기본적인 사용법부터 시작해보겠습니다!

### 2-1. 기본 사용법

스프링 컨텍스트를 호출하여 검증하는 간단한 테스트 코드를 작성해보겠습니다.

![springboot_기본테스트](./images/springboot_기본테스트.png)

 ```Bean```으로 등록된 ```CustomerRepository```을 호출하여 ```findAll()``` 메소드를 검증하는 코드입니다.  
Spock을 처음 접하셔도 전혀 위화감이 없는 코드이지 않나요?  
기존 JUnit과 크게 사용법이 다르지 않습니다.  
그만큼 Spock으로 전환하기가 쉽다고 생각합니다.  
  
앞서 1부에서 소개한 기능 외에 새로운 코드가 보입니다.

* setup
  * JUnit의 ```@Before```와 같은 기능
  * 모든 테스트 메소드가 **실행되기 전에 각각 수행**된다. 
* cleanup
  * JUnit의 ```@After```와 같은 기능
  * 모든 테스트 메소드가 **실행된 후에 각각 수행**된다.

이외에도 ```setupSpec```, ```cleanupSpec```등이 더 있는데, 이런 메소드를 보고 Spock에선 [fixture 메소드](http://spockframework.org/spock/docs/1.1-SNAPSHOT/all_in_one.html#_fixture_methods) 라고 합니다.  
  
JUnit 코드와 다른 점이 한가지 더 있다면 ```SpringRunner.class```가 빠진것입니다.  
 ```@RunWith(SpringRunner.class)```는 Spock 테스트에서 사용하지 않습니다.  

![SpringRunner](./images/SpringRunner.png)

보시는 것처럼 ```SpringRunner``` 클래스는 이전에 사용하던 ```SpringJUnit4ClassRunner```를 확장한 클래스이다보니 JUnit을 사용할때 필요하며 Spock에선 사용되지 않습니다.  

### 2-2. SpringBatch

### 2-3. @MockBean, @SpyBean

