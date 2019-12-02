# Xtrabackup 복구하기


## 백업 파일 다운로드

**s3**

```bash
aws s3 cp s3://버킷/압축파일 ./
```

**wget**

```bash
wget https://aws주소/버킷/압축파일
```

## 압축풀기

먼저 빠르게 압축 해제를 위해 ```pigz``` 패키지를 설치하겠습니다.

> pigz란? 압축 파일을 병렬 & 멀티프로세서 & 멀티코어로 해제 할 수 있게 지원하는 패키지입니다.
> 지금처럼 100G가 넘는 압축 파일을 해제시 속도 향상을 위해 사용됩니다.


```bash
yum install pigz
```

설치가 끝나셨다면 아래와 같이 ```nohup &``` 으로 ```pigz```를 실행합니다.

> ```nohup &``` 을 사용하는 이유는 지금처럼 대용량 파일 압축 해제시 최소 1시간, 최대 10시간 이상 소요될 수 있기 때문입니다.
> 압축 해제 하는 동안 사용자 세션이 끊기면 강제 종료 되니, 세션 관계 없이 백그라운드로 실행하기 위해 필수로 사용됩니다.
> 파일 용량이 적을때는 ```pigz -dc 2019-02-28_21-10-01.tar.gz | tar xvfi -``` 만 사용하셔도 됩니다.


```bash
nohup sh -c "pigz -dc 2019-02-28_21-10-01.tar.gz | tar xvfi -" &
```

해당 명령어가 잘 수행되는지 확인 하기 위해 ```tail -f```로 로그를 확인합니다.

```bash
tail -f nohup.out
```

아래와 같이 ibdata1을 시작으로 압축 해제된 디비 정보들이 보이면 잘 되고 있는 것입니다.

```bash
$ tail -f nohup.out
ibdata1
mysql/innodb_index_stats.ibd
mysql/gtid_slave_pos.ibd
```

## 복구하기

압축 해제가 잘되었으면 

```bash
vim /etc/my.cnf
```

```bash
## config server and data path
datadir                         = /data/mysql/mysql-data
```

```bash
mv 
```
