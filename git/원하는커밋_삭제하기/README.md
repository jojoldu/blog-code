# Git에서 SourceTree로 원하는 커밋만 제거하기

Git을 사용하다보면 특정 커밋들만 날리고 싶을때가 있습니다.  
근데 하필 그게 한참 전 커밋들이거나, 중간 커밋들이면 `reset`으로 골라내기가 쉽지 않은데요.  
이럴때 `rebase interactively` 를 사용하기가 좋습니다.

## 해결방법

예를 들어 아래와 같이 `REC-296` 커밋 2개를 모두 빼고 싶다고 하겠습니다.

![1](./images/1.png)

![2](./images/2.png)

![3](./images/3.png)

![4](./images/4.png)

![5](./images/5.png)

![6](./images/6.png)

![7](./images/7.png)