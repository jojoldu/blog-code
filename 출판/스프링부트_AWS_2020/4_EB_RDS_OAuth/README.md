# Github Action & AWS Beanstalk 배포하기 - 운영환경 배포하기

```bash
files:
    "/sbin/appstart" :
        mode: "000755"
        owner: webapp
        group: webapp
        content: |
            #!/usr/bin/env bash
            JAR_PATH=/var/app/current/application.jar

            # Datasource Conifg
            JDBC_URL="--spring.datasource.hikari.jdbc-url=@JDBC_URL"
            USERNAME="--spring.datasource.hikari.username=@USERNAME"
            PASSWORD="--spring.datasource.hikari.password=@PASSWORD"

            # OAuth
            GOOGLE_ID="--spring.security.oauth2.client.registration.google.client-id=@GOOGLE_ID"
            GOOGLE_SECRET="--spring.security.oauth2.client.registration.google.client-secret=@GOOGLE_GOOGLE_SECRET"

            NAVER_ID="spring.security.oauth2.client.registration.naver.client-id=@NAVER_ID"
            NAVER_SECRET="spring.security.oauth2.client.registration.naver.client-secret=@NAVER_SECRET"

            # run app
            killall java
            #java -Dfile.encoding=UTF-8 -jar $JAR_PATH $JDBC_URL $USERNAME $PASSWORD $GOOGLE_ID $GOOGLE_SECRET $NAVER_ID $NAVER_SECRET
            java -Dfile.encoding=UTF-8 -jar $JAR_PATH

container_commands:
  001-command:
      command: sed -i "s/@JDBC_URL=/${SPRING_DATASOURCE_HIKARI_RDS_HOST}/g" /sbin/appstart
  002-command:
      command: sed -i "s/@USERNAME=/${SPRING_DATASOURCE_HIKARI_USERNAME}/g" /sbin/appstart
  003-command:
      command: sed -i "s/@PASSWORD=/${SPRING_DATASOURCE_HIKARI_PASSWORD}/g" /sbin/appstart
  004-command:
      command: sed -i "s/@GOOGLE_ID=/${GOOGLE_ID}/g" /sbin/appstart
  005-command:
      command: sed -i "s/@GOOGLE_GOOGLE_SECRET=/${GOOGLE_GOOGLE_SECRET}/g" /sbin/appstart
  006-command:
      command: sed -i "s/@NAVER_ID=/${NAVER_ID}/g" /sbin/appstart
  007-command:
      command: sed -i "s/@NAVER_SECRET=/${NAVER_SECRET}/g" /sbin/appstart
```

## RDS

RDS는 빈스톡 환경 변수로 관리한다

* 개발환경
* QA환경
* 운영환경

등 DB가 여러 환경으로 나눠져있을 수 있기 때문

## OAuth

OAuth 정보의 경우 별도로 환경에 관계 없이 동일하게 구성이 가능하기 때문에 Github Action으로 정보를 관리한다.



## 타임존 설정

빈스톡 Config를 통한 타임존 설정하기

```bash
commands:
  01remove_local:
    command: "rm -rf /etc/localtime"
  02link_seoul_zone:
    command: "ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime"
```