# AWS S3를 이용한 Jenkins 마이그레이션

```bash
sudo su - root
```

기존 젠킨스 서버

```bash
service jenkins stop
```

> 혹시나 압축하는 동안 신규 로그가 쌓일 수 있으니 정지하고 진행한다.
> cp로 디렉토리 복사한 뒤에 압축하여도 무방하다.

```bash
cd /var/lib
tar cvzf jenkins-`date +"%Y%m%d%H%M%S"`.tgz jenkins
```

```bash
aws s3 cp jenkins-20200707005336.tgz s3://beta-settler-attach/
```

```bash
upload: ./jenkins-20200707005336.tgz to s3://beta-settler-attach/jenkins-20200707005336.tgz
```


```bash
aws s3 cp s3://dev-settler-attach/jenkins-20200707005336.tar /var/lib/
```

```bash
cd /var/lib
mv /var/lib/jenkins /var/lib/old-jenkins
tar xvzf ./jenkins-20200707005336.tar
```


```bash
service jenkins stop
vim /var/lib/jenkins/config.xml
```


```bash
service jenkins start
```
