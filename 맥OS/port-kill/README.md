# Mac OS에서 특정 포트 Process 종료하기

간혹 IntelliJ를 사용하다가 JVM 프로세스를 종료하였음에도, 
이는 IntelliJ까지 종료하여도 프로세스가 남는 경우가 있는데

```bash
lsof -i :포트번호
```

```bash
kill 프로세스ID
```

한번에 쉽게 하려면 다음과 같이 진행할 수 있다.

```bash
kill $(lsof -i:8080)
```