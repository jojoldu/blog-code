# Tidy First?

TDD, XP 의 Kent Beck 의 신간이 출시되었다.  
[Tidy First? - A Personal Exercise in Empirical Software Design](https://www.amazon.com/Tidy-First-Personal-Exercise-Empirical/dp/1098151240), 우리말로는 "먼저 정리" 가 될 것 같다.


> 소프트웨어 디자인은 항상 저에게 지적 퍼즐을 제공했습니다.  
> "이 큰 변화를 한 입에 쏙 들어가는 크기로 축소할 수 있는 디자인은 무엇일까?"라고 궁금해하는 순간이 즐겁습니다.  
> 저에게는 프로그래밍에 가학적인 냄새가 나고, 복잡성의 장작더미 위에서 영웅적인 자기희생을 하는 것 같은 느낌이 듭니다.  
> 세상은 우리 자신과 다른 사람들을 위해 일을 더 쉽게 만들 수 있는 기회를 무시할 수 없을 만큼 충분히 도전적입니다.  

## 파트 1 - 정리

저의 일반적인 학습 전략은 구체적인 것에서 추상적인 것으로 나아가는 것입니다.  
따라서 변경해야 하는 지저분한 코드에 직면했을 때 할 수 있는 작은 디자인 '동작'의 카탈로그부터 시작하겠습니다.
리팩터링에 익숙하신 분들은 동작을 변경하지 않는 구조 변경으로 정의되는 리팩터링과 정리 작업이 매우 유사하다는 것을 알 수 있습니다.  
정리 작업은 리팩터링의 하위 집합입니다.  
정리 작업은 누구도 싫어할 수 없는 귀엽고 흐릿한 작은 리팩터링입니다.
'리팩토링'은 사람들이 기능 개발의 장기적인 중단을 의미하는 용어로 사용하기 시작하면서 치명적인 타격을 입었습니다.  
심지어 "동작을 변경하지 않는다"는 단서 조항도 삭제되어 '리팩토링'은 시스템을 쉽게 망가뜨릴 수 있었습니다.  
새로운 기능도 없고, 손상 가능성도 있으며, 결국에는 아무것도 남지 않겠죠. 

### 1. Guard Clauses

### 2. Dead Code

사용하지 않는 코드는 제거한다.  
필요하면 Git과 같은 버전관리 도구를 통해 과거의 코드를 확인하면 된다.
리플렉션 등의 기술이 광범위하게 사용되면 코드 사용 추적이 쉽지 않다.

### 3. Normalize Symmetries

코드 스타일은 일치화시킨다.
같은 스타일의 코드들이 있어야 그들 사이에서 다시한번 공통점을 추출해서 리팩토링을 할 수 있다.  
다른 스타일간의 코드들이 모여있으면 **같은 로직이여도 공통점을 추출할 수가 없다**.

### 4. New Interface, Old Implementation

호출하고 싶은 인터페이스를 구현하고 호출한다.  
이전 인터페이스를 호출하기만 하면 새 인터페이스를 구현할 수 있다.

통과 인터페이스를 만드는 것은 소프트웨어 설계의 마이크로 스케일 본질이다.  
어떤 동작을 변경하고 싶다고 가정해보자.  
설계가 이렇게 저렇게 되어 있다면 그 변경은 쉬울 것이다.  
그러니 디자인을 그렇게 만든다.

TDD 등이 유용하다.

### 5. Reading Order

### 6. Cohesion Order

### 7. Move Declaration And Initialization Together

### 8. Explaining Variables
### 9. Explaining Constants
### 10. Explicit Parameters
### 11. Chunk Statements
### 12. Extract Helper
### 13. One Pile
### 14. Explaining Comments
### 15. Delete Redundant Comments

## 파트 2 - 매니징

### 16. Separate Tidying
### 17. Chaining
### 18. Batch Sizes
### 19. Rhythm
### 20. Getting Untangled
### 21. First, After, Later, Never

## 파트 3 - 이론

### 22. Beneficially Relating Elements
### 23. Structure And Behavior
### 24. Economics: Time Value And Optionality
### 25. A Dollar Today > A Dollar Tomorrow
### 26. Options
### 27. Options Versus Cash Flows
### 28. Reversible Structure Changes
### 29. Coupling
### 30. Constantine’s Equivalence
### 31. Coupling Versus Decoupling
### 32. Cohesion
### 33. Conclusion