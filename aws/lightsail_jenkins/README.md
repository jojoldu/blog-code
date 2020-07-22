# Amazon Lightsail 시작하기

웹 서비스를 구성하는 모든 요소들이 항상 고사양의 서버를 필요로 하진 않습니다.  
이를 테면 관리자 어드민, Jenkins와 같은 설치형 CI/CD, Jira/Conflunce, 배치 애플리케이션 서버 등 트래픽에 영향을 안받지만 꼭 필요한 서비스들입니다.  

> 최근엔 많은 서비스들이 클라우드 버전도 함께 지원하곤 합니다.  
> Teamcity의 클라우드 버전, Jira/Conflunce의 클라우드 버전등이 있습니다.

이럴 경우 고사양의 EC2가 필요로 하지 않으며, 전문가 수준의 AWS 사용 방법도 필요로 하진 않습니다.  
  
Lightsail은 이렇게 작은 비용으로 시작할 수 있는 가상서버(VPS) 서비스인데요.  

> VPS란?  
> 하나의 물리 서버를 **여러 개의 가상 서버로 쪼개어 사용**하는 것을 의미합니다.  
> 그렇게 쪼개어진 가상 서버를 여러 명의 클라이언트가 나누어 쓰는 것이지요.  
> 따라서 하나의 물리서버를 다른 이들과 공유하는 개념이기는 하지만 각자 독립적인 서버 공간을 가지는 것이 가능합니다.  
> 다만, 하나의 물리서버에서 컴퓨팅 리소스들을 모든 사용자와 공유하기 때문에 만약 **한 사용자가 너무 많은 리소스를 점유**할 경우 RAM, CPU 등에 큰 영향이 간다는 단점이 있습니다.

기존 EC2에 비해 저렴한 비용과 웹 서비스에 필요한 **주요 기능들을 한 곳에서 쉽게 관리**할 수 있게 구성된 **입문자용 서비스**입니다.
  
입문자용이라 하더라도 로드밸런서/RDS/S3 등을 모두 사용할 수 있으니 웬만한 서비스 구축에는 큰 무리가 없습니다.  

![intro](./images/intro.png)


다만, Lightsail에는 아래와 같은 제한이 있는데요.

* 계정당 최대 20개의 인스턴스 유지
* 5개의 고정 IP (EIP) 
* 총 3개의 DNS 존 
* 총합 20TB의 블록스토리지(디스크) 연결
* 5개의 로드 밸런서
* 최대 20개 인증서

위 조건을 초과할 경우 일반적인 AWS 방식대로 EC2를 직접 생성하시고 진행하시면 될 것 같습니다.  
  
가격의 경우 nano (512MB) 사양에서는 예약 인스턴스 (월 ```$3```) 보다는 조금 더 비싸며 온디멘드 인스턴스 (월 ```$4.875```) 보다는 훨씬 저렴합니다.  

> small (2GB 메모리) 부터는 예약 인스턴스 (월 ```$11.25```)보다 저렴해집니다.  

![price](./images/price.png)

굉장히 작은 규모의 서비스인데, 매번 1년 선결제 하는게 부담되셨던 분들은 Lightsail은 좋은 선택지라고 생각됩니다.


## 1. Lightsail 인스턴스 생성하기


![aws1](./images/aws1.png)

![aws2](./images/aws2.png)

![aws3](./images/aws3.png)

![aws4](./images/aws4.png)

![aws5](./images/aws5.png)

![aws6](./images/aws6.png)

![aws7](./images/aws7.png)

![aws7-2](./images/aws7-2.png)

> Spring Boot, Jenkins 등을 실행시키길 원하시는 분들은 최소 메모리 1GB 이상을 선택하셔야 합니다.
> 첫달 무료인 nano 는 메모리가 512MB인데, 실제 Linux 시스템에 필요한 메모리를 제외하면 200~300MB만 사용할 수 있어 Spring Boot를 실행시킬 수가 없습니다. 

![aws8](./images/aws8.png)

![aws9](./images/aws9.png)

![aws10](./images/aws10.png)

![aws11](./images/aws11.png)

![aws12](./images/aws12.png)

![aws13](./images/aws13.png)

![aws14](./images/aws14.png)

![aws15](./images/aws15.png)

![aws16](./images/aws16.png)

![aws17](./images/aws17.png)


## 2. Lightsail 인스턴스 접속하기

```bash
ssh ec2-user@퍼블릭IP -i /Users/idong-uk/.ssh/jojoldu-lightsail.pem -o StrictHostKeyChecking=no
```

```bash
Warning: Permanently added 'XXX.XXX.XXX.XXX' (ECDSA) to the list of known hosts.
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@         WARNING: UNPROTECTED PRIVATE KEY FILE!          @
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
Permissions 0644 for '/Users/idong-uk/.ssh/jojoldu-lightsail.pem' are too open.
It is required that your private key files are NOT accessible by others.
This private key will be ignored.
Load key "/Users/idong-uk/.ssh/jojoldu-lightsail.pem": bad permissions
ec2-user@XXX.XXX.XXX.XXX: Permission denied (publickey).
```

```bash
chmod 600 라이트세일pem키
```


```bash
sudo yum update -y
```




 
## 3. 더이상 사용하지 않을 시 삭제

![aws-delete](./images/aws-delete-eip1.png)

![aws-delete](./images/aws-delete-eip2.png)

![aws-delete](./images/aws-delete.png)