# SpringBoot Test 사용하기
[공식문서](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html) 를 참고하며 기록하는 SpringBoot Test 적용하기
> TDD를 기반으로 프로젝트를 시작하는 예제 <br/>
부족함이 많은 예제이다.<br/> 
TDD로 실전 프로젝트를 해본적이 없어 개인적으로 만들 서비스의 예행연습으로 보고 진행함을 먼저 얘기한다.


## 1. @DataJpaTest
* SpringBoot에서 **JPA만 테스트**할 수 있도록 제공하는 어노테이션
* 개발의 첫 단계인 Domain 설계 단계에서 불필요한 코드 작성 없이, Entity간의 관계 설정 및 기능 테스트가 가능해졌다.
  - 예를 들자면 View를 만들거나, Controller를 작성하는 것 등등 **Domain 설계 확인을 위한 코드** 작성이 필요없어졌다.
* 사용법은 간단하다.
```
@RunWith(SpringRunner.class) 
@DataJpaTest 
public class DataJpaTest {

    /* 
        Repository == Dao
        본인이 테스트하려는 Dao를 선언하고 기능을 테스트 하면 된다.
        아래는 여기 프로젝트에서 사용한 코드의 일부이다.
    */
    
    @Autowired
    private MemberRepository memberRepository; 

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;
    
}
```
* H2 + JPA + @DataJpaTest = AWESOME!!
* 단위 테스트가 끝날때 마다 자동으로 DB를 롤백시켜 준다 (우앙!)
* 여기에서 사용할 예제 Entity는 아래와 같다
  - 사용자 : Member
  - 글 : Post
  - 댓글 : Comment
* JPA 참고 자료
  - 아라한사님이 [번역하신 공식 문서](http://arahansa.github.io/docs_spring/jpa.html)
  - 김영한님의 [자바 ORM 표준 JPA 프로그래밍](http://www.yes24.com/24/goods/19040233)
  
### 1.1 상황1
* Post와 Comment간의 관계 설정
* 하나의 글은 여러개의 댓글을 가질 수도 있고, 없을 수도 있다.
* 하나의 글을 조회하면 해당하는 댓글이 같이 와야 한다.
> OneToMany 단방향으로 해결 <br/>
댓글(Many)은 글(One) 없이 존재할수 없으므로 단방향으로 처리 가능. <br/>


### 1.2 상황2
* Member와 Comment간의 관계 설정
* 글이 올라오면, 사용자는 해당 글에 댓글을 남길수 있다.
* 한명의 사용자는 여러개의 글에 여러개의 댓글을 작성할 수 있다.
* 사용자가 직접 글을 작성할 수는 없다.
* 사용자 정보 조회시 해당 사용자가 작성한 댓글을 모두 조회할 수 있어야 한다.
> OneToMany 양방향으로 해결 <br/>
OneToMany의 경우 부모, 자식간에 전부 set을 해줘야하는 불편함이 있다. <br/>
객체간 연간관계는 양방향이란게 없기 때문인데, 이를 해결하기 위해 단방향 2개(Comment -> Member와 Member -> Comment)를 사용한것이라고 보면 된다.

### 1.3 상황3
* Member와 Post간의 관계 설정
* 사용자는 0개 혹은 다수의 글를 가질수 있다. 
* 글은 꼭 사용자가 있어야 하는것은 아니다.
* 하나의 글은 여러 사용자에 참조 될 수도 있다.
> JoinTable을 통해 해결 <br/>
사용자와 글은 선택적(Optional) 참조이므로 joinColumn 으로는 해결할수가 없다.

### 1.4 상황4
* ORM에서 컬렉션 사용법
* 사용자는 중복된 글을 가질수 없다. 여러개의 글을 가질순 없지만 고유하게 하나씩 있어야만 한다.
* 이럴 경우 Member.favorites가 List타입일 경우 중복 제거를 위한 비지니스 로직이 추가되어야 한다.
* 중복제거를 로직으로 해결하지말고 자료구조로 해결하자
> Member.favorites를 List에서 Set으로 변경하여 해결  


## 2. @WebMvcTest

