# SpringBoot 1.4 Test 사용하기
[공식문서](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)를 참고하며 기록하는 SpringBoot Test 적용하기
> TDD를 기반으로 프로젝트를 시작하는 예제 <br/>
1. 테스트 코드를 통해 Entity와 Dao 구현 <br/>
2. 테스트 코드를 통해 Controller 구현 <br/> 
3. 테스트 코드를 통해 Oauth 인증 구현 <br/>
TDD로 실전 프로젝트를 해본적이 없어 개인적으로 만들 서비스의 예행연습으로 보고 진행함을 먼저 얘기한다.

## 1. @DataJpaTest
* SpringBoot에서 **JPA만 테스트**할 수 있도록 제공하는 어노테이션
* 개발의 첫 단계인 Entity 설계 단계에서 불필요한 코드 작성 없이, Entity간의 관계 설정 및 기능 테스트가 가능해졌다.
  - 예를 들자면 View를 만들거나, Controller를 작성하는 것 등등 **Entity 설계 확인을 위한 코드** 작성이 필요없어졌다.
* 사용법은 간단하다.
```
@RunWith(SpringRunner.class)  //Junit 테스트 선언
@DataJpaTest // DataJpaTest 선언
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
}

 @Test
 public void test_Post와Comment관계정의() throws Exception {
    Post savedPost = postRepository.save(post);
    savedPost.addComment(comment); // 글에 댓글 추가
    
    comment.setPost(savedPost);
    commentRepository.save(comment); // 댓글에 글 추가

    Post firstPost = postRepository.findOne(1L);
    Comment firstComment = commentRepository.findOne(1L);

    assertThat(savedPost.getContent(), is("content"));
    assertThat(savedPost.getComments().get(0).getContent(), is(firstComment.getContent()));
}
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
> 상황 1.1과 동일한 ManyToOne(다대일) 양방향으로 해결 <br/>
객체간 연간관계는 **양방향이란게 없기 때문**에, 이를 해결하기 위해 단방향 2개(Comment -> Member와 Member -> Comment)를 사용한것이라고 보면 된다.

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
}

// Test 코드
@Test
public void test_Member와Comment관계정의() throws Exception {
    Post savedPost = postRepository.save(post);
    Member savedMember = memberRepository.save(member);

    savedPost.addComment(comment);
    savedMember.addComment(comment);

    comment.setPostAndMember(savedPost, savedMember);

    commentRepository.save(comment);

    Post afterPost = postRepository.findOne(1L);
    Member afterMember = memberRepository.findOne(1L);

    assertThat(afterPost.getComments().get(0).getContent(), is("댓글"));
    assertThat(afterMember.getComments().get(0).getContent(), is("댓글"));
    assertThat(commentRepository.findAll().size(), is(1)); // savedPost와 savedMember에 각각 addComment를 했지만 결국 comment는 1개가 들어간것을 확인
}
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

// Test 코드
@Test
public void test_Post와Member관계정의() throws Exception {
    Member member2 = new Member("test@gmail.com", new ArrayList<>(), new LinkedHashSet<>());
    Post savedPost = postRepository.save(post);
    member.addPost(savedPost);
    member2.addPost(savedPost);

    Member savedMember = memberRepository.save(member);
    Member savedMember2 = memberRepository.save(member2);

    assertThat(savedMember.getFavorites().stream().findFirst().orElse(new Post()).getContent(), is("content")); // 1번 사용자의 1번 글이 post인지 확인
    assertThat(savedMember2.getFavorites().stream().findFirst().orElse(new Post()).getContent(), is("content")); // 2번 사용자의 1번 글이 post인지 확인
}
```

### 1.4 상황4
* ORM에서 컬렉션 사용법
* 사용자는 중복된 글을 가질수 없다. 여러개의 글을 가질순 없지만 고유하게 하나씩 있어야만 한다.
* 이럴 경우 Member.favorites가 List타입일 경우 중복 제거를 위한 비지니스 로직이 추가되어야 한다.
* 중복제거를 로직으로 해결하지말고 **자료구조로 해결**하자
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

// Test 코드
@Test
public void test_oneToMany에서Set과List차이() throws Exception {
    Post savedPost = postRepository.save(post);
    member.addPost(savedPost);
    member.addPost(savedPost);

    Member savedMember = memberRepository.save(member);

    assertThat(savedMember.getFavorites().size(), is(1)); // 2개의 Post를 넣었지만 결국 중복된게 제거되서 1개만 등록된것을 확인할수 있다.
}
```

### 1.5 상황5
* 상속관계 매핑
* Post가 Job, Tech, Essay 라는 3가지 타입으로 분류되도록 해야한다.
* 3타입 모두 가지고 있는 컬럼은 같다. (idx, content, updateDate, comments)
* 객체지향적 코드 작성을 위해 각 클래스는 분리하길 원한다.
> JPA의 상속관계중 단일테이블전략을 사용한다. <br/>
조인전략의 경우 3타입이 서로 다른 컬럼을 1개이상 가지고 있으며, 차후 별도로 컬럼이 추가/삭제 될 가능성이 높은 경우에 고려해볼만 하다. <br/>
하지만 일반적으로 동일한 속성들을 가지고 있는 경우엔 단일 테이블 전략이 더 좋다 <br/>
조회 속도 역시 불필요한 조인이 없어 일반적으로 더 빠르다.

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


// Test 코드
@Test
public void test_상속관계() throws Exception {
    jobRepository.save(new Job("잡플래닛", LocalDateTime.now(), new ArrayList<>()));
    techRepository.save(new Tech("OKKY", LocalDateTime.now(), new ArrayList<>()));
    essayRepository.save(new Essay("임백준", LocalDateTime.now(), new ArrayList<>()));

    Job savedJob = jobRepository.findAll().get(0);
    Tech savedTech = techRepository.findAll().get(0);
    Essay savedEssay = essayRepository.findAll().get(0);

    assertThat(savedJob.getContent(), is("잡플래닛"));
    assertThat(savedTech.getContent(), is("OKKY"));
    assertThat(savedEssay.getContent(), is("임백준"));
}
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

### 2.1 상황1
* 호출한 URL의 View를 검증한다.
* 시작페이지 구성을 위해 "/" 를 요청하면 home.ftl을 전달하는지 테스트한다.
* code
```
    @Test
    public void test_View검증() throws Exception {
        this.mvc.perform(get("/")) // /로 url 호출
                .andExpect(status().isOk()) // 위 요청에 따라 결과가 status는 200이며
                .andExpect(view().name("home"));  // 호출한 view의 이름이 home인지 확인 (확장자는 생략)
    }
```

### 2.2 상황2
* 호출한 URL의 View와 Model 데이터를 검증한다.
* 아직 Service Layer의 상세스펙은 나온 상태가 아니다.
* 시작페이지 구성을 위해 "/" 를 요청하면 home.ftl과 Job, Tect, Essay List를 전달하는지 테스트한다.
* code
```
// service의 상세스펙이 나오지 않았으니 인터페이스로 service를 먼저 선언한다.
public interface PostService {

    List<Job> getJobList();
    List<Tech> getTechList();
    List<Essay> getEssayList();
}

    // 테스트 코드
    
    @MockBean // postService에 가짜 Bean을 등록
    private PostService postService;
    
    @Test
    public void test_Model검증및ServiceMocking() throws Exception {
        Job[] jobs = {new Job("잡플래닛", LocalDateTime.now(), new ArrayList<>())};
        Tech[] techs = {new Tech("OKKY", LocalDateTime.now(), new ArrayList<>())};
    
        given(this.postService.getJobList()) // this.postService.getJobList 메소드를 실행하면
                .willReturn(Arrays.asList(jobs)); // Arrays.asList(jobs) 를 리턴해줘라.
    
        given(this.postService.getTechList())
                .willReturn(Arrays.asList(techs));
    
        given(this.postService.getEssayList())
                .willReturn(new ArrayList<>());
    
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("jobs")) // model에 "jobs" 라는 key가 존재하는지 확인
                .andExpect(model().attribute("jobs", IsCollectionWithSize.hasSize(1))) // jobs model의 size가 1인지 확인
                .andExpect(model().attribute("techs", contains(techs[0]))) // techs model이 "OKKY" 라는 객체를 가지고 있는지 확인
                .andExpect(model().attribute("essays", is(empty()))); // 빈 Collection인지 확인
    }
```

### 2.3 상황3
* Job 데이터를 입력받는다.
* 아직 Service Layer의 상세스펙은 나온 상태가 아니다.
* Job 스펙에 맞춰 content 가 parameter에 포함된다. 
* code
```

```

### 2.4 상황4
* Job 데이터 조회시 데이터가 없을 경우 NotFound Exception을 발생시킨다.
* NotFoundExcption은 Job/Tech/Essay 만을 나타낼수 있도록, PostNotFoundException 이라는 새로운 Exception으로 처리한다.
* code
```
// PostNotFound Exception
@ResponseStatus(HttpStatus.NOT_FOUND) // 404 NOT_FOUND status
public class PostNotFoundException extends RuntimeException{ // 직접 생성한 Exception

    public PostNotFoundException(long idx) {
        super("could not find post '" + idx + "'.");
    }
}

    // Controller의 메소드
    @RequestMapping(value="/job/{idx}")
    public Job getJob(@PathVariable long idx) {
        return Optional.ofNullable(this.postService.getJob(idx))
                .orElseThrow(() -> new PostNotFoundException(idx)); // this.postService.getJob(idx)가 null일 경우 PostNotFoundException 발생
    }
    
    //테스트 코드
    @Test
    public void test_Exception체크() throws Exception {
        given(this.postService.getJob(1)) // getJob 메소드에 인자값 1이 입력될 경우
                .willReturn(null); // exception 발생을 위해 null 리턴

        mvc.perform(get("/job/1")) // /job/1 을 호출할 경우
                .andExpect(status().isNotFound()); // not found exception이 나오는지 아닌지 테스트
    }    

```