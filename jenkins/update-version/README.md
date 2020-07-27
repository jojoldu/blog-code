# Jenkins 버전 업데이트하기

![intro](./images/intro.png)

```bash
ps -ef | grep jenkins
```

![war](./images/war.png)


```bash
service jenkins stop
```


![version](./images/version.png)

가장 최신 버전

```bash
http://mirrors.jenkins-ci.org/war/latest/jenkins.war
```

LTS 버전

```bash
http://mirrors.jenkins-ci.org/war-stable/latest/jenkins.war
```

![wget](./images/wget.png)

```bash
wget http://mirrors.jenkins-ci.org/war/latest/jenkins.war
```

```bash
wget http://mirrors.jenkins-ci.org/war-stable/latest/jenkins.war
```


```bash
mv 다운받은war경로 /usr/lib/jenkins/jenkins.war
```