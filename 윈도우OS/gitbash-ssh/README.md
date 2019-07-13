# 윈도우에서 ssh 터미널 사용하기


Git Bash를 이용하여 putty 대체하기

## Dracula 테마 적용하기

![dracula2](./images/dracula2.png)

## ssh config 등록

```bash
vim ~/.ssh/config
```

```bash
Host *
KexAlgorithms +diffie-hellman-group1-sha1
HostKeyAlgorithms +ssh-dss
Ciphers +3des-cbc
Macs +hmac-md5
UserKnownHostsFile ~/.ssh/known_hosts

Host 호스트명
    HostName 호스트IP
    User 호스트사용자명

```

