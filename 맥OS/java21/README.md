# Java 21 & Gradle 8 Mac에 설치하기

[whichjdk.com](https://whichjdk.com/ko/)

- Amazon Corretto
  - AWS의 Amazon Linux 2에서 Java 애플리케이션을 직접 실행하는 경우 최적화된 버전
- Adoptium Eclipse Temurin
  - Eclipse Adoptium은 오픈 소스 소프트웨어에 대한 리소스와 전문 거버넌스 모델을 제공하는 Eclipse 재단 산하의 최상위 프로젝트 
  - Adoptium 워킹 그룹은 Red Hat, IBM, Microsoft, Azul, iJUG 등 Java 기술에 전략적 관심을 갖고 있는 주요 기업 및 조직으로 구성
  - 이전의 AdoptOpenJDK 프로젝트는 Eclipse Adoptium으로 이전

## Java 21 설치

### Adoptium Eclipse Temurin 21 설치

```bash
brew install cask
```

**21 설치**

```bash
brew install --cask temurin@21
```

```bash
$ java --version
openjdk 21.0.3 2024-04-16 LTS
OpenJDK Runtime Environment Temurin-21.0.3+9 (build 21.0.3+9-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.3+9 (build 21.0.3+9-LTS, mixed mode)
```

### jenv 설치

- [](https://www.jenv.be/)

```bash
$ brew install jenv
```

그리고 `~/.bashrc` 또는 `~/.bash_profile` 최근 Mac을 사용한다면 `~/.zsh`에 아래 내용을 추가하자.

```bash
export PATH="$HOME/.jenv/bin:$PATH"
eval "$(jenv init -)"
```

```bash
$ echo 'export PATH="$HOME/.jenv/bin:$PATH"' >> ~/.zshrc
$ echo 'eval "$(jenv init -)"' >> ~/.zshrc
```

**Enable the export plugin**

```bash
$ eval "$(jenv init -)"
$ jenv enable-plugin export
```

**Restart your shell**

```bash
$ exec $SHELL -l
```

```bash
$ jenv add /Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home
```

```bash
$ jenv versions
* system (set by /Users/jojoldu/.jenv/version)
  21
  21.0
  21.0.3
  temurin64-21.0.3
```

```bash
$ jenv global 21.0.3
```

## Gradle 8 설치

### SDKMAN 설치

[](https://sdkman.io/install)

```bash
$ curl -s "https://get.sdkman.io" | bash
```

```bash
$ source "$HOME/.sdkman/bin/sdkman-init.sh"
```

```bash
$ sdk install gradle 8.8
```

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
