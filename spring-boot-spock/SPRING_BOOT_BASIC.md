# SpringBoot + Spock

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