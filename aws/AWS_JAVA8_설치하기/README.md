# AWS EC2(Amazon Linux)에 Java8 설치하기

현재(2018.01.14) EC2 (Amazon Linux)가 Java7이 기본 버전이라 Java8로 버전업 하는 방법을 소개드립니다.  
  
AWS EC2에 접속하셔서 아래 명령어를 실행 합니다.

```bash
sudo yum install -y java-1.8.0-openjdk-devel.x86_64
```

설치가 완료되셨으면 인스턴스의 Java 버전을 8로 변경하겠습니다.

```bash
sudo /usr/sbin/alternatives --config java
```

![1](./images/1.png)

버전이 변경되셨으면 사용하지 않는 Java7을 삭제합니다.

```bash
sudo yum remove java-1.7.0-openjdk
```

현재 버전이 Java8이 되었는지 확인합니다.

```bash
java -version
```
