# 젠킨스와 Github ssh 연동하기


## 1. 키 생성

현재 실행중인 젠킨스 사용자를 확인합니다.

```bash
ps aux | grep jenkins
```

![1](./images/1.png)

> 특별히 설정하지 않으면 jenkins 사용자로 실행중입니다.

젠킨스가 ```jenkins``` 사용자로 실행중인게 확인되었다면 **현재 사용자를 jenkins로 전환**합니다.

```bash
sudo -u jenkins /bin/bash
```

![2](./images/2.png)

> jenkins 사용자는 일반적으로 서버에 등록된 사용자는 아닙니다.  
그러다보니 ```sudo su - u jenkins``` 등으로 전환은 안됩니다.


```bash
ssh-keygen -t rsa -f /var/lib/jenkins/.ssh/github_ansible-in-action
```

![3](./images/3.png)

![4](./images/4.png)

## 2. Github

공개키 복사

![5](./images/5.png)

![6](./images/6.png)

![7](./images/7.png)

![8](./images/8.png)

![9](./images/9.png)


## 3. 젠킨스

비공개키 복사
