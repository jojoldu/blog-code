# AWS S3를 이용한 Jenkins 마이그레이션

기존 Jenkins 환경을 다른 서버로 마이그레이션을 해야할 때가 있습니다.  

> 일반적으로는 잘 없지만, 계열사 혹은 서비스 분리로 AWS 환경 자체가 변경될 경우가 있습니다.

그럴때를 대비해서 EC2에 설치된 Jenkins를 다른 EC2로 마이그레이션 하는 방법을 진행해보겠습니다.  
  
전체 구조는 아래와 같습니다.

![intro](./images/intro.png)

Jenkins는 모든 설정과 Job 내용이 **파일**로 관리가 됩니다.  
그래서 Jenkins가 설치된 디렉토리 전체를 타 서버로 이관만 하면 마이그레이션은 끝입니다.  

그럼 하나씩 진행해보겠습니다.

## 1. 기존 서버

먼저 기존에 운영되던 Jenkins EC2 서버에서 root 계정으로 전환합니다.

```bash
sudo su - root
```

안전하게 이관하기 위해 실행중인 Jenkins를 정지합니다.

```bash
service jenkins stop
```

혹시나 압축하는 동안 신규 로그가 쌓일 수 있으니 정지하고 진행합니다.

> 물론 무중단으로 백업하고 싶으시다면 디렉토리 복사한 뒤에 압축하여도 무방합니다.

Jenkins 설정이 있는 디렉토리 위치는 ```/var/lib/jenkins``` 입니다.  
아래 명령어로 해당 디렉토리를 ```tar```로 압축하겠습니다.

```bash
cd /var/lib
tar cvzf jenkins-`date +"%Y%m%d%H%M%S"`.tgz jenkins
```

압축이 다 되셨다면 ```jenkins-xxxxx.tgz``` 와 같이 압축시간이 포함된 tgz 파일이 생성 됩니다.  

![tar](./images/tar.png)

> 압축이 끝나면 다시 ```service jenkins start``` 로 실행하시면 됩니다.

해당 파일을 S3로 업로드 합니다.

```bash
aws s3 cp jenkins-20200707005336.tgz s3://버킷명/
```

지정된 버킷으로 잘 업로드 된 것을 확인합니다.

![s3-upload](./images/s3-upload.png)


## 2. 신규 서버

신규 서버에서도 Jenkins가 설치되어 있어야만 합니다.  
아직 Jenkins를 설치 안했다면, [이전 포스팅](https://jojoldu.tistory.com/441)을 참고하셔서 Jenkins를 설치합니다.  
  
설치가 다 되셨다면 신규 서버에서도 root 계정으로 전환후 작업을 시작하겠습니다.
  
```java
sudo su - root
```

기존 서버에서 올린 tar 파일을 S3에서 다운 받습니다.  

```bash
aws s3 cp s3://버킷명/jenkins-20200707005336.tar /var/lib/
```

다운로드가 끝나면, 방금 설치되어 기본 설정만 되어있는 Jenkins의 디렉토리를 옮겨놓습니다.

```bash
service jenkins stop
mv /var/lib/jenkins /var/lib/old-jenkins
```

> ```mv /var/lib/jenkins /var/lib/old-jenkins``` 를 하는 이유는, 혹시나 이관시 이슈가 발생할 경우 바로 원복하기 위함입니다.

디렉토리가 옮겨졌다면, tar 파일을 압축 해제하여 설정 디렉토리를 교체합니다.

```bash
cd /var/lib
tar xvzf ./jenkins-20200707005336.tar
```

압축이 풀리면 ```/var/lib/jenkins``` 디렉토리가 다시 생성되어있는 것을 확인할 수 있습니다.  
  
확인이 되셨다면 다시 Jenkins를 실행해보고 정상적으로 작동되는지 확인해봅니다.

```bash
service jenkins start
```


## 3. Github 로그인 인증키 교체

```bash
/var/lib/jenkins/config.xml
```