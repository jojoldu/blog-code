# Spring Batch 공통 설정 관리하기 (feat. 젠킨스 Environment variables)

젠킨스의 경우 스프링 배치의 스케줄러로 많이 사용 됩니다.  
다만, **공통 설정**을 어떻게 해야할지 고민될 때가 많습니다.  
이럴 경우 젠킨스의 Environment variables 가 아주 유용합니다.  
이번 시간에는 젠킨스의 Environment variables를 통해 스프링 배치의 공통 설정들을 관리해보겠습니다.

## 1. 기존 상황

젠킨스에서 스프링 배치를 사용하다보면 다음과 같은 상황을 자주 목격합니다.

![1](./images/1.png)

```bash
java -jar \
-XX:+UseG1GC \
-Dspring.profiles.active=real \
배치jar \
--job.name=스프링배치Job이름 \
파라미터1=파라미터값1 \
파라미터2=파라미터값2
```

* ```-XX:+UseG1GC```
* ```-Dspring.profiles.active=real```
* ```배치jar```

## 2. 젠킨스 Environment variables 설정하기

![2](./images/2.png)

![3](./images/3.png)

![4](./images/4.png)


## 3. readlink 사용하기


![1](./images/read1.png)

```bash
ORIGIN_JAR=$(readlink -f 배치jar)

java -jar \
-XX:+UseG1GC \
-Dspring.profiles.active=real \
${ORIGIN_JAR} \
--job.name=스프링배치Job이름 \
currentTime=${currentTime} \
version=${BUILD_NUMBER}
```

> ```$(readlink -f 배치jar)```는 스프링 배치를 **무중단 배포**하기 위해 자주 사용되는 방법입니다.  
왜 필요한지, 어떻게 사용되는지 궁금하신 분들은 [이전에 작성된 포스팅](https://jojoldu.tistory.com/315)을 참고해보세요.

