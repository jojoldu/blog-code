# my-junit

안녕하세요? 이번 시간엔 JUnit을 직접 만들어보는 시간을 가지려고 합니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/my-junit)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>
 

## 계기

긴 추석연휴 기간동안 미뤄둔 포스팅 예정 글들을 정리했습니다.  
3개를 연달아 처리하고 뭐가 더 남았나 에버노트를 보다가 아주 예전에 메모해놓은 일감이 있었습니다.  
바로 **나만의 XUnit 만들기**입니다.  
  
토비님께서 올리신 글을 보고 일감 등록을 했었던 기억이 떠올랐습니다.  
![토비님글](./images/토비님글.png)

(원분 : [페이스북링크](https://www.facebook.com/tobyilee/posts/10208774948625630))  
  
일단 회사에서 사용하는 기술들을 익히기에 급급해 계속 미루다가 이제야 다시 봤습니다.  
장기간 휴식이 또 언제 생길지 모르니 지금 당장 시작하기로 마음먹고 진행하게 되었습니다.  
(토비님의 블로그 글을 꼭 읽어보시면 큰 도움이 되실것 같습니다.)  
  
마침 [[번역]JUnit A Cook’s Tour](https://bluepoet.me/2016/12/03/%EB%B2%88%EC%97%ADjunit-a-cooks-tour/)도 있어 참고하며 진행할 예정입니다.

## 본문

빌드툴은 Gradle을 사용할 예정입니다.  
IDE는 IntelliJ 를 사용하는데, 스프링과 같은 웹 개발이 필요한 부분이 아니기 때문에 꼭 유료버전인 Ultimate이 아니더라도 무료 버전인 Community 버전을 사용하셔도 됩니다.  
(이클립스 쓰셔도 됩니다)  
  
build.gradle은 로그 관리를 위해 2개의 의존성을 먼저 추가하고 시작하겠습니다.  

build.gradle

```gradle
dependencies {
    compile group: 'org.slf4j', name: 'slf4j-api', version: '1.7.25' // log를 위해 slf4j 추가
    compile group: 'ch.qos.logback', name: 'logback-classic', version: '1.2.3' // log를 위해 logback 추가
}
```

혹시나 logback을 처음 접하신다면 [소내기님의 logback 튜토리얼](https://sonegy.wordpress.com/category/logback/)을 참고하시면 좋습니다.  
  
기본적인 디렉토리 구조는 다음과 같습니다.

![디렉토리구조](./images/디렉토리구조.png)

src/main/java,resources & src/test/java,resources이며 기본 패키지는 myjunit입니다.  
  
자 그럼 첫번째 테스트 코드를 작성해보겠습니다.

### 1. 첫 테스트 코드

간단하게 테스트 코드를 작성하겠습니다.  
src/test/java/myjunit에 ```TestCaseTest.java```를 생성하고 아래의 코드를 추가하겠습니다.

```java
public class TestCaseTest {

    public static void main(String[] args) {
        new TestCaseTest().runTest();
    }

    public void runTest () {
        long sum = 10+10;
        Assert.assertTrue(sum == 20);
    }
}

```

현재 JUnit이 의존성으로 등록되어있지 않기 때문에 Assert 클래스가 존재하지 않습니다.  
이 테스트 코드를 수행하기 위해 ```Assert``` 클래스와 static Method인 ```assertTrue```를 생성하겠습니다.  

```java
public class Assert {
    private static final Logger logger = LoggerFactory.getLogger(Assert.class);

    private Assert() {} // 인스턴스 생성을 막기 위해 기본생성자 private 선언

    public static void assertTrue(boolean condition) {
        if(!condition){
            throw new AssertionFailedError();
        }

        logger.info("Test Passed");
    }
}
```

slf4j logger가 의존성으로 등록되어있기 때문에 logger는 sfl4j logger를 사용합니다.  
 ```assertTrue```는 단순합니다.  
파라미터로 넘겨진 boolean 값이 ```true```이면 Test는 성공이며(```logger.info("Test Passed")```), ```false```하면 ```throw new AssertionFailedError()```를 발생하여 테스트 실패를 알립니다.  
 ```AssertionFailedError```도 존재하지 않는 클래스이니 추가 생성합니다.

```java
public class AssertionFailedError extends Error {
    public AssertionFailedError() {}
}
```

자 이렇게 구성하고 ```TestCaseTest```의 main 메소드를 다시 실행해보겠습니다.

![테스트성공1](./images/테스트성공1.png)

이렇게 첫번째 테스트가 성공되었음을 확인할 수 있습니다.

### 2. TestCase

현재 구조는 **테스트 프레임워크**라고 할수는 없습니다.  
결국 각각의 테스트 케이스 단위로 요청을 나눌수 있는 구조가 되어야 합니다.  
이런 의도에 가장 어울리는 것이 [커맨드패턴](http://javacan.tistory.com/entry/6)입니다.  
  
각각의 테스트 케이스를 Command로 보고, 이를 실행하는 것은 ```run```(보통은 ```execute```를 사용) 메소드가 담당하도록 합니다.  
  
이렇게 해서 만든 ```TestCase```의 코드는 아래와 같습니다.

```java
public abstract class TestCase {

    protected String testCaseName;

    public TestCase(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public abstract void run();
}
```

각각의 TestCase는 이름을 가져야 식별가능하기 때문에 생성자에서 이를 받도록 하였습니다.  
 ```TestCase```는 그 자체로 사용하기 보다는 이를 상속한 실제 테스트 케이스 클래스들을 사용할것이기 때문에 추상 클래스(```abstract class```)로 선언하였습니다.  
  
위 ```TestCase```클래스를 사용하여 ```TestCaseTest```코드를 수정하겠습니다.  

```java
public class TestCaseTest extends TestCase {

    public TestCaseTest(String testCaseName) {
        super(testCaseName);
    }

    @Override
    public void run() {
        try {
            Method method = this.getClass().getMethod(super.testCaseName, null);
            method.invoke(this, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void runTest() {
        long sum = 10+10;
        Assert.assertTrue(sum == 20);
    }

    public static void main(String[] args) {
        new TestCaseTest("runTest").run();
    }
}
```

> 왜 TestCase의 구현체인 TestCaseTest를 new로 인스턴스 생성해서 써야하는지 의문이실수 있습니다.  
**각각의 테스트가 독립적으로 실행**하기 위해 모든 테스트 케이스는 새로운 인스턴스에서 수행되도록 하였습니다.  
  
그리고 변경한 테스트 코드도 잘 성공되는지 테스트 해보겠습니다.  

![테스트성공2](./images/테스트성공2.png)

이제는 테스트 케이스 메소드들을 하나만 생성하는 것이 아니라, 여러개를 생성하고 실제 main 메소드에선 해당 메소드들의 이름만 추가하면 테스트를 실행할수 있게 되었습니다.  

실제로 ```TestCaseTest```에 테스트 메소드를 하나더 추가해보겠습니다.

```java
    ... 
    private static final Logger logger = LoggerFactory.getLogger(TestCaseTest.class);

    ... 

    @Override
    public void run() {
        try {
            logger.info(super.testCaseName+ " execute "); // 테스트 케이스들 구별을 위해 name 출력 코드
            Method method = this.getClass().getMethod(super.testCaseName, null);
            method.invoke(this, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    ...
    
    public void runTestMinus() {
        long minus = 100-10;
        Assert.assertTrue(minus == 90);
    }

    public static void main(String[] args) {
        new TestCaseTest("runTest").run();
        new TestCaseTest("runTestMinus").run();
    }
```

 ```run``` 메소드에 실행한 테스트 메소드를 확인하기 위해 메소드명을 출력하도록 ```logger```를 추가하였습니다.  
 ```runTestMinus```를 추가하여 main 메소드에서 2개의 테스트 케이스를 실행하는데 파라미터만 변경하면 실행하는것을 확인할 수 있습니다.  

이 코드를 실제로 수행해보시면!

![테스트성공3](./images/테스트성공3.png)

테스트가 잘 성공됨을 알 수 있습니다.  
이 코드를 보시면서 뭔가 리팩토링 할만한게 보이시지 않나요?  
  
오버라이딩 하고 있는 ```run``` 메소드는 ```TestCase``` 클래스를 상속하는 모든 하위 클래스들이 다시 구현해야하는데, 실제로 하는 일은 공통으로 뽑아도 무방한 일입니다.  
이런 코드는 부모인 ```TestCase```가 가지는게 좀 더 좋아보입니다.  
그래서 ```run``` 메소드를 ```TestCase```로 이동시키겠습니다.

```java
public abstract class TestCase {
    
    private static final Logger logger = LoggerFactory.getLogger(TestCase.class);

    protected String testCaseName;

    public TestCase(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public void run() {
        try {
            logger.info(testCaseName+ " execute "); // 테스트 케이스들 구별을 위해 name 출력 코드
            Method method = this.getClass().getMethod(testCaseName, null);
            method.invoke(this, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

그리고 코드를 리팩토링했으니 다시한번 기존 테스트를 실행해보겠습니다.

![테스트성공4](./images/테스트성공4.png)

 ```TestCaseTest``` **코드가 대폭 다이어트 되면서도 테스트 코드는 여전히 잘 수행**되었습니다.  
리팩토링이 잘된거라 볼수있겠죠?  
  
현재까지 상황을 다이어그램으로 표기하면 아래와 같습니다.

![스냅샷1](./images/스냅샷1.png)

### 3. TestCase

## 참고

### 계기

* [테스팅 프레임워크는 직접 만들어 써보자 - 토비님 블로그](http://toby.epril.com/?p=424)
* [[번역]JUnit A Cook’s Tour](https://bluepoet.me/2016/12/03/%EB%B2%88%EC%97%ADjunit-a-cooks-tour/)

### 사용한 패턴 설명

* [Command 패턴](http://javacan.tistory.com/entry/6)
* [Collecting Parameter 패턴](http://www.javajigi.net/display/SWD/Move+Accumulation+to+Collecting+Parameter)
* [Composite 패턴](https://ko.wikipedia.org/wiki/%EC%BB%B4%ED%8F%AC%EC%A7%80%ED%8A%B8_%ED%8C%A8%ED%84%B4)
* [Template Method 패턴](http://jdm.kr/blog/116)