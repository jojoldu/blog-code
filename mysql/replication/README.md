# MariaDB 에서 Replication (레플리케이션-복제) 설정하기

## 0. 작업 환경

* AWS EC2 * 3대
  * Amazon Linux 1
    * Centos 6 사용하셔도 무방
* MariaDB 1.1.x
  * 현재 (2019.11.18) 1.4.10 까지 나왔으나 사용하는 버전과 맞추기 위해 1.10 사용
  * ```yum install```로 설치


## 1. 레플리케이션이란?

보통 다음과 같은 이유로 레플리케이션을 사용합니다.

* 고가용성
  * 여러개의 슬레이브로 단일 실패 지점 (SPOF - Single Point Of Failure) 을 벗어나게 해줍니다.
* 읽기 성능 향상
  * 단일 서버가 아닌 복제된 여러 서버로 읽기 요청이 분산 되어 읽기 성능 향상이 가능합니다.
* 대용량 데이터 통계/집계 처리
  * 대량의 데이터를 통계/집계할 경우 복제된 서버를 사용하면 실제 라이브 서버에는 영향 없이 안정적으로 처리 가능 

## 2. Replication 설정

### 2-1. Percona Repository 등록

```bash
yum install -y https://repo.percona.com/yum/percona-release-latest.noarch.rpm
```

```bash
yum list | grep percona
```

```bash
$ yum list | grep percona
percona-release.noarch                1.0-13                        installed
Percona-SQL-50-debuginfo.x86_64       5.0.92-b23.89.rhel6           percona-release-x86_64
Percona-SQL-client-50.x86_64          5.0.92-b23.89.rhel6           percona-release-x86_64
Percona-SQL-devel-50.x86_64           5.0.92-b23.89.rhel6           percona-release-x86_64
Percona-SQL-server-50.x86_64          5.0.92-b23.89.rhel6           percona-release-x86_64
Percona-SQL-shared-50.x86_64          5.0.92-b23.89.rhel6           percona-release-x86_64
Percona-SQL-shared-compat.x86_64      5.0.92-b23.89.rhel6           percona-release-x86_64
Percona-SQL-test-50.x86_64            5.0.92-b23.89.rhel6           percona-release-x86_64
```

### server id 변경