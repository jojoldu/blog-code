# 스프링부트의 깨알같은 팁 시리즈
1. [Intro (소개)](#intro)
2. [Banner](#banner)
3. [ViewResolver](#viewresolver)
4. [Actuator](#actuator)


## Intro (소개)
![SpringFramework VS SpringBoot](./images/springframework-vs-springboot.png)

 * 기존에 SpringFramework가 기본적으로 해야할 셋팅이 너무 많고 어려웠던점을 해소하기 위해 나옴
   - RoR (Ruby On Rails)나 Express (Nodejs 웹프레임워크)를 사용해보니 그 차이가 더 심하게 느껴짐 (SpringFramework 으로 하루가 걸릴 CRUD 게시판이 express로 1~2시간만에 작성되니 멘붕)
   - 이런 이유로 이전까지 직접 하던 설정들 중 변경요소가 크지 않는 **많은 설정들을 기본적으로 지원**하도록 나온것이 Spring Boot이다. (흔히 이런 변경요소 없이 계속 사용되는 코드를 **보일러플레이트**라고 한다. 스크립트 언어쪽은 이런쪽이 엄청 잘되어 있다.)
   - 예를 들어, 템플릿엔진으로 Freemarker를 사용해야한다면 기존엔 ViewResolver를 Bean으로 등록하고, prefix, suffix를 설정해야한다. 하지만 boot의 경우 해당하는 의존성을 추가만 하면 별도로 설정이 필요 없다.
 * 구 Spring프로젝트(2.x나 3.0)를 하다가 바로 Spring Boot로 넘어올 경우 오해하는것 중 하나가 Java 코드로 설정하는 것이다.
   - Java 코드로 설정하는 것은 **Boot에서만 되는것이 아니다.** SpringFramework 에서도 된다.
   - 다만 Boot는 Java code로 설정하는것조차 더욱 간단하게 application.properties/application.yml로 관리할수 있게 해준다.
   - [링크](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)를 가보면 application.properties/application.yml로 어떤것들을 설정할수있는지 나오는데, 보면 Tomcat port부터 시작해서 웹 어플리케이션에 관련된 설정 전부를 다루고 있음을 확인할수 있다. 
 * 간편한 설정외에도 부트의 최고장점으로 꼽는것이 단독 어플리케이션만으로 배포가 되도록 하는것이다.
   - 이전까지만 하더라도 서버가 새로 구축되면 해당 서버에 기존 프로젝트와 동일한 버전의 Tomcat을 설치하고, Tomcat의 여러 설정 xml값을 수정하고, maven/gradle같은 build 툴을 설치하는 등 서버 하나 확장하는것이 일이였다.
   - 하지만 Boot의 경우 JDK 설치하고, java -jar ~~~.jar로 실행시키면 웹서버 구축 끝이다. 이 얼마나 간단한가!

 
## Banner
![SpringBoot의 Banner](./images/banner.png)

 * Boot 실행시 등장하는 아스키 배너는 다른 배너로 수정이 가능하다.
 * [이미지 to 아스키](http://picascii.com/) 에서 원하는 이미지를 아스키코드로 convert 한다.
 * src/main/resources에 추출한 아스키 코드를 banner.txt 파일에 복붙한다.
 * application.properties 혹은 application.yml에 banner.location=banner.txt로 등록하면 끝
 * 소속 팀장님 사진으로 등록하면 고과+@, 설현 사진으로 하면 코딩력+@
 
 ![자바지기님](./images/javajigi-banner.png)
 
 (열심히 수강중^^)
 
## ViewResolver
 * JSP를 제외한 다른 템플릿 엔진의 경우 의존성만 추가하면 추가설정없이 바로 사용이 가능하다
   - 사용되는 의존성들은 spring-boot-starter-xxxx 의 이름을 가진다
   - 예를들어 본인이 freemarker를 사용한다면 spring-boot-starter-freemarker, thymeleaf를 사용한다면 spring-boot-starter-tyhmeleaf를 추가하면 된다.
   - 디폴트 설정은 prefix는 src/main/resources/templates 이며, suffix는 해당하는 템플릿의 확장자명이 된다.
 
 * freemarker를 예로 들면, 먼저 spring-boot-starter-freemarker 의존성을 추가한다.
  
  ![freemarker의존성](./images/freemarker.png) 
  
 * src/main/resources/templates에 index.ftl을 만든다.
  
    ![freemarker의존성](./images/index.png)
    
 * Controller에 return "index" 코드를 작성하고 확인
  
    ![freemarker의존성](./images/index-controller.png)
 
 * 개발자가 집중해야하는 코드에만 집중할 수 있도록 Spring Boot가 얼마나 강력하게 지원해주는지 확인할 수 있다.
 * **JSP는 이제 하지 말자**는 Spring 진영의 강력한 의지도 덤으로 볼 수 있다.
  
## Actuator
 * SpringBoot의 가장 강력한 기능 중 하나
 * SpringBoot 어플리케이션의 상태를 실시간으로 확인할 수 있다. 
   - 현재 Context에 등록된 Bean 확인, 사용중인 Heap 사이즈, 최근 요청한 HTTP 상태 등등 관련한 모든 정보를 볼 수 있다.

 ![액추에이터 Path들](./images/actuator-path.png)
 
 * 특히나 [헤로쿠](http://jojoldu.tistory.com/18)와 같은 PaaS 환경에서는 정말 유용하게 사용됨
 * spring-boot-starter-actuator 의존성을 pom.xml이나 build.gradle에 추가 후, 어플리케이션을 재시작하면 아래와 같은 로그를 확인할 수 있다.
 ![Actuator 실행로그](./images/actuator-log.png)
 * 사용법 및 예제 작성중...
