# SpringBoot Test 사용하기
[공식문서](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)를 참고하며 기록하는 SpringBoot Test 적용하기
> TDD를 기반으로 프로젝트를 시작하는 예제 <br/>
부족함이 많은 예제이다.<br/> 
TDD로 실전 프로젝트를 해본적이 없어 개인적으로 만들 서비스의 예행연습으로 보고 진행함을 먼저 얘기한다.


## 1. @DataJpaTest
* SpringBoot에서 **JPA만 테스트**할 수 있도록 제공하는 어노테이션
* 개발의 첫 단계인 Entity 설계 단계에서 불필요한 코드 작성 없이, Entity간의 관계 설정 및 기능 테스트가 가능해졌다.
  - 예를 들자면 View를 만들거나, Controller를 작성하는 것 등등 **Entity 설계 확인을 위한 코드** 작성이 필요없어졌다.
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
> ManyToOne(다대일) 양방향으로 해결

* Code (자세한 코드는 생략)
```
// Post 클래스
@Entity
public class Post {

    @OneToMany(mappedBy="post", cascade = CascadeType.ALL)
    private List<Comment> comments;
 }  
 
// Comment 클래스
@Entity
public class Comment {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_post"))
    private Post post;

```

* OneToMany(일대다) 를 왜 쓰지 않은걸까? 예를 들어 Comment를 수정해야하는일이 생길 경우

```

// OneToMany(일대다) 단방향
Post post = postRepository.findOne(1L);
List<Comment> comments = post.getComments();
Comment comment = comments.get(0); 
comment.setXXX(); // update

// ManyToOne(다대일) 양방향
Comment comment = commentRepository.findOne(1L);
comment.setXXX(); // update

```

* 즉, 일대다 단방향일 경우 '다'에 속해있는 객체 하나를 수정하기 위해선 '일'을 조회하고 '일' 내부에 있는 '다'에서 원하는 객체를 다시 뽑아내야하는 과정이 필요하다.
* 반변에 다대일 양방향일 경우 수정을 원하는 하위 객체를 바로 수정할 수가 있기 때문에 **다대일 양방향으로 해결하는것을 권장**한다.

### 1.2 상황2
* Member와 Comment간의 관계 설정
* 사용자가 직접 댓글 작성 기능 구현
* 글이 올라오면, 사용자는 해당 글에 댓글을 남길수 있다.
* 한명의 사용자는 여러개의 글에 여러개의 댓글을 작성할 수 있다.
* 사용자 정보 조회시 해당 사용자가 작성한 댓글을 모두 조회할 수 있어야 한다.
> ManyToOne(다대일) 양방향으로 해결 <br/>
OneToMany의 경우 부모, 자식간에 전부 set을 해줘야하는 불편함이 있다. <br/>
객체간 연간관계는 양방향이란게 없기 때문인데, 이를 해결하기 위해 단방향 2개(Comment -> Member와 Member -> Comment)를 사용한것이라고 보면 된다.

* Code (자세한 코드는 생략)
```
// Member 클래스
@Entity
public class Member {

    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    @OrderBy("idx DESC")
    private List<Comment> comments;
 }  
 
// Comment 클래스
@Entity
public class Comment {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_member"))
    private Member member;

```

### 1.3 상황3
* Member와 Post간의 관계 설정
* 사용자별 즐겨찾기 기능 구현
* 사용자는 0개 혹은 다수의 글를 가질수 있다. 
* 글은 꼭 사용자가 있어야 하는것은 아니다.
* 하나의 글은 여러 사용자에 참조 될 수도 있다.
> JoinTable을 통해 해결 <br/>
사용자와 글은 선택적(Optional) 참조이므로 joinColumn 으로는 해결할 수가 없다. 

* Code (자세한 코드는 생략)
```
// Member 클래스
@Entity
public class Member {

    @OneToMany()
    @JoinTable(name="MEMBER_POST",
            joinColumns=@JoinColumn(name="MEMBER_IDX"),
            inverseJoinColumns=@JoinColumn(name="POST_IDX"))
    @OrderBy("idx DESC")
    private List<Post> favorites;
 }  

// Post 클래스는 변경 없음
```

### 1.4 상황4
* ORM에서 컬렉션 사용법
* 사용자는 중복된 글을 가질수 없다. 여러개의 글을 가질순 없지만 고유하게 하나씩 있어야만 한다.
* 이럴 경우 Member.favorites가 List타입일 경우 중복 제거를 위한 비지니스 로직이 추가되어야 한다.
* 중복제거를 로직으로 해결하지말고 자료구조로 해결하자
> Member.favorites를 List에서 Set으로 변경하여 해결  

* Code (자세한 코드는 생략)
```
// Member 클래스
@Entity
public class Member {

    @OneToMany()
    @JoinTable(name="MEMBER_POST",
            joinColumns=@JoinColumn(name="MEMBER_IDX"),
            inverseJoinColumns=@JoinColumn(name="POST_IDX"))
    @OrderBy("idx DESC")
    private Set<Post> favorites;
 }  

// Post 클래스는 변경 없음
```

### 1.5 상황5
* 상속관계 매핑
* Post가 Job, Tech, Essay 라는 3가지 타입으로 분류되도록 해야한다.
* 3타입 모두 가지고 있는 컬럼은 같다. (idx, content, updateDate, comments)
* 객체지향적 코드 작성을 위해 각 클래스는 분리하길 원한다.
> JPA의 상속관계중 단일테이블전략을 사용한다. <br/>
조인전략의 경우 3타입이 서로 다른 컬럼을 1개이상 가지고 있으며, <br/>
앞으로 각각 별도로 컬럼이 추가/삭제 될 가능성이 높은 경우에 고려해볼만 하다. <br/>
하지만 일반적으로 동일한 속성들을 가지고 있는 경우엔 단일 테이블 전략이 더 좋다 <br/>
조회 속도 역시 불필요한 조인이 없어 더 빠르다.

* Code
```
// Post 클래스
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계(단일테이블) 선언
@DiscriminatorColumn(name="DTYPE") // 하위 엔티티들을 구분하는 컬럼명 (default가 DTYPE라서 생략가능, 정보전달을 위해 명시함)
public abstract class Post { }


// Job 클래스 (Tech, Essay 역시 동일함)
@Entity
@DiscriminatorValue("JOB") // DTYPE에 저장될 값
public class Job extends Post { }


// PostRepository 인터페이스
// 제네릭을 사용하여 하위 인터페이스 타입 보장
public interface PostRepository<T extends Post> extends JpaRepository<T, Long>{}


// JobRepository 인터페이스
public interface JobRepository extends PostRepository<Job>{}
```

1번 스탭을 통해 Repository (Dao) 의 기능테스트가 끝이났으니 Controller 구현 & 테스트를 진행해보자

## 2. @WebMvcTest
* DataJpa 어노테이션이 Repository (Dao) 에 대한 테스트라면 WebMvcTest는 Controller을 위한 테스트 어노테이션이다.
* Scan 대상은 아래와 같다.
  - @Controller 
  - @ControllerAdvice
  - @JsonComponent 
  - Filter 
  - WebMvcConfigurer and HandlerMethodArgumentResolver

* MockMvc를 자동으로 지원하고 있어 별도의 HTTP 서버 없이 Controller 테스트를 진행할 수 있다.
* 사용법 역시 간단하다.
```

@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class WebMvcTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void test_샘플() throws Exception {
        this.mvc.perform(get("/hello").accept(MediaType.TEXT_PLAIN)) // /hello 라는 url로 text/plain 타입을 요청
                .andExpect(status().isOk()) // 위 요청에 따라 결과가 status는 200이며
                .andExpect(content().string("Hello World"));  // response body에 "Hello World" 가 있는지 검증
    }
}

```
