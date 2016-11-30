# AOP 시작하기
현재 신입사원 분들의 입사로 Spring에서 중요한 개념들에 대해 한번 정리하려고 작성하게 되었습니다. <br/>
Spring의 가장 중요한 개념 중 하나인 AOP를 제 나름의 이해로 정리할 예정입니다. 틀린 내용이 있다면 가감 없이 풀리퀘나 댓글 부탁드리겠습니다.<br/>
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review)를 star 하시면 실시간으로 feed를 받을 수 있습니다.)

### 문제 상황

**build.gradle**
```
buildscript {
	ext {
		springBootVersion = '1.4.2.RELEASE'
	}
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
	}
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'

jar {
	baseName = 'aop'
	version = '0.0.1-SNAPSHOT'
}
sourceCompatibility = 1.8
targetCompatibility = 1.8

repositories {
	mavenCentral()
}


dependencies {
	compile('org.springframework.boot:spring-boot-starter-aop')
	compile('org.springframework.boot:spring-boot-starter-web')
	compile('org.springframework.boot:spring-boot-starter-data-jpa')

	runtime('org.springframework.boot:spring-boot-devtools')
	runtime('com.h2database:h2')

	testCompile('org.springframework.boot:spring-boot-starter-test')
}

```

**Board.java**
```
@Entity
public class Board {

    @Id
    @GeneratedValue
    private Long idx;

    @Column
    private String title;

    @Column
    private String content;

    public Board() {
    }

    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
```

**BoardService.java**
```
@Service
public class BoardService {

    @Autowired
    private BoardRepository repository;

    public List<Board> getBoards() {
        return repository.findAll();
    }
}
```

**BoardRepository.java**
```
@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{}
```

**Application.java**
```
@SpringBootApplication
@RestController
public class Application implements CommandLineRunner{

	@Autowired
	private BoardService boardService;

	@Autowired
	private BoardRepository boardRepository;

	@Override
	public void run(String... args) throws Exception {
		for(int i=1;i<=100;i++){
			boardRepository.save(new Board(i+"번째 게시글의 제목", i+"번째 게시글의 내용"));
		}
	}

	@GetMapping("/")
	public List<Board> getBoards() {
		return boardService.getBoards();
	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
```
### AOP란?
Spring의 핵심 개념중 하나인 DI가 애플리케이션 모듈들 간의 결합도를 낮춰준다면, AOP는 **애플리케이션 전체에 걸쳐 사용되는 기능을 재사용**하도록 지원하는 것입니다. <br/>  
AOP (Aspect-Oriented Programming) 란 단어를 번역하면 **관점(관심) 지향 프로그래밍**으로 됩니다. <br/>
이 관점(관심)이란 단어가 잘 와닿지 않아 AOP를 이해하는데 있어 더 어려움을 일으킨다고 생각하였습니다. <br/>


* OOP : 비지니스 로직의 모듈화
  - 모듈화의 핵심 단위는 비지니스 로직
* AOP : 시스템 인프라 단위의 모듈화
  - 대표적 예 : 로깅, 트랜잭션, 보안 등
  - 각각의 모듈들의 주 목적 외에 필요한 부가적인 기능들 
  
AOP라고 해서 전에 없던 새로운 개념이 아닙니다. 결국은 **공통된 기능을 재사용하는 기법**입니다. <br/>
일반적으로 공통된 기능을 재사용하는 방법으로 상속이나 위임을 사용합니다. <br/>
하지만 전체 어플리케이션에서 여기저기에서 사용되는 **부가기능**들을 상속이나 위임으로 처리하기에는 깔끔하게 모듈화가 어렵습니다. <br/>
(일례로 트랜잭션을 생각해보시면 됩니다. JDBC 커넥션을 오픈하고 정상적 처리후엔 커밋, 예외발생시엔 롤백, 끝나면 커넥션 종료등과 같은 처리를 
상속과 위임의 개념만으로 해결하려고 하면 깔끔한 모듈화가 되기에 어려움이 많습니다.) <br/>

AOP의 장점은 2가지입니다. <br/>

* 어플리케이션 전체에 흩어진 공통 기능이 하나의 장소에서 관리된다는 점
* 다른 서비스 모듈들이 본인의 목적에만 충실하고 그외 사항들은 신경쓰지 않아도 된다는 점

### AOP 용어
**애스펙트 (Aspect)** <br/>

**어드바이스 (Advice)** <br/>
일종의 부가기능을 담은 클래스를 얘기합니다. <br/>

**조인포인트 (JoinPoint)** <br/>
어드바이스가 적용될 수 있는 위치를 얘기합니다. <br/>

**포인트컷 (PointCut)** <br/>

**인트로덕션 (Introduction)** <br/>
**위빙 (Weaving)** <br/>
지정된 객체에 애스팩트를 적용해서 새로운 프록시 객체를 생성하는 과정을 얘기합니다. <br/>
예를 들면 A라는 객체에 트랜잭션 애스팩트가 지정되어 있다면, A라는 객체가 실행되기전 커넥션을 오픈하고 실행이 끝나면 커넥션을 종료하는 기능이 추가된 프록시 객체가 생성되고, 
이 프록시 객체가 앞으로 A 객체가 호출되는 시점에서 사용됩니다. 이때의 프록시객체가 생성되는 과정을 **위빙**이라 생각하시면 됩니다. <br/> 
컴파일 타임, 클래스로드 타임, 런타임과 같은 시점에서 실행되지만, Spring AOP는 런타임에서 프록시 객체가 생성 됩니다. <br/>

### Spring의 AOP
AOP 프레임워크는 스프링 외에도 여러가지가 있지만 Spring의 AOP를 사용할 것이니 Spring의 AOP에 대해서만 이야기하겠습니다. <br/>

* 런타임에 어드바이스를 생성
  - 프록시 객체는 타겟 객체로 위장해서 해당 메소드의 호출을 가초래고, 지정된 행위를 수행후, 타겟 객체로 호출을 전달
* Spring은 메소드 조인 포인트만 지원

### 실습
