# Ngrinder 설치하기 (feat. AWS Beanstalk)

## 1. EC2에 Web 설치하기

```bash
wget https://github.com/naver/ngrinder/releases/download/ngrinder-3.5.5-20210430/ngrinder-controller-3.5.5.war
```

```bash
mv ngrinder-controller-3.5.5.war ngrinder-controller.war
```

```bash
nohup java -jar ngrinder-controller.war >/dev/null 2>&1 &
```


```bash
ps -ef | grep java
```

```bash
ec2-user  2601  2528 21 15:06 pts/0    00:00:00 java -jar ngrinder-controller.war
ec2-user  2614  2528  0 15:06 pts/0    00:00:00 grep --color=auto java
```


## 2. Beanstalk에 Agent 설치하기