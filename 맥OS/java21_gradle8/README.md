# Java 21 & Gradle 8 Mac에 설치하기

Java 21이 2023년 9월에 출시 된지 1년이 되어가고 있고, [여러 신규 기능](https://www.infoworld.com/article/3689880/jdk-21-the-new-features-in-java-21.html)이 패치 되었기도 하여서 개인 노트북에 JDK 21 설치를 하기로 했다.  
      
[whichjdk.com](https://whichjdk.com/ko/)를 보면 크게 2가지 버전의 JDK를 추천한다.

- Amazon Corretto
  - AWS의 Amazon Linux 2에서 Java 애플리케이션을 직접 실행하는 경우 최적화된 버전
- Adoptium Eclipse Temurin
  - 오픈 소스 소프트웨어에 대한 리소스와 전문 거버넌스 모델을 제공하는 Eclipse 재단 산하의 최상위 프로젝트 
  - Red Hat, IBM, Microsoft, Azul, iJUG 등 Java 기술에 전략적 관심을 갖고 있는 주요 기업 및 조직으로 구성
  - 이전의 AdoptOpenJDK 프로젝트는 Eclipse Adoptium으로 이전됨

현재 여러 팀에서도 대부분 이 중 하나로 선택해서 사용중이다.  

- [(페이스북) 다들 개발팀 로컬 PC는 JDK 어느것들을 사용하시나요?](https://www.facebook.com/jojoldu/posts/pfbid0woHLzFSoaYwom5HNEfSYFtDsythkiQdkqakKpxJLC92w1dBn5dQyDpZPVJgk9uUMl?notif_id=1718758810997688&notif_t=feedback_reaction_generic&ref=notif)
- [(트위터) 다들 개발팀 로컬 PC는 JDK 어느것들을 사용하시나요?](https://twitter.com/jojoldu/status/1803228699225104577)

온프레미스 기반의 회사에서는 당연하게도 Temurin을 사용중이고, AWS를 사용중인 팀에서도 요즘 같이 컨테이너 환경에서는 특정 클라우드 벤더사의 JDK만 써야하는 제약이 있지 않다보니 Temurin을 사용하는 경우가 많다.  
  
이 글에서도 속한 회사의 인프라 환경에 크게 제약을 받지 않는 오픈소스인 Temurin 21 로 설치를 진행한다.

### Adoptium Eclipse Temurin 21 설치

(설치 안되어있다면) `cask` 를 설치하고 

```bash
brew install cask
```

**Temurin 21 설치**한다.

```bash
brew install --cask temurin@21
```

설치가 완료되면 잘 설치되어있는지 확인해본다.

```bash
$ java --version
openjdk 21.0.3 2024-04-16 LTS
OpenJDK Runtime Environment Temurin-21.0.3+9 (build 21.0.3+9-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.3+9 (build 21.0.3+9-LTS, mixed mode)
```

### jenv 설치

정상혁님의 ["여러 개의 JDK를 설치하고 선택해서 사용하기"](https://blog.benelog.net/installing-jdk)을 보면 여러 JDK 버전 관리 도구들에 대한 소개가 나온다.  
  
SDKMAN 의 경우 JDK 설치까지 편하게 사용 가능하지만, 버전 변경에 대해 불편한 점이 많다.  
반면 **jenv는 JDK 설치는 수동으로 진행해야하지만, 그 이후 버전 관리에 대해서는 다양하고 편리하게** 사용할 수 있어 여기서는 [jenv](https://www.jenv.be/) 로 진행한다.  

```bash
$ brew install jenv
```

설치된 jenv 를 등록하기 위해  `~/.bashrc` 또는 `~/.bash_profile` 혹은 `~/.zsh`에 아래 내용을 추가한다.

```bash
export PATH="$HOME/.jenv/bin:$PATH"
eval "$(jenv init -)"
```

아래와 같이 직접 명령어를 수행해서 추가해도 된다.

```bash
$ echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.zshrc
$ echo 'eval "$(jenv init -)"' >> ~/.zshrc
```

아래 명령어들도 차례로 수행한다.  
  
**Enable the export plugin**

```bash
$ eval "$(jenv init -)"
$ jenv enable-plugin export
```

**Restart your shell**

```bash
$ exec $SHELL -l
```

jenv 설정이 완료되었다면, 위에서 설치한 temurin21 JDK를 jenv에 등록한다.

```bash
$ jenv add /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
```

jenv에 잘 등록되었는지 확인해본다.

```bash
$ jenv versions
* system (set by /Users/jojoldu/.jenv/version)
  21
  21.0
  21.0.3
  temurin64-21.0.3
```

JAVA_HOME에도 jenv 로 설정된 버전을 인식할 수 있도록 아래 명령어로 글로벌 JDK 버전을 변경한다.  

```bash
$ jenv global 21.0.3
```

(이걸 하지 않으면 Gradle 등에서 JAVA_HOME 인식을 하지 못한다.)

## Gradle 8 설치

sdkman은 JDK 외에도 여러 JVM 진영의 도구들을 설치, 관리하기 편리한 도구이다.  
그래서 Gradle 은 [sdkman](https://sdkman.io/install)을 통해 진행한다.  

먼저 sdkman 을 설치한다.

```bash
$ curl -s "https://get.sdkman.io" | bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh"
```

설치된 sdkman 을 통해 Gradle 최신 버전을 설치한다.

```bash
$ sdk install gradle 8.8
```

잘 설치되었는지 아래 명령어로 gradle 버전을 확인해본다.

```bash
$ gradle -v
------------------------------------------------------------
Gradle 8.8
------------------------------------------------------------

Build time:   2024-05-31 21:46:56 UTC
Revision:     4bd1b3d3fc3f31db5a26eecb416a165b8cc36082

Kotlin:       1.9.22
Groovy:       3.0.21
Ant:          Apache Ant(TM) version 1.10.13 compiled on January 4 2023
JVM:          21.0.3 (Eclipse Adoptium 21.0.3+9-LTS)
OS:           Mac OS X 14.1.2 aarch64
```
