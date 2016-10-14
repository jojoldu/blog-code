Gradle task 적응하기
====================

maven이나 gradle와 같은 build 툴중 하나는 어느정도 알아야겠다는 생각에 이것저것 불편했던 사항들을 task로 해결하려고 한다.

<!--toc -->

-	[Gradle task 적응하기](#gradle-task-적응하기)
	-	[Build 후 SCP로 원격서버에 빌드파일 전송하기](#build-후-scp로-원격서버에-빌드파일-전송하기) - [프로젝트 생성](#프로젝트-생성) - [플러그인 추가](#플러그인-추가)

<!-- tocstop -->

Build 후 SCP로 원격서버에 빌드파일 전송하기
-------------------------------------------

CI (젠킨스 or 허드슨) 미도입 + 망분리 상황에서는 배포가 여간 귀찮은게 아니다.<br/> 특히 망 분리로 인해서 배포서버로 다이렉트로 접근할 수가 없어 인터넷 PC - > 중간 서버 -> 내부망 PC -> 배포 서버 과정을 거쳐 war/jar를 전송해야만 한다.

![충격과 공포의 배포 프로세스](./images/충격과공포다.png)

(으아아아앙ㅠㅠㅠ)

보안 때문에 어쩔수 없긴 한데 QA도중 수정사항을 즉각 반영하는게 잦다보니 이 과정중 하나라도 좀 줄여보자고 생각해서 gradle task를 작성하게 되었다. <br/> 진행할 내용은 **IDE에서 build 진행이 완료되면 지정한 원격 서버로 war/jar를 전송** 하는 것이다. <br/> 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/gradle-task)에 전부 있으니 같이 보면 좋을것 같다. 그럼 시작하겠다.

#### 프로젝트 생성

먼저 gradle을 설치하자. 이건 별다를게 없는데 그래도 검색이 귀찮다면 [여기](http://enterkey.tistory.com/351)를 참고! 프로젝트는 IntelliJ + SpringBoot 기반으로 진행할 예정이다.<br/> SpringBoot로 프로젝트 생성하는 것은 인터넷에 워낙 많으니 각자 본인의 IDE에 맞게 프로젝트 생성을 하자.<br/> 프로젝트 생성후 build.gradle을 열어보면 휑한 화면을 볼 수 있다.

![build.gradle](./images/default.png)

우린 여기서부터 하나씩 진행할 것이다. <br/>

#### 플러그인 추가

Gradle의 경우 ssh 사용을 도와주는 유용한 플러그인으로 [gradle-ssh-plugin](https://gradle-ssh-plugin.github.io/) 이 있다. 특히 scp의 경우 함수만 호출하면 될 정도로 아주 쉬운 형태로 지원해주기 때문에 한번 써보면 여러모로 쓸곳이 많음을 느낄 수 있을것 같다. <br/> 여튼 이 플러그인을 사용할 수 있도록 build.gradle을 수정해보겠다.

```
buildscript {
	ext {
		springBootVersion = '1.4.1.RELEASE'
    gradleSshVersion = '2.2.0' //아래 gradle-ssh-plugin에서 사용할 버전 명시
	}
	repositories {
		jcenter()
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    classpath("org.hidetake:gradle-ssh-plugin:${gradleSshVersion}") //gradle 플러그인 의존성 추가
	}
}

//플러그인 적용
apply plugin: 'org.hidetake.ssh'
```

위 코드를 추가하고 gradle 새로고침을 해보자
