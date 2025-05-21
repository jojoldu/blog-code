# 2. Github Action & AWS Beanstalk 배포하기 - profile=local로 배포하기

[지난 시간](https://jojoldu.tistory.com/543)에 만들어둔 Github Action을 통해 **profile=local**로 Beanstalk에 배포를 진행해보겠습니다.  
  
profile=local, 즉, **운영 DB와 구글&네이버 OAuth 를 사용하지 않는** 간단한 테스트 용도로만 배포할 예정입니다.  
  
실제 운영 배포는 다음 시간에 진행할 예정입니다.  
Github Action과 Beanstalk 연동된 환경 (즉, 이번 시간에 설정된 환경)를 구성하고 이를 기반으로 개선하는 과정으로 진행할 예정입니다.  
  
지난 시간과 마찬가지로 모든 애플리케이션 코드 (Java & Gradle)는 저의 저서 [스프링 부트와 AWS로 혼자 구현하는 웹 서비스](https://jojoldu.tistory.com/463)를 기반으로 합니다.

> **2020.12월 기준**이기 때문에 시간이 지나면 AWS의 UX 변경이 있을 수 있습니다.  
> 최대한 **키워드** 중심으로 진행할테니, 혹시나 시간이 흘러 보시는 분들은 이점 감안해주시면 됩니다.

## 1. AWS Beanstalk 생성하기

저번 시간에도 간략하게 소개 드렸지만, AWS Beanstalk은 AWS에 지원하는 PaaS (Platform as a Service) 입니다.  
  
기존에 EC2를 직접 구성하고, Code Deploy를 통해 배포환경을 구성하고, 로드밸런서를 연결하고, 오토스케일링 그룹을 직접 생성해서 연결하는 등의 모든 행위가 이 Beanstalk에서는 자동으로 이루어지는데요.  
  
이로인해서 개발자는 코드를 업로드하기만 하면 Elastic Beanstalk가 프로비저닝, 로드 밸런싱, Auto Scaling부터 시작하여 애플리케이션 상태 모니터링에 이르기까지 배포를 자동으로 처리합니다.  

![eb-intro](./images/eb-intro.png)

다만, 이렇게 자동으로 해주는 형태로 인해서 **요금** 걱정을 하실 수가 있는데요.  
Beanstalk는 추가 비용 없이 구성한 AWS 리소스에 대해서만 요금을 지불하면 됩니다.  

![eb-price](./images/eb-price.png)

([AWS 공식 문서](https://aws.amazon.com/ko/elasticbeanstalk/pricing/))  
  
이번에 저희가 사용할 AWS Beanstalk의 서비스는 2가지입니다.

* EC2
* LoadBalancer
  
아직 계정이 프리티어시라면 두 서비스 모두 월 750시간까지 무료이니 걱정 없이 실습을 진행하시면 됩니다.  
  
자 그럼 실제로 하나씩 구성해보겠습니다.

### 1-1. AWS Beanstalk 환경 생성하기

AWS 서비스 검색창에서 elasticbeanstalk을 검색합니다.

![eb1](./images/eb1.png)

대시보드에 보이는 **새 환경 생성** 버튼을 클릭합니다.

![eb2](./images/eb2.png)

환경에서는 **웹 서버 환경**을 선택합니다.

![eb3](./images/eb3.png)

일반적으로 Beasntalk의 환경은 위 화면처럼 2가지로 나뉘는데요.

* 웹 서버 환경
  * 기존에 EC2를 사용하던 방식과 크게 다르지 않습니다.
  * 일반적인 웹 서버를 할당 받을 때 사용하면 됩니다.
* 작업자 환경 (Worker environments)
  * 웹 서버 환경에 **메세지 큐 (SQS) 수신이 가능한 리스너 데몬**이 별도 구축된 환경 입니다.
  * 보통의 HTTP API 처리가 아닌 프로세스들(배치/메시지워커 등)을 위한 환경입니다.
  * [참고](https://docs.aws.amazon.com/ko_kr/elasticbeanstalk/latest/dg/using-features-managing-env-tiers.html)

여기서 저희는 일반적인 웹 서비스를 구축하니 **웹 서버 환경**을 선택하고 넘어갑니다.  
  
Beanstalk 생성을 위한 기본정보들을 차례로 등록합니다.

![eb4](./images/eb4.png)

보통은 애플리케이션 이름과 환경 이름을 별도로 구분하지만, 여기서는 하나로 통일해서 진행하겠습니다.  
(별도로 추가 환경 구성할 일이 없기 때문입니다.)  
  
마찬가지로 도메인명 역시 동일하게 맞추어 줍니다.

![eb5](./images/eb5.png)

플랫폼은 관리형 플랫폼을 선택하여 **Java 8** 을 선택합니다.

![eb6](./images/eb6.png)

[Corretto](https://aws.amazon.com/ko/corretto/) 란 AWS에서 지원하는 무료로 사용할 수 있는 Open Java Development Kit(OpenJDK)의 멀티플랫폼 배포판입니다.  
  
같은 JDK라면 Java 11을 선택하면 안되냐고 하실 수도 있는데요.  
  
이 시리즈의 애플리케이션 코드는 모두 **Java 8**을 기본으로 합니다.  
Java 11에서 잘 돌아가는지 검증이 안되어있기 때문에 선택하지 않습니다.

> Java 9부터 [Jigsaw](https://greatkim91.tistory.com/197)가 적용되어, 일반적으로 사용할 수 있던 여러 기본 jar 패키지들을 추가 옵션을 넣어야만 쓸 수 있도록 변경되었습니다.  

마지막은 절대 **환경 생성을 선택하시면 안됩니다**.  
**무중단 배포를 위한 설정** 작업이 추가로 필요합니다.  
그래서 화면 아래와 같이 **추가 옵션 구성**을 선택합니다.

![eb7](./images/eb7.png)

### 1-2. 추가 옵션 구성

Beanstalk의 추가 옵션을 차례로 설정합니다.  
  
먼저 최상단에 사전 설정으로 되어있는 부분을 **사용자 지정 구성**으로 변경합니다.  
  
![eb-config1](./images/eb-config1.png)

단일 인스턴스 옵션으로 설정할 경우 **로드 밸런서**를 설정할 수 없어 **Beanstalk으로 무중단 배포**를 할 수가 없습니다.  
그러니 꼭 사용자 지정 구성으로 진행합니다.  
  
이제 각 항목별로 설정을 해보겠습니다.

#### 인스턴스

![eb-config2](./images/eb-config2.png)

SSH 접속과 HTTP 접근이 가능하도록 보안그룹을 선택합니다.

> 책 p.239에서 설정한 보안그룹이며, 생성을 해본적이 없으신 분들은 EC2로 가서 보안그룹 생성후 진행하시면 됩니다.

![eb-config3_1](./images/eb-config3_1.png)

아래와 같이 보안그룹이 정상적으로 선택된 것을 확인하시면 됩니다.

![eb-config3_2](./images/eb-config3_2.png)

#### 용량

용량은 오토스케일링 그룹을 이야기합니다.  
즉, 이 Beanstalk으로 운영될 인스턴스의 수를 몇대로 할 것인지 정한다고 보시면 되는데요.  
  
![eb-config4](./images/eb-config4.png)

프리티어로 사용할 것이기 때문에 1대로 유지합니다.

![eb-config5](./images/eb-config5.png)

최대값을 1개 초과해서 적으시면 많은 트래픽이 들어올 경우 서버가 자동으로 증설되어 프리티어를 초과한 비용이 부담됩니다.  

> 현재 설정으로 하시면 많은 트래픽이 오면 서버가 죽는 한이 있어도, 증설되진 않습니다.

### 롤링 업데이트와 배포

![eb-config6](./images/eb-config6.png)

![eb-config7](./images/eb-config7.png)

#### 보안

![eb-config8](./images/eb-config8.png)

![eb-config9](./images/eb-config9.png)

만약 pem키를 한번도 생성해보신적 없다면 (즉, EC2 생성을 한번도 못해봤거나, key가 분실된 경우) [이 글](https://jojoldu.tistory.com/540)을 참고하여 수동으로 pem키를 생성하시면 됩니다. 

#### 로드 밸런서

마지막으로 로드밸런서까지 선택합니다.

![eb-config10](./images/eb-config10.png)

기존에 많이 사용한 로드밸런서는 Elastic Load Balancer (이하 ELB) 로 불리기도 했던 Class Load Balancer인데요.  
  
ELB의 경우 먼저 나온 로드밸런서다보니 이후에 나온 Application Load Balancer (이하 ALB) 에 비해 **성능과 기능**면에서 부족한점이 많습니다.

> 로드밸런서 라우팅 기능이 ELB는 거의 없다시피하며, ALB는 다양한 라우팅 기능을 지원합니다.

다만, 먼저 나왔다보니 그만큼 국내 자료가 많긴하지만, 최근 대부분의 서비스는 ALB를 선택하다보니 저희 역시 ALB를 선택하겠습니다.  

> 대규모 서비스를 운영하다보면 서버의 하드웨어 지표는 무리가 없는데 로드밸런서가 못버텨서 장애가 나기도 합니다.  
> 이를 대응하기 위해 ELB에서는 미리 충분한 대역폭을 늘려놓을 수 있도록 pre-warming 이라는 작업을 AWS측으로 요청할 수 있는데요.  
> 예정된 이벤트의 경우에는 이렇게 pre-warming이 가능한데, 갑작스런 트래픽에 관해서는 대응이 어려웠습니다.  
> 반면 ALB의 경우 이런 갑작스런 트래픽에 관해서도 대응이 되기 때문에 선택하지 않을 이유가 없습니다.

![eb-config11](./images/eb-config11.png)

### 1-3. 생성

1-2의 모든 설정들이 끝나면 환경 생성 버튼을 클릭합니다.  

애플리케이션 코드는 **샘플 애플리케이션**을 선택합니다.  
  
그럼 아래와 같이 생성 로그가 출력 됩니다.

![eb-start1](./images/eb-start1.png)

생성이 다 끝나시면 아래와 같이 애플리케이션과 환경을 볼 수 있습니다.

![eb-start2](./images/eb-start2.png)

참고로 위 생성 그림만 보면 Elastic Benastalk 의 구조를 환경 하위에 애플리케이션이 있다고 오해하실 수도 있으실텐데요.  
  
실제로는 애플리케이션 하위에 환경이 있습니다.  
이는 Elastic Benastalk은 컨셉상 하나의 애플리케이션을 호스팅하는데요.  
그러다보니 개발환경/스테이지환경/프로덕션환경 등등 여러 환경으로 나뉠수 있도록 **환경**이라는 단위가 있는 것입니다.  
  
그래서 실제로는 애플리케이션이 상위에, 환경이 하위에 있다고 보시면 됩니다.  
  
자 그럼 Beanstalk 환경이 생성되는 동안 저희는 Github Action 작업을 진행하겠습니다.

## 2. IAM 인증키 Github Action에서 사용하기

같은 AWS 서비스가 아닌 외부 서비스인 Github Action에서는 TravisCI와 마찬가지로 AWS 서비스에 명령을 줄 수 있는 권한을 받아야 합니다.  
  
> [Travis CI와 AWS ElasticBeanstalk 연동하기](https://jojoldu.tistory.com/317)

외부 서비스가 AWS 서비스에 대한 명령 권한을 받는 방법으로는 IAM 사용자를 이용한 인증키 (accessKey, secretKey)가 있습니다.  
  
그래서 해당 인증키를 먼저 발급 받겠습니다.

### 2-1. IAM 인증키 발급받기

AWS Beanstalk 생성 페이지를 두고, 새 페이지를 열어서 ```iam```을 검색해봅니다.  
그럼 아래와 같이 IAM 서비스가 나오는데요.  
해당 페이지로 이동합니다.

![iam1](./images/iam1.png)

좌측 사이드바의 **사용자** 항목을 선택합니다.  

![iam2](./images/iam2.png)

accessKey, secretKey를 발급받을 수 있는 사용자 정보를 생성합니다.  

![iam3](./images/iam3.png)

기존 정책 연결에서는 **AWS Beanstalk의 Access**를 할당 받습니다.

> Code Deploy때와 다르게 별도로 **S3에 대한 권한이 필요하지 않습니다**.  
> 배포 파일을 그대로 Beanstalk으로 전달하기 때문에 S3를 통할 필요가 없습니다.

![iam4](./images/iam4.png)

여러 사용자들 사이에서 식별 가능하도록 태그에는 Name을 지정합니다.

![iam5](./images/iam5.png)

생성 되시면 아래와 같이 accessKey와 secretKey가 생성됩니다.

![iam6](./images/iam6.png)

해당 내용을 복사를 하신 뒤 해당 프로젝트의 Github 페이지로 이동합니다.  
  
Github 에서 상단 탭을 보시면 **Settings**가 보입니다.  
클릭하신뒤, 좌측 사이드바의 **Secrets** -> **New Repository secret** 버튼을 차례로 클릭합니다.

![secret1](./images/secret1.png)

그럼 아래와 같이 생성된 IAM 인증키 항목을 등록하시면 되는데요.  

* AWS_ACCESS_KEY_ID: IAM 엑세스키 ID
* AWS_SECRET_ACCESS_KEY: IAM 비밀 엑세스 키

로 채워주시면 됩니다.  
  
**AWS_ACCESS_KEY_ID**

![secret2](./images/secret2.png)

**AWS_SECRET_ACCESS_KEY**

![secret3](./images/secret3.png)

다 생성 되시면 아래와 같이 secrets 항목에 2개의 키가 추가된 것을 확인할 수 있습니다.

![secret4](./images/secret4.png)

자 그럼 위에서 등록한 key들을 Github Action에서 사용할 수 있도록 스크립트에 코드를 심어보겠습니다.

### 2-2. Github Action 스크립트 수정하기

이번에 사용할 Github Action 플러그인은 [Beanstalk Deploy](https://github.com/marketplace/actions/beanstalk-deploy) 입니다.  
  
AWS CLI (커맨드라인)으로도 할 수 있지만, 해당 플러그인을 사용할 경우 아래와 같이 설정값만 채워주면 편하게 배포 코드를 작성할 수 있으니 이를 사용합니다.

```yaml
- name: Deploy to EB
    uses: einaregilsson/beanstalk-deploy@v14
    with:
    aws_access_key: ${{ secrets.AWS_ACCESS_KEY_ID }}
    aws_secret_key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
    application_name: MyApplicationName
    environment_name: MyApplication-Environment
    version_label: 12345
    region: ap-northeast-2
    deployment_package: deploy.zip
```

사이트를 방문 해보시면 최신 버전 사용방법을 볼 수 있는데요.

![plugin-eb1](./images/plugin-eb1.png)

14버전이 최신이니 ```einaregilsson/beanstalk-deploy@v14``` 를 선언만 하면됩니다.

![plugin-eb2](./images/plugin-eb2.png)

자 그럼 위 플러그인을 [지난 시간](https://jojoldu.tistory.com/543)에 만든 Github Action 스크립트 파일에 적용해보겠습니다.

> 몇번 언급드렸지만, 이 코드는 모두 **2021년 1월**을 기준으로 합니다.  
> 이후에 플러그인/AWS 콘솔등의 변경이 있을 수 있음을 미리 말씀드립니다.

```yaml
name: freelec-springboot2-webservice

on:
  push:
    branches:
      - version/2020-12-11  # push되면 실행될 브랜치를 선택합니다.
                            # ex) master (저는 version/2020-12-11 브랜치로 지정)
  workflow_dispatch: # 수동 실행

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v2

      - name: Set up JDK 1.8
        uses: actions/setup-java@v1.4.3
        with:
          java-version: 1.8

      - name: Grant execute permission for gradlew
        run: chmod +x ./gradlew
        shell: bash

      - name: Build with Gradle
        run: ./gradlew clean build
        shell: bash

      - name: Get current time
        uses: 1466587594/get-current-time@v2
        id: current-time
        with:
          format: YYYY-MM-DDTHH-mm-ss
          utcOffset: "+09:00"

      - name: Generate deployment package # (1)
        run: |
          mkdir -p deploy
          cp build/libs/*.jar deploy/application.jar
          cp Procfile deploy/Procfile
          cp -r .ebextensions deploy/.ebextensions
          cp -r .platform deploy/.platform
          cd deploy && zip -r deploy.zip .

      - name: Deploy to EB # (2)
        uses: einaregilsson/beanstalk-deploy@v14
        with:
          aws_access_key: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws_secret_key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          application_name: freelec-springboot2-webervice
          environment_name: freelec-springboot2-webervice
          version_label: github-action-${{steps.current-time.outputs.formattedTime}}
          region: ap-northeast-2
          deployment_package: deploy/deploy.zip
```

(1) 

* Gradle Build를 통해 만들어진 jar 파일을 Beanstalk에 배포하기 위한 zip 파일로 만들어줄 스크립트 입니다.
* 빌드가 끝나면 해당 배포 Jar의 파일명을 ```application.jar```로 교체합니다.
  * 매 빌드때마다 jar의 파일명이 버전과 타임스탬프로 파일명이 교체됩니다.
  * 그래서 Beanstalk 배포시에 매번 달라질 파일명을 찾아내기 보다는 하나로 통일해서 사용하도록 변경하였습니다.
* application.jar 외에 3개의 파일/디렉토리 ```Procfile```, ```.ebextensions```, ```.platform``` 도 함께 zip에 포함시킵니다.
* 3개 파일/디렉토리에 대해서는 아래 2-3에서 좀 더 상세하게 설명 드리겠습니다.

(2)

* Beanstalk 플러그인을 사용하는 코드입니다.
* 미리 생성해둔 IAM 인증키를 사용합니다.
* [이전 시간](https://jojoldu.tistory.com/543) 에 만들어준 **현재 시간** 플러그인을 통해 Beanstalk이 배포될때마다 유니크한 버저닝이 될 수 있도록 ```github-action-${{steps.current-time.outputs.formattedTime}}``` 코드를 추가하였습니다.

여기까지 하셨다면 배포 인프라 환경 구성은 끝이납니다.  
남은 작업은 **배포에 필요한 설정 파일**들을 만들어보겠습니다.  

> 위에서 언급한대로  ```Procfile```, ```.ebextensions```, ```.platform``` 3개에 대한 설정들입니다.

### 2-3. Beanstalk 애플리케이션 구성

3개 파일/디렉토리의 구조는 다음과 같이 됩니다.  
(즉 셋 모두 프로젝트 루트에서 생성해주시면 됩니다.)

![dir](./images/dir.png)

(deploy.yml은 저희가 기존에 만들어두었던 Github Action 스크립트 파일입니다.)  
  
위에서부터 차례로 생성해보겠습니다.  
  
첫번째는 ```.ebextensions``` 입니다.  
Beanstalk은 시스템의 대부분을 AWS에서 자동으로 구성해주기 때문에 기존 EC2에 직접 설치할때처럼 사용할 순 없는데요.  
그래서 직접 Custom 하게 사용할 수 있도록 설정할 수 있는 방법이 바로 ```.ebextensions``` 디렉토리입니다.  
  
해당 디렉토리에 ```.config``` 파일 확장명을 가진 YAML이나 JSON 형태의 설정 코드를 두면 그에 맞춰 Beanstalk 배포시/환경 재구성시 사용하게 됩니다.  
  
이번에 저희가 사용할 Custom 기능은 **애플리케이션 실행 스크립트 생성**입니다.  
Beanstalk이 Github Action으로 전달 받은 zip파일 (배포 파일)이 압축이 풀리고 나서 어느 파일을 어떤 파라미터로 실행할지를 설정하는 스크립트라고 보시면 됩니다.  

> ```java -jar application.jar``` 하는 코드를 스크립트로 만든다고 보시면 됩니다.
  
자 그래서 실제로 코드를 보시면 아래와 같습니다.  
  
**.ebextensions/00-makeFiles.config**

```vim
files:
    "/sbin/appstart" :
        mode: "000755"
        owner: webapp
        group: webapp
        content: |
            #!/usr/bin/env bash
            JAR_PATH=/var/app/current/application.jar

            # run app
            killall java
            java -Dfile.encoding=UTF-8 -jar $JAR_PATH
```  

* 아시다시피 ```/sbin``` 아래에 스크립트 파일을 두면 **전역에서 실행 가능**합니다.
* 그래서 ```/sbin``` 아래에 ```appstart```란 이름의 스크립트 파일을 만들고,
* 권한은 ```755```, 사용자는 ```webapp```으로 하여 ```content``` 내용을 가진 스크립트 파일이 생성된다고 보시면 됩니다.
* 여기서 만들어진 ```/sbin/appstart``` 스크립트 파일이 Procfile에서 실행됩니다.

> ```.ebextensions``` 의 ```config```에 대한 상세한 설명은 [우아한형제들의 기술 블로그](https://woowabros.github.io/woowabros/2017/08/07/ebextension.html)를 참고하시면 좋습니다.

다음으로는 Procfile입니다.  
Beanstalk은 배포 파일을 전달 받고나면 ```.ebextensions```를 비롯한 각종 설정파일들을 실행한 뒤에, 애플리케이션 실행 단계를 거치는데요.  
이때 애플리케이션 실행 단계때 하는 행위는 이 Procfile을 실행하는 것 뿐입니다.  
  
즉, Beanstalk 입장에서 **배포 애플리케이션 실행**이라는 것은 **Procfile파일을 실행**하는 것을 의미합니다.  
  
그래서 위에서 ```.ebextensions/00-makeFiles.config``` 으로 만들어진 ```/sbin/appstart``` 스크립트를 실행하도록 코드를 구성합니다.

**Procfile**

```bash
web: appstart
```

마지막으로 리버스 프록시를 담당할 Nginx 설정을 합니다.  
  
여기서 Nginx는 **무중단 배포를 위한 것이 아닙니다**.  
  
[제 저서에서는](https://jojoldu.tistory.com/463) Nginx를 이용해서 무중단 배포를 하는 방법을 소개 드렸는데요.  
  
이 시리즈에서는 **로드밸런서** (ALB) 가 그 역할을 대신 하기 때문에 Nginx에서는 단순히 **임베디드 톰캣으로 요청을 보내는 역할**만 할 예정입니다.  
  
그래서 아래와 같이 config 파일을 생성합니다.  
  
**.platform/nginx/nginx.conf**

```vim
user                    nginx;
error_log               /var/log/nginx/error.log warn;
pid                     /var/run/nginx.pid;
worker_processes        auto;
worker_rlimit_nofile    33282;

events {
    use epoll;
    worker_connections  1024;
}

http {
  include       /etc/nginx/mime.types;
  default_type  application/octet-stream;

  log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                    '$status $body_bytes_sent "$http_referer" '
                    '"$http_user_agent" "$http_x_forwarded_for"';

  include       conf.d/*.conf;

  map $http_upgrade $connection_upgrade {
      default     "upgrade";
  }

  upstream springboot {
    server 127.0.0.1:8080;
    keepalive 1024;
  }

  server {
      listen        80 default_server;

      location / {
          proxy_pass          http://springboot;
          proxy_http_version  1.1;
          proxy_set_header    Connection          $connection_upgrade;
          proxy_set_header    Upgrade             $http_upgrade;

          proxy_set_header    Host                $host;
          proxy_set_header    X-Real-IP           $remote_addr;
          proxy_set_header    X-Forwarded-For     $proxy_add_x_forwarded_for;
      }

      access_log    /var/log/nginx/access.log main;

      client_header_timeout 60;
      client_body_timeout   60;
      keepalive_timeout     60;
      gzip                  off;
      gzip_comp_level       4;

      # Include the Elastic Beanstalk generated locations
      include conf.d/elasticbeanstalk/healthd.conf;
  }
}
```

> Beanstalk에서의 Nginx 설정이 왜 ```.platform```가 되었는지 궁금하신 분들은 [이전 포스팅](https://jojoldu.tistory.com/541)을 참고하시면 좋습니다.

Beanstalk 배포를 위한 설정파일들도 모두 생성되었습니다.  
  
자 그럼 마지막으로 RDS 접속정보와 OAuth 인증 정보 없이 실행될 수 있도록 프로젝트 코드를 변경해보겠습니다.

## 3. Github Action으로 Beanstalk 배포하기

제 저서를 그대로 하셨다면, profile=local로 실행할 경우에도 OAuth 정보가 필요할텐데요.  
  
이번 시간에는 OAuth 정보 없이도 애플리케이션이 실행 가능해야하기 때문에 profile을 상세하게 분리해볼 예정입니다.

> [제 저서로 프로젝트를 구성하신 분들](https://jojoldu.tistory.com/463)만 참고해주세요.  
> 개인 프로젝트가 별도로 있으신분들에겐 해당하지 않습니다.

* ```profile=local```
  * 이번 시간에 사용될 배포환경
  * RDS나 구글/네이버 등의 OAuth정보 없이 **단순히 실행만 가능한 상태**
  * 테스트용 OAuth 토큰 정보와 H2 DB만 사용하는 상태
* ```profile=local-real```
  * 로컬 PC에서 개발용으로 사용할 환경
  * H2 DB를 사용하나, OAuth 정보는 실제 토큰 값을 사용하는 상태
* ```profile=real```
  * 서비스 배포 환경
  * RDS와 OAuth 정보를 모두 사용하는 상태

이렇게 나누는 이유는 RDS (DB) 접속 정보나 구글/네이버 등의 OAuth Key를 이번 챕터에서 다루기에는 범위가 너무 크기 때문입니다.  
  
해당 정보들은 외부에 공개되면 안되기 때문에 별도의 우회하는 방법을 선택해야하는데요.  
  
이것까지 포함하기에는 이번 챕터의 범위가 너무 크기 때문에 **테스트용 정보만 갖고 있는 profile**을 만들어서 사용하겠습니다.

> 기존의 ```local``` profile은 로컬 PC에서 개발한다는 의미로 구글/네이버 등의 OAuth Key를 사용하는 환경이였으나, 이를 제거하고 ```local-real```에서 구글/네이버 등의 OAuth Key를 가진다고 보시면 됩니다.

### 3-1. application.properties 정리

**application.properties**

```properties
spring.profiles.active=local
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
spring.jpa.properties.hibernate.dialect.storage_engine=innodb
spring.session.store-type=jdbc

spring.profiles.group.local-real=local-real, oauth
```

기존의 ```profiles.include``` 옵션이 아닌 ```profiles.group.그룹명``` 이라는 생소한 옵션이 보이실텐데요.  
  
이는 Spring Boot 2.4 부터 profile 사용 방식이 변경되었기 때문입니다.  

> 자세한 내용은 아래 2개글을 참고해주세요.
> * [스프링 부트와 AWS로 혼자 구현하는 웹 서비스 최신 코드로 변경하기](https://jojoldu.tistory.com/539)
> * [권남님의 Spring Boot 2.4 multi module properties & profile-group](https://github.com/kwon37xi/research-spring-boot-2.4/tree/master/profile-group?fbclid=IwAR2UJoItIrzo6NpmAn58x4wNDGxG5rsxbUFbIIzCRYxu10c5fgXKdTUOmQA)

간단하게 정리하면 ```spring.profiles.group.local-real=local-real, oauth``` 로 선언되면 앞으로 ```local-real```로 실행할 경우 ```local-real```과 ```oauth``` profile이 묶음으로 포함되어 실행된다는 것입니다.  
  
위 설정으로 이제 공통 설정들은 끝이나, 이번 Beanstalk에 테스트 용도로 배포할 profile 설정을 해보겠습니다.  
  
**application-local.properties**

```properties
spring.jpa.show_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
spring.jpa.properties.hibernate.dialect.storage_engine=innodb
spring.datasource.hikari.jdbc-url=jdbc:h2:mem:testdb;MODE=MYSQL
spring.datasource.hikari.username=sa

spring.h2.console.enabled=true
spring.session.store-type=jdbc

# Test OAuth

spring.security.oauth2.client.registration.google.client-id=test
spring.security.oauth2.client.registration.google.client-secret=test
spring.security.oauth2.client.registration.google.scope=profile,email
```

마지막으로 로컬 PC에서 실제 개발에 사용될 (OAuth정보를 사용하되, H2 DB를 사용하는) profile을 설정하겠습니다.  
  
**application-local-real.properties**

```properties
spring.jpa.show_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
spring.jpa.properties.hibernate.dialect.storage_engine=innodb
spring.datasource.hikari.jdbc-url=jdbc:h2:mem:testdb;MODE=MYSQL
spring.datasource.hikari.username=sa

spring.h2.console.enabled=true
spring.session.store-type=jdbc
```

> IntelliJ에서 기본 설정된 profile(```local```) 이 아닌 별도의 profile (```local-real```) 로 실행하는 방법에 대해서는 [다른 포스팅](https://jojoldu.tistory.com/547)에서 정리해두었으니 참고해주세요.

여기까지 다 하셨으면, 이제 Github으로 Push를 해봅니다.

## 4. Beanstalk 배포

Push를 하시면 아래와 같이 Github Action 빌드로그를 확인할 수 있습니다.  
  
**Generate deployment package**

![ga-log1](./images/ga-log1.png)

**Deploy to EB**

![ga-log2](./images/ga-log2.png)

Github Action 에서 성공 메세지를 확인하셨다면 AWS Beanstalk 페이지를 보시면 다음과 같이 배포가 성공된 것을 확인할 수 있습니다.

![eb-log](./images/eb-log.png)

성공하셨으면 Beanstalk의 대시보드에 있는 URL을 클릭해봅니다.

![eb-url](./images/eb-url.png)

H2 DB와 테스트용 OAuth 토큰이지만, 사이트가 정상적으로 실행되는 것을 확인할 수 있습니다.

![success](./images/success.png)

> 현재 상태로는 **로그인 기능을 사용할 수 없습니다**  
> 테스트용 값들로 OAuth 토큰을 채웠기 때문이고, 다음 시간에 실제 토큰 값으로 배포하는 방법을 배워볼 예정입니다.

혹시나 Beanstalk 배포가 실패하면 상세한 이슈를 확인하시려면 해당 Beanstalk의 EC2 서버로 접속하여 아래 경로에 있는 로그를 확인해봅니다.

```bash
vim /var/log/eb-engine.log
```

그럼 아래와 같이 배포 실패한 사유를 확인할 수 있습니다.

![eb-error-log](./images/eb-error-log.png)

(위 에러는 ```nginx.conf``` 파일이 배포 파일에 포함되어 있지 않음을 나타냅니다.)

## 5. 마무리

이번 시간에는 실제 운영 정보 (DB 접속정보, OAuth 토큰정보) 등을 제외하여 최대한 배포에만 초점을 맞췄습니다.  
  
이렇게 배포환경이 한번 구성되면, 이후에는 정보보안이 필요한 정보들에 대해서만 별도로 처리하면 운영 배포환경 구성이 됩니다.  

한번에 다 해볼수도 있겠지만, 진행을 하다보면 한번에 너무 많은 일들이 진행되면 이슈가 발생할 때 **정확히 어디 지점이 문제인지 파악이 어렵다**고 생각합니다.  
  
이번 챕터까지 잘 진행되셨으면 다음 챕터 진행시에는 큰 무리 없이 가능하니, 꼭 차근차근 진행해보시길 추천드립니다.
