# MariaDB 설치 및 설정

AWS 를 쓸 수 있다면 Aurora가 정답이지만, IDC를 쓰고 있다면 MariaDB 혹은 MySQL을 써야 합니다.  
  
여기선 IDC 환경에서 MariaDB 운영을 위해 필요한

## 1. 설치

저 같은 경우 별도로 IDC 장비를 사용하지 못하여 AWS EC2에 직접 설치하며 사용할 예정입니다.  

> Centos 6을 쓰신다면 거의 비슷한 명령어로 수행할 수 있습니다.

### 사용자 계정 추가

서버의 root 계정이 있다면 root 계정으로 전환하고 사용 하고, sudo 권한만 있다면 sudo로 명령어를 실행합니다.  
  
먼저 그룹 추가를 합니다.  


```bash
sudo groupadd -g 600 dba
```

사용자 계정을 생성합니다.

```bash
sudo useradd -g 600 -u 605 mysql
```

방금 생성한 mysql 계정의 비밀번호를 등록합니다.

```bash
sudo passwd mysql
```

### ulimit 수정

다음으로 ulimit 설정을 합니다.

> ulimit에 대한 자세한 내용은 예전에 [작성한 글](http://woowabros.github.io/experience/2018/04/17/linux-maxuserprocess-openfiles.html)을 참고해보세요.

아래 파일을 열어 확인해봅니다.

```
/etc/security/limits.conf
```

아래와 같이 전체 사용자의 nofile 옵션을 추가합니다.

```
*               soft    nofile          65535
*               hard    nofile          65535
```

![1](./images/1.png)

### MariaDB 설치

아래 명령어로 yum repository를 열어봅니다.

```bash
sudo vim /etc/yum.repos.d/maria.repo
```

아래 내용을 그대로 붙여넣습니다.

```bash
[mariadb]
name = MariaDB
baseurl = http://yum.mariadb.org/10.1/centos6-amd64
gpgkey=https://yum.mariadb.org/RPM-GPG-KEY-MariaDB
gpgcheck=1

```

* baseurl 은 본인이 원하는 스펙으로 합니다.
  * 10.1 은 MariaDB의 버전을 얘기합니다.
    * 즉, 10.1.x 버전을 사용합니다 (10.1.12를 사용해도 10.1로 입력하셔야 합니다.)
    * 저는 현재 업무에서 사용되는 버전을 사용했습니다.
    * 현재 (2019.07.10) 으로는 10.3 (10.3.16) 까지 출시되었습니다.
* centos6-amd64
  * 현재 리눅스 서버 스펙을 선택합니다.
  * Centos 64비트를 사용하는데, 본인이 Centos 7을 사용한다면 ```centos7-amd64``` 을 쓰시면 됩니다.

yum install 을 실행합니다.

```
sudo yum install -y MariaDB-server MariaDB-client
```

### my.cnf 설정

```
mkdir -p /data/mysql/mysql-data
mkdir -p /data/mysql/mysql-tmp
mkdir -p /data/mysql/mysql-binlog
mkdir -p /data/mysql/mysql-ibdata
chown -R mysql.dba /usr/local/mysql /data/mysql /etc/my.cnf
```


```
[client]
port                            = 3306
socket                          = /data/mysql/mysql.sock

[mysqld]
port                            = 3306
socket                          = /data/mysql/mysql.sock

## config server and data path
basedir                         = /usr/local/mariadb
datadir                         = /data/mysql/mysql-data
tmpdir                          = /data/mysql/mysql-tmp
pid-file                        = /data/mysql/mysql.pid
innodb_data_home_dir           = /data/mysql/mysql-data
innodb_log_group_home_dir       = /data/mysql/mysql-iblog



# Log Config
binlog_format                   = mixed
expire_logs_days                = 7
long_query_time                 = 10
max_binlog_size                 = 1G
sync_binlog                     = 1
slow_query_log                  = 1
log-bin                         = /home/MariaDB/log/binary/mysql-bin
log-error                       = /home/MariaDB/log/error/mysql.err
relay-log                       = /home/MariaDB/log/relay/relay-log
slow_query_log_file             = /home/MariaDB/log/mysql-slow-query.log
general_log_file                = /home/MariaDB/log/general/mysql_general.log
log-warnings                    = 2

# Character set Config
character_set-client-handshake  = FALSE
character-set-server            = utf8
init_connect                    = set collation_connection=utf8_general_ci
init_connect                    = set names utf8

# Common Config
back_log                        = 1024
binlog_cache_size               = 1M
ft_min_word_len                 = 4
interactive_timeout             = 600
join_buffer_size                = 2M
max_allowed_packet              = 1G
max_connections                 = 8196
max_heap_table_size             = 4096M
max_length_for_sort_data        = 1024
open_files_limit                = 8192
performance_schema
read_buffer_size                = 1M
read_rnd_buffer_size            = 8M
skip_external_locking
skip-name-resolve               = 1
sort_buffer_size                = 1M
key_buffer_size                 = 8388608
table_open_cache                = 10240
tmp_table_size                  = 4096M
transaction_isolation           = READ-COMMITTED
slave_skip_errors               = all

# Query Cache Disable
query_cache_type                = 0
query_cache_size                = 0

# Innodb config
innodb_additional_mem_pool_size = 32M
innodb_autoinc_lock_mode        = 1
innodb_buffer_pool_size         = 32G
innodb_fast_shutdown            = 1
innodb_file_per_table           = 1
innodb_flush_log_at_trx_commit  = 2
innodb_flush_method=ALL_O_DIRECT
innodb_lock_wait_timeout        = 72000
innodb_log_buffer_size          = 64M
innodb_log_file_size            = 512M
innodb_log_files_in_group       = 8
innodb_mirrored_log_groups      = 1
innodb_open_files               = 8192
innodb_read_io_threads          = 8
innodb_thread_concurrency       = 0
innodb_thread_sleep_delay       = 0
innodb_write_io_threads         = 8

#Thread Pool
thread_handling=pool-of-threads
thread_pool_idle_timeout        = 120
thread_pool_stall_limit         = 60

# Replication related settings
log_bin_trust_function_creators = 1

server-id                       = 1

[mysqldump]
quick
max_allowed_packet              = 512M

```