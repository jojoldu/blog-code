# 일급 컬렉션

안녕하세요?  
이번 시간에는 일급 컬렉션 (First Class Collection) 에 대해 배워보겠습니다.  
  
일급 컬렉션이란 단어는 [소트웍스 앤솔로지](https://wikibook.co.kr/thoughtworks-anthology/) 의 객체지향 생활체조 파트에서 언급이 되었습니다.  

> 규칙 8: 일급 콜렉션 사용
이 규칙의 적용은 간단하다. 
콜렉션을 포함한 클래스는 반드시 다른 멤버 변수가 없어야 한다. 
각 콜렉션은 그 자체로 포장돼 있으므로 이제 콜렉션과 관련된 동작은 근거지가 마련된셈이다.
필터가 이 새 클래스의 일부가 됨을 알 수 있다. 
필터는 또한 스스로 함수 객체가 될 수 있다. 
또한 새 클래스는 두 그룹을 같이 묶는다든가 그룹의 각 원소에 규칙을 적용하는 등의 동작을 처리할 수 있다. 
이는 인스턴스 변수에 대한 규칙의 확실한 확장이지만 그 자체를 위해서도 중요하다. 
콜렉션은 실로 매우 유용한 원시 타입이다. 
많은 동작이 있지만 후임 프로그래머나 유지보수 담당자에 의미적 의도나 단초는 거의 없다. - 소트웍스 앤솔로지 객체지향 생활체조편

간단하게 설명드리자면, 아래의 코드를

```java
Map<String, String> map = new HashMap<>();
map.put("1", "A");
map.put("2", "B");
map.put("3", "C");
```

Wrapping 합니다.

```java
public class GameRanking {

    private Map<String, String> ranks;

    public GameRanking(Map<String, String> ranks) {
        this.ranks = ranks;
    }
}
```

위와 같이 **Collection을 Wrapping**하면서, **그외 다른 멤버 변수가 없는 상태**를 일급 컬렉션이라 합니다.



Wrapping 함으로써 다음과 같은 이점이 있습니다.

* Collection과 Collection에 필요한 기능을 **함께 관리** 할 수 있다
* Collection의 **불변성**을 보장
* Collection에 이름을 붙일 수있다

하나 하나 설명드리겠습니다.


## 값과 로직이 함께

이 부분은 예전에 소개 드린 [Enum](http://woowabros.github.io/tools/2017/07/10/java-enum-uses.html)의 장점과도 일맥상통합니다.  

## 불변

일급 컬렉션은 **컬렉션의 불변을 보장**합니다.  

여기서 ```final```을 사용하면 안되나요?  라고 하시는 분들이 계신데요.  
Java의 ```final```은 정확히는 불변을 만들어주는 것은 아니며, **재할당만 금지** 합니다.

```

```