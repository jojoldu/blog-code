# Mac에서  코틀린 개발환경 구성하기

간단하게 코틀린을 테스트해볼 일이 있어 개인 PC에 코틀린을 설치해봤습니다.  
모든 과정은 [Homebrew](https://www.44bits.io/ko/keyword/homebrew)를 통해 진행합니다.

## 1. JDK 구성

아직 코틀린은 JVM이 있어야 하기 때문에 JDK를 꼭 설치해야 합니다.  

> 물론 [kotlin native](https://kotlinlang.org/docs/native-overview.html) 로의 시도가 계속 되고 있습니다.
  
JDK가 미설치라면 다음과 같이 설치를 진행합니다.  
저는 OpenJDK 11을 설치했는데, 최근엔 17까지 나왔기 때문에 본인 환경에 맞게 설치하시면 됩니다.

```bash
brew install --cask adoptopenjdk11
```

설치가 끝나면 아래와 같이 버전을 확인할 수 있습니다.

```bash
java -version
```

![1](./images/1.png)

## 2. 코틀린 구성

Java 버전을 확인했다면 코틀린을 바로 설치합니다.

```bash
brew install kotlin
```

설치된 코틀린을 확인한다면 다음과 같이 할 수 있습니다.

```bash
kotlinc -version
```

![2](./images/2.png)

## 3. 테스트

설치된 코틀린을 통해 간단하게 코드를 작성해봅니다.  
코틀린은 콘솔에서 바로 실행해볼 수 있기 때문에 아래와 같이 테스트해봅니다.

```bash
kotlinc-jvm
```

![3](./images/3.png)


실습이 끝나시면 쉘에서 빠져나오시면 됩니다.

```bash
:quit
```