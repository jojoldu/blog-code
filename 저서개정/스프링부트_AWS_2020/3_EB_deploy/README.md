# 

## IAM 인증키 발급받기

## IAM 인증키 Github Action에서 사용하기

책 p.239에서 설정한 보안그룹을 선택합니다.


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

```yaml
name: freelec-springboot2-webservice

on:
  push:
    branches:
      - version/2020-12-11 # push되면 실행될 브랜치를 선택합니다. ex) master (저는 version/2020-12-11 브랜치로 지정)
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

        - name: Generate deployment package
            run: |
            mkdir -p deploy
            cp build/libs/*.jar deploy/application.jar
            cp Procfile deploy/Procfile
            cp -r .ebextensions deploy/.ebextensions
            cd deploy && zip -r deploy.zip .

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


[](https://github.com/marketplace/actions/beanstalk-deploy)



![eb-log1](./images/eb-log1.png)

```bash
vim /var/log/eb-engine.log
```