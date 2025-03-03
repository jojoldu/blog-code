# 데이터 지향 프로그래밍 (DOP)

> 역자이신 성철님의 코멘트처럼 
> "OOP 개발자에게는 FP를 다른 이름으로 포장해 설명하면서 지금까지 쌓아온 OOP를 섣불리 무너뜨리려는 것으로 보일테고",
> "FP 개발자에게는 잘못된 유사 FP를 가르치는 시도로 보일 것"
> 이 책은 OOP와 FP 둘 다를 익힌 사람이 아니라면 이도저도 아닌 책으로 읽힐 수 있다.

- 복잡한 문제는 기존의 OOP를 활용해 풀고, 단순한 문제는 단순하게 풀기
  - 도메인 모델은 객체지향 모델링을 적용하고, 외부와 통신하는 게층은 DOP를 적용
  - 경계를 구분할때는 OOP의 캡슐화를 적극적으로 활용하고, 세부 구현에는 DOP를 적용
- 상속 계층이 깊거나 클래스 내부에 은닉되는 구현이 복잡하고 클수록 OOP는 장점이 부각
  - 반면 상속 깊이가 얕거나 내부 구현이 단순할 때 OOP는 비용만 많이 들고 별 도움이 안된다고 느껴질 수 있음

## 1부

DOP에 따르면 기존의 많은 OOP 시스템에 내재된 복잡성의 주요 근원은 다음과 같다.

- 코드와 데이터가 섞여 있다.
  - 클래스가 많은 관계에 연루되는 편이다.
- 객체가 변경 가능하다.
  - 코드를 읽을때 더 많은 생각이 필요하다.
  - 다중 스레드 환경에서 명시적 동기화가 필요하다.
- 데이터가 멤버로 객체에 고정된다.
  - 데이터 직렬화가 쉽지 않다.
- 코드는 메서드로 클래스에 고정된다.
  - 클래스 계층 구조가 복잡하다.

이 분석 내용은 FP가 기존 OOP에 대해 갖는 생각과 비슷하다.  
그러나 이 책에서 언급하는, DOP가 시스템 복잡도를 줄이려고 취하는 데이터 접근법은 FP 방식과 다르다.


## 요약

1.	전통적 OOP의 한계:
OOP에서는 데이터와 행동(메서드)이 클래스로 밀접하게 연결되어 있다. 이는 상태 변경, 상속 구조 복잡성, 캡슐화로 인한 데이터 접근 어려움 등의 문제를 야기하고, 결국 시스템의 유연성과 유지보수성을 떨어뜨릴 수 있다.

2.	데이터를 데이터로 다루기:
DOP의 핵심 아이디어는 데이터와 로직을 명확히 분리하는 것이다. 데이터는 가능한 한 불변(immutable)으로 유지하고, 별도의 함수나 메서드로 데이터 변환(또는 처리) 로직을 독립시킨다. 이렇게 하면 데이터 구조가 복잡한 클래스 계층이 아니라 단순한 자료 형태(예: Map, Record 등)로 존재하며, 로직은 필요할 때만 이 자료들을 다룬다.

3.	데이터 구조의 단순화와 표현성 향상:
DOP에서는 복잡한 객체 그래프 대신, 계층적이고 단순한 자료 구조(예: 중첩된 맵, JSON 형태)로 데이터를 표현한다. Java 14+의 Records나 더 단순한 POJO(Plain Old Java Object), 혹은 불변 컬렉션과 같은 구조를 활용하면, 데이터 정의가 간결해지고 변경 추적이 쉬워진다.

4.	불변성(Immutable) 추구:
데이터 구조를 불변으로 설계하면 동시성(concurrency) 문제를 크게 완화하고, 사이드 이펙트(side effect)를 줄이며, 디버깅과 테스트를 용이하게 만든다. 불변 컬렉션이나 Record를 사용함으로써 데이터를 읽기 전용으로 다루고, 필요할 때만 새로운 변형된 사본을 반환하는 식으로 설계를 단순화할 수 있다.

5.	유연한 로직 구현:
데이터 조작 로직은 별도의 함수나 서비스 레이어에 위치해 데이터를 받아 가공한 뒤 결과를 반환하는 식으로 구성된다. 이는 함수형 프로그래밍과 유사하며, 기존 OOP의 복잡한 상속이나 다형성 대신 단순한 함수 조합으로 문제를 해결할 수 있게 한다.

6.	점진적 도입과 상호운용성:
완전히 OOP를 포기할 필요는 없으며, DOP 개념을 점진적으로 적용해볼 수 있다. 특정 도메인 모델을 Records로 재정의하거나, 기존 OOP 코드베이스 내에서 데이터 처리 로직을 분리하고 불변 데이터 구조를 도입하는 등의 방법으로 서서히 전환할 수 있다.


구조는 객체 지향, 로직은 함수형으로
복잡한 데이터 구조나 계층적 모델링이 필요할 때는 객체 지향 방식을 활용하여 직관적이며 논리적인 형태로 데이터를 표현한다. 반면, 데이터 변환이나 리스트 처리 같은 순수 로직 문제는 함수형 패러다임으로 해결하여 예측 가능하고 안정적인 로직 구현을 가능하게 한다. 이를 통해 데이터 구조와 처리 로직을 명확히 분리할 수 있으며, 궁극적으로 코드 이해도와 유지보수성이 향상된다.

데이터를 명확한 구조로 표현하고 상태 변화 제어하기
객체를 통해 데이터(상태)를 명확히 구조화하고, 해당 상태를 다루는 로직을 별도로 유지하면, 데이터 변경을 추적하고 제어하기 용이하다. 이는 데이터에 대한 불변성 추구나, 불변 상태를 기반으로 한 함수형 변환과 자연스럽게 어우러져 안정적인 코드 흐름과 높은 이해도를 확보하는 데 도움이 된다.