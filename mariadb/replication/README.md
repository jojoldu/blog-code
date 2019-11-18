# MariaDB 에서 Replication (레플리케이션) 설정하기

## 0. 작업 환경

* AWS EC2 * 3대
  * Amazon Linux 1
    * Centos 6 사용하셔도 무방
* MariaDB 1.1.x
  * 현재 (2019.11.18) 1.4.10 까지 나왔으나 사용하는 버전과 맞추기 위해 1.10 사용
  * ```yum install```로 설치


## Replication 설정

### server id 변경