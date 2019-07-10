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

