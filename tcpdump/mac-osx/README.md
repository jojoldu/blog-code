# Mac OSX에서 tcpdump 사용하기

* 설치하기

```bash
brew install tcpdump
```

Mac일 경우 tcpdump 사용시 BSD name을 옵션에 추가해야만 합니다.

```bash
networksetup -listnetworkserviceorder
```

![mac1](./images/mac1.png)