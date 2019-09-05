# 젠킨스 빌드 파일 캐시 문제

젠킨스를 통해 Github Clone -> Build를 하다보면 다음과 같이 **이름이 변경된 파일의 이전 파일**이 그대로 남아 있는 경우를 종종 보게 됩니다.  
  
아래 사진은 01-makeFiles.config였던 파일을 00-makeFiles.config로 변경했는데 그대로 00도 남아있는 경우입니다.

![1](./images/1.png)

(실제 프로젝트에서는 이미 01-makeFiles.config 파일이 존재하지 않습니다.)  
  
이는 젠킨스의 workspace가 **완전히 초기화 되지 않기 때문**입니다.  
  
젠킨스의 경우 ```npm install``` 등 패키지 다운로드가 매번 build때마다 발생하는 것을 막기 위해 workspace를 삭제하지 않고 **overwrite** 하는 방식으로 진행됩니다.  
  

![2](./images/2.png)

![3](./images/3.png)

![4](./images/4.png)

## 번외

매번 전체 workspace를 날릴순 없으니 **예외 패턴**을 넣을 수 있습니다.  
저 같은 경우 ```node_modules```와 ```.git``` 디렉토리는 남겨놓고 나머지만 모두 삭제하도록 구성합니다.

![5](./images/5.png)





