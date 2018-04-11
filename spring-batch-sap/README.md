# SAP 연동 시스템 개편기

안녕하세요?  
작년에 [회사 블로그 글](http://woowabros.github.io/tools/2017/07/10/java-enum-uses.html)을 쓰고나서 "좀 더 잘 쓸수있었을텐데.." 라는 아쉬움이 있었습니다.  
그래서 올해 블로그를 작성할때는 좀 더 욕심이 있었습니다.

![에피타이저](./images/에피타이저.png)

(하지만 올해도 에피타이저였습니다...)  
  
다시 내년을 기약하며!  
글을 시작하겠습니다.  
  
이번 편에서 다룰 주제는 **Spring Batch 사용 경험**입니다.  
  
작년 하반기에 SAP 연동 시스템 개편을 진행했습니다.  
기존 DB to DB 방식에서 API통신으로 개편하는 과정이였는데요.  
기존보다 훨씬 더 다양한 데이터를 다루기 위해 통신 프로토콜 외에 **인터페이스 스펙이 전부 변경**되는 큰 작업이였습니다.  
  
신규 연동 시스템 구축을 진행하면서 Spring Batch로 어떻게 문제들을 해결했는지를 소개드리겠습니다.  

> 회사 코드를 공개할 수 없어서 간소화한 샘플 코드임을 먼저 말씀드립니다.  

## 들어가며


Spring Batch는 다음과 같은 구조를 가지고 있습니다.

![아키텍처](./images/아키텍처.png)







## 1. Enum으로 다양한 API 스펙 대응하기

처음 신규 연동 스펙을 받았을때 **16개의 Batch**가 필요하다고 생각했습니다.  

![flow](./images/flow.png)

### 1-1. 공통 필드 동적으로 사용하기

### 1-2. Reader & Processor 동적으로 사용하기

### 1-3. Writer 동적으로 사용하기

## 2. Custom Batch 모듈 사용하기

### 2-1. Querydsl

### 2-2. JpaItemListWriter

## 3. Mockito로 테스트 코드 간소화하기



### 3-1. Reader Mocking

### 3-2. API 통신 Mocking

> 여담이지만, 웹 어플리케이션을 개발할때보다 **Spring Batch를 사용할때가 테스트 코드 연습 하기가 더 좋았습니다**.  
웹 어플리케이션의 경우 UI Layer에서의 테스트까지 있어야만 완전한 검증이 됐다고 할 수 있는 반면에, Spring Batch의 경우 UI Layer가 없기 때문에 순전히 결과 데이터만 검증하면 충분했습니다.  
컴포넌트 역시 Reader / Processor / Writer로 명확히 역할이 분리되어있어 Mocking 대상도 뚜렷하게 정할수 있기 때문입니다.

## 4. 젠킨스로 Batch 관리하기

Spring Batch의 스케줄링을 관리하는 방법은 크게 3가지가 있습니다.  

* [Cron](https://zetawiki.com/wiki/%EB%A6%AC%EB%88%85%EC%8A%A4_%EB%B0%98%EB%B3%B5_%EC%98%88%EC%95%BD%EC%9E%91%EC%97%85_cron,_crond,_crontab)으로 스케줄링하기
* [Quartz](http://www.quartz-scheduler.org/)로 Admin 페이지 만들어 사용하기
* [젠킨스](https://jenkins.io/) Job으로 사용하기


> Quartz와 Spring Batch는 그 용도가 서로 다릅니다.  
인터넷 검색을 하다보면 Quartz가 Spring Batch보다 낫다 아니다를 이야기하는 글을 보게 되는데요.  
바다코끼리와 코끼리처럼 **둘은 완전히 목적이 다른 모듈**들입니다.  
Quartz는 cron과 마찬가지로 스케줄러의 역할을 합니다.  
일괄처리 작업을 하는 Batch 모듈이 아니라는 점을 다시한번 말씀드립니다.


## 5. 마무리

