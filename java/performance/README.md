# Java SE 성능 비교
Java 8을 기준으로 진행하였다. <br/>
사내 신규 프로젝트가 전부 8로 진행되기도 하였고, 조금 있으면 9가 나오는 상황에서 6,7을 기준으로 작성
### Exception (예외처리)
우리가 예외처리를 하는 방법은 다양하다. <br/>


![Exception 코드](./images/exception코드.png)

첫번째 메소드의 경우 i가 홀수일 경우 null pointer exception을 throw 하여 바깥부분에서 catch하도록 하였다. <br/>
두번째 메소드의 경우 null pointer exception이 발생할 부분인 list.add() 를 try catch로 감싸도록 하였다. <br/>
마지막 메소드의 경우 null pointer exception이 발생하지 않도록 방어 코딩을 하였다. <br/>


![Exception 성능비교](./images/exception결과.png)

위 결과를 보면 흥미로운 사실이 몇가지 보인다. <br/>
코드에서 직접 NullPointerException을 보내는것(첫번째 예제)과 JVM에서 NullPointerException을 보내는 것(두번째 예제)에는 큰 차이가 난다. <br/>
즉, JVM은 필요할때마다 새로 생성하지 않고, 동일한 Exception 객체를 재사용 한다는 것이다. <br/>

하지만 예외처리의 방법에 따른 성능상 이슈는 생각보다 크지 않다. <br/>
결과에서 보다시피 10만번을 수행하는데 들어간 시간은 100ms 이상 소모되지 않는다. <br/>
그렇다 하더라도 불필요한 예외처리는 비용이 발생하므로, 적절하게 사용하며 방어코드를 사용하는 것이 좀더 비용이 덜드는 방식임을 확인할 수 있다. <br/>

### String (문자열) 처리
문자열 처리의 경우 Java Compiler에서 내부적으로 StringBuilder와 StringBuffer로 변환시켜 연산한다는 것을 많은 개발자들이 알고 있다. <br/>
그래서 최근들어 "StringBuffer를 사용하나, + 연산자를 사용하나 내부 연산이 같다" 라는 이유로 + 연산자를 막쓰는 분들을 보게 된다. <br/>
그래서 정확히 어떻게 차이가 나는지 보여주기 위한 예제이다.
