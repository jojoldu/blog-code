# EC2 (Amazon Linux 1) 에 젠킨스 설치하기

## Jenkins 설치

![1](./images/1.png)

```bash
cat /etc/*release
```

![2](./images/2.png)

```bash
sudo yum update
```

![3](./images/3.png)

```bash
sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
```

```bash
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
```

### Exception

만약 아래와 같이 ```java.lang.UnsupportedClassVersionError```이 발생한다면 현재 젠킨스 버전과 설치된 자바 버전간 호환이 되지 않는다는 이야기입니다.

![5](./images/5.png)

아래 링크를 참고하여 Java 8 버전을 설치합니다.

* [자바8 설치](https://jojoldu.tistory.com/261)

## Nginx 설치

```bash
sudo yum install nginx
```

```bash
sudo vim /etc/nginx/nginx.conf
```

![6](./images/6.png)

```bash
proxy_pass http://localhost:8080;
proxy_set_header X-Real-IP $remote_addr;
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
proxy_set_header Host $http_host;
```

```bash
sudo service nginx start
```

ec2 보안그룹

![7](./images/7.png)


## 젠킨스 기본 설정

```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

![10](./images/10.png)

![11](./images/11.png)

![12](./images/12.png)

## 확장

* [Github 로그인](https://jojoldu.tistory.com/310)
* [머티리얼 테마 적용하기](https://jojoldu.tistory.com/343)


