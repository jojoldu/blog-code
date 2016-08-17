# 스프링부트의 깨알같은 팁 시리즈

## Intro
![SpringFramework VS SpringBoot](./images/springframework-vs-springboot.png)

 - 기존에 SpringFramework가 기본적으로 해야할 셋팅이 너무 많고 어려웠던점을 해소하기 위해 나옴
 - ROR (Ruby On Rails)나 Express (Nodejs 웹프레임워크)를 사용해보니 그 차이가 더 심하게 느껴짐 (SpringFramework 으로 하루가 걸릴 CRUD 게시판이 express로 1~2시간만에 작성되니 멘붕)
 - 이런 이유로 이전까지 직접 하던 설정들 중 변경요소가 크지 않는 **웬만한 설정들을 기본적으로 지원**하도록 하였다. 
 - 예를 들어, 템플릿엔진으로 Freemarker를 사용해야한다면 기존엔 ViewResolver의 prefix, suffix를 설정해야한다. 하지만 boot의 경우 해당하는 의존성을 추가만 하면 별도로 설정이 필요 없다.
 - 구 Spring프로젝트를 하다가 바로 SpringBoot로 넘어올 경우 오해하는것 중 하나가 Java 코드로 설정하는 것이다.
 - Java 코드로 설정하는 것은 **boot에서만 되는것이 아니다.** SpringFramework 에서도 된다.
 - Boot는 Java code로 설정하는것조차 더욱 간단하게 application.properties/application.yml로 관리할수 있게 해준다. [참고](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)
 - 
 
## Banner
![SpringBoot의 Banner](./images/banner.png)
 - Boot 실행시 등장하는 아스키 배너는 다른 배너로 수정이 가능하다.
 - [이미지 to 아스키](http://picascii.com/) 에서 원하는 이미지를 아스키코드로 convert 한다.
 - src/main/resources에 추출한 아스키 코드를 banner.txt 파일에 복붙한다.
 - application.properties 혹은 application.yml에 banner.location=banner.txt로 등록하면 끝
 - 소속 팀장님 사진으로 등록하면 고과+@
 ![자바지기님](./images/javajigi-banner.png)
 (열심히 수강중^^)
 
## ViewResolver
 - spring-boot-starter-freemarker 의존성을 추가한다.
 - src/main/resources/templates 디렉토리가 prefix가 되고 .ftl이 자동으로 suffix로 설정되는것을 확인.
 
## Actuator
 - SpringBoot의 가장 강력한 기능중 하나
 - SpringBoot 어플리케이션의 상태를 
 - 일반적으로는 [Nagios](https://www.nagios.org/) 와 같은 시스템 모니터링 툴이 더 사용이되나, [헤로쿠](http://jojoldu.tistory.com/18)와 같은 PaaS 환경에서 유용하게 사용됨
 - spring-boot-starter-actuator 의존성을 pom.xml이나 build.gradle에 추가 후, 어플리케이션을 재시작하면 아래와 같은 로그를 확인할 수 있다.
 ![Actuator 실행로그](./images/actuator-log.png)
 - 