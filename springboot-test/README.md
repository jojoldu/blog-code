# SpringBoot Test 사용하기
[공식문서](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html) 를 참고하며 기록하는 SpringBoot Test 적용하기
 
## 1. SpringBootTest

## 2. @WebMvcTest

## 3. @DataJpaTest
* SpringBoot에서는 Jpa만 테스트할 수 있도록 별도의 어노테이션을 제공하는데, 이게 ** @DataJpaTest ** 이다.
* 이로 인해 개발의 첫 단계인 Domain 설계 단계에서 불필요한 코드 작성 없이, Entity간의 관계 설정 및 기능 테스트가 가능해졌다.
  - 예를 들자면 View를 만들거나, Controller를 작성하는 것 등등 Domain 설계 확인을 위한 코드 작성
* H2 + JPA + @DataJpaTest = AWESOME!!
* Entity는 아래와 같다
  - 사용자 : Member
  - 글 : Post
  - 댓글 : Comment
* JPA 참고 자료
  - 아라한사님이 [번역하신 공식 문서](http://arahansa.github.io/docs_spring/jpa.html)
  - 김영한님의 [자바 ORM 표준 JPA 프로그래밍](http://www.yes24.com/24/goods/19040233)
  
### 3.1 상황1
* 하나의 글은 여러개의 댓글을 가질 수도 있고, 없을 수도 있다.


### 3.2 상황2
* 한명의 사용자는 하나의 글에 여러개의 댓글을 작성할 수 있다.
* 사용자가 직접 글을 작성할 수는 없다.

### 3.2 상황2
* 사용자는 0개 혹은 다수의 글를 가질수 있다. 대신 Post는 꼭 Member가 있어야 하는것은 아니다.
* 이럴 경우 Member와 Post는 선택적(Optional) 참조이므로 joinColumn 으로는 해결할수가 없다.
* 그래서 조인테이블을 사용해 문제를 해결해야 한다.

### 3.3 상황3
* 사용자는 중복된 글을 가질수 없다. 여러개의 글을 가질순 없지만 고유하게 하나씩 있어야만 한다.
* 이럴 경우 Member.favorites가 List타입일 경우 중복 제거를 위한 비지니스 로직이 추가되어야 한다.
* 중복제거를 로직으로 해결하지말고 자료구조로 해결하자
* List -> Set으로 변경