# MariaDB ERROR 1524 Plugin is Not Loaded 문제 해결

MariaDB를 설치하던 중에 root 계정의 비밀번호 설정을 잘못할 때가 있습니다.  
그럼 아래와 같이 접속 시도시마다 에러가 발생합니다.

```bash
ERROR 1524 (HY000): Plugin '잘못된 플러그인' is not loaded
```

이 문제를 해결해보겠습니다.

## 1. 해결

서버에 접속해 root 계정으로 전환합니다.

```bash
sudo su - root
```

이제 실행하는 모든 명령어는 ```sudo```가 생략되었다고 생각하시면 됩니다.  
  
정상적인 ```MariaDB``` 실행으로는 위와 같이 플러그인 에러가 계속 발생합니다.  
그래서 이럴때를 대비해 mysqld_safe (일종의 윈도우에서 안전모드 같은 거라 생각하시면 됩니다.)를 사용합니다.  
  
아래 명령어로 실행해봅니다.

```bash
mysqld_safe --skip-grant-tables &
```

enter를 입력하면 아래와 같은 로그가 나오면서 입력 모드로 전환됩니다.

```bash
[2] 7551
[root@mariadb1 ~]# 191117 20:27:10 mysqld_safe Logging to '/home/mysql/log/error/mysql.err'.
191117 20:27:10 mysqld_safe A mysqld process already exists

[2]+  Exit 1                  mysqld_safe --skip-grant-tables
```

root 계정으로 mysqld_safe 에 접근합니다.

```bash
mysql -u root
```

> 이제부터는 모두 MariaDB CLI로 실행하는 것입니다.

먼저 현재 문제가 되는 플러그인 상태를 확인합니다.

```sql
select Host,User,plugin from mysql.user where User='root';
```

아래와 같이 문제의 플러그인들이 설정되어있는 것을 확인할 수 있습니다.

```bash
+------------------+------+-----------+
| Host             | User | plugin    |
+------------------+------+-----------+
| localhost        | root | 문제플러그인 |
| ip-172-31-26-125 | root | 문제플러그인 |
| 127.0.0.1        | root | 문제플러그인 |
| ::1              | root | 문제플러그인 |
+------------------+------+-----------+
4 rows in set (0.00 sec)
```

이들을 모두 초기화합니다.

```sql
update mysql.user set plugin='mysql_native_password';
```

그리고 다시 root 계정의 비밀번호를 등록합니다.

```sql
update mysql.user set password=PASSWORD("본인비밀번호") where User='root';
```

아래와 같이 성공적으로 메세지가 뜨면 됩니다.

```bash
Query OK, 4 rows affected (0.00 sec)
Rows matched: 4  Changed: 4  Warnings: 0
```

최종적으로 반영합니다.

```sql
flush privileges;
```

위 과정이 모두 끝나셨다면 MariaDB CLI를 종료하여 비밀번호 설정이 잘 되었는지 테스트해보겠습니다.

```sql
quit;
```

위 과정을 실제로 진행한 내용의 캡쳐입니다.

![1](./images/1.png)

## 2. 재접속

일단 mysqld_safe 를 종료합니다.

```bash
sudo kill -9 $(pgrep mysql)
```

다시 정상적으로 MariaDB를 실행해봅니다.

```bash
service mysql start
```

root 계정으로 접속해봅니다.

```bash
mysql -u root -p
```

비밀번호가 정상적으로 사용되면 성공입니다.