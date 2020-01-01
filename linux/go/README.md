# 원격서버 접속해서 root권한까지 받기

이번 시간에 진행할 내용은 원격서버로 ssh 접속시 root권한까지 받는 것을 자동화 하려고 한다.<br/>
예를 들면 이런 것이다. <br/>
리눅스 PC에서 <br/>
(1) 원격서버에 내 계정 (ssh jojoldu@~~)으로 접근 <br/>
(2) jojoldu 계정에 대한 비밀번호 입력 및 접속 <br/>
(3) jojoldu 계정에서 root 계정으로 전환 (su -) <br/>
(4) root 계정 비밀번호 입력 <br/>

<br/>
이건 사실 굳이 스크립트로 작성 안해도 될 정도의 작은 작업량이긴 한데, <br/>
그래도 가끔 root 권한이 필요할 때 비밀번호 입력이 귀찮기도 하고, 이것저것 커맨드 날리다보면 history가 잘 관리 안되서 history를 뒤져야 하는 일이 생겨버려서 그냥 스크립트를 작성하게 되었다. <br/>
<br/>
작업 내용은 간단하다. <br/>
(1) ~ (3) 내용까지를 스크립트로 실행되도록 하는 것이다. <br/>
( (4) 은 어떻게 코드를 짜야할지 잘모르겠어서 일단은 보류...) <br/>
바로 코드부터 시작하겠다!

### 코드
스크립트 파일명은 go.sh로 지었다. <br/>

```
#!/bin/bash

RED="\033[1;31m"
RESET="\033[0m"

function color_echo {
        # $RED + $1 + $RESET
        echo -e "$RED$1$RESET"
}

USER=jojoldu
PASSWORD=비밀번호
HOST=원격서버 주소
PORT=원격서버 포트

function connect_server {
	color_echo "connet server"
	sshpass -p $PASSWORD ssh -t -p$PORT $USER@$HOST "su - root"
}


connect_server

```

실질적으로 수행하는 함수는 connect_server 뿐이다. <br/>
(color_echo의 경우 그냥 ```echo```를 빨간색으로 출력하는 기능이라 크게 신경쓰지 않아도 될것 같다.) <br/>
bash의 경우 일반적으로 **ssh에 접속후 password를 입력할 수 있는 방법이 없는것** 같았다.<br/>
(혹시 있다면 꼭 댓글 부탁드립니다.. 몰라서 이렇게 한거라 ㅠ) <br/>
그래서 sshpass를 통해 **미리 ssh 접속에 필요한 password를 입력** 하도록 한 것이다. <br/>
즉, ```sshpass -p $PASSWORD``` 까지가 1셋트 <br/>
```ssh -t -p$PORT $USER@$HOST "su - root"``` 가 1셋트이다. <br/>
sshpass로 ssh 명령어를 실행시킬때 password를 미리 입력하도록 하였다. <br/>
이후 ssh 명령어를 실행하되, ```-t``` 옵션으로 터미널 기능을 부여한 것이다. <br/>
-t로 실행시킬 것은 "su - root" 이므로 <br/>
바로 root 계정으로 전환을 시도하게 된다. <br/>
즉, (1) ~ (3) 까지의 과정을 go.sh로 끝내고 마지막 root 계정의 비밀번호만 개발자가 입력하면 된다. <br/>
이렇게 함으로써 (2) 과정에서 내가 직접 내 계정(jojoldu) 의 비밀번호 입력단계를 생략할 수 있고, <br/>
root 계정으로 전환하는 명령어인 ```su -``` 도 생략할 수 있게 되었다. <br/>
root 계정의 비밀번호 입력 방법도 한번 찾아봐야겠다.
<br/>
**끝!**
