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

            # run app
            killall java
            #java -Dfile.encoding=UTF-8 -jar $JAR_PATH $JDBC_URL $USERNAME $PASSWORD
            java -Dfile.encoding=UTF-8 -jar $JAR_PATH

container_commands:
  001-command:
      command: sed -i "s/@JDBC_URL=/${SPRING_DATASOURCE_HIKARI_RDS_HOST}/g" /sbin/appstart
  002-command:
      command: sed -i "s/@USERNAME=/${SPRING_DATASOURCE_HIKARI_USERNAME}/g" /sbin/appstart
  003-command:
      command: sed -i "s/@PASSWORD=/${SPRING_DATASOURCE_HIKARI_PASSWORD}/g" /sbin/appstart
```

## OAuth

OAuth 정보의 경우 별도로 환경에 관계 없이 동일하게 구성이 가능하기 때문에 Github Action으로 정보를 관리한다.

## RDS

RDS는 빈스톡 환경 변수로 관리한다

* 개발환경
* QA환경
* 운영환경

등 DB가 여러 환경으로 나눠져있을 수 있기 때문

## 타임존 설정

빈스톡 Config를 통한 타임존 설정하기

```bash
commands:
  01remove_local:
    command: "rm -rf /etc/localtime"
  02link_seoul_zone:
    command: "ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime"
```