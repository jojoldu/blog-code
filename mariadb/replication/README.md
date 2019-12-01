# MariaDB 에서 Replication (레플리케이션) 설정하기

## 0. 작업 환경

* AWS EC2 * 3대
  * Amazon Linux 1
    * Centos 6 사용하셔도 무방
* MariaDB 1.1.x
  * 현재 (2019.11.18) 1.4.10 까지 나왔으나 사용하는 버전과 맞추기 위해 1.10 사용
  * ```yum install```로 설치


## 1. 레플리케이션이란?

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