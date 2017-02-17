# Gradle 멀티 프로젝트 관리
안녕하세요! 이번 시간에는 아시는 분들은 거의다 아시는(!?) Gradle을 이용한 멀티 프로젝트(모듈) 관리에 대해 소개하려고 합니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/java/enum-mapper)에 있으니 참고하셔서 보시면 더 좋으실 것 같습니다.  
(공부한 내용을 정리하는 [blog-code](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [review](https://github.com/jojoldu/review), 이걸 모두 정리하는 [블로그](http://jojoldu.tistory.com/)가 있습니다.)<br/>  

대부분의 서비스는 단일 프로젝트로 구성되는 일이 거의 없습니다.  
아무리 작게 구성해도 일정 수준 이상의 트래픽을 감당하려면 사용자와의 접점을 담당하는 서버(이하 web프로젝트라고 하겠습니다.), DB와의 접점을 담당하는 서버(api프로젝트라 칭하겠습니다.)로 구분하여 구성하게 됩니다.  
이럴 경우 고민이 되는 것이 그럼 **web과 api 모두에서 사용되는 클래스들은 어떻게 다룰 것인가** 입니다.  

![서버구성](./images/서버구성.png)  

(공통으로 사용되는 클래스를 Member클래스라 칭하겠습니다.)  

가장 쉽고 흔한 방법은 **복사 & 붙여넣기**입니다.  
한 프로젝트에서 Member 클래스파일을 생성하고 이를 다른 프로젝트에서 코드를 복사하는 방식입니다.  
하지만 이럴 경우 연동되는 프로젝트가 늘어날 경우, 혹은 Member 클래스의 코드에 수정이 필요할 경우에 정말 **많은 양을 수정해야하고 실수할 여지가 많아집니다**.  

이번 주제는 바로 이 문제를 어떻게 하면 해결할 수 있을까 라는 의문에서 시작됩니다.  
그럼 이제 본격적으로 문제를 해결해보겠습니다.

### 예시코드

### member-domain

**Member.java**  

```
@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    public Member() {}

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
```


member-domain 프로젝트는 단독으로 구동시킬 프로젝트가 아니기 때문에 spring context를 불러올 수 없습니다.   그래서 SpringBoot Test를 구동시킬 경우 아래와 같은 에러가 발생합니다.  

![Spring Context 불러오기 실패](./images/context불러오기실패.png)  

그래서 아래처럼 메인 테스트 클래스에 ```@SpringBootApplication```을 추가하였습니다.     

**DomainApplicationTest.java**  
```
@SpringBootApplication
public class ModuleCommonApplicationTest {
}

```


**MemberRepositoryTest.java**  
```
@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void save() {
        memberRepository.save(new Member("jojoldu", "jojoldu@gmail.com"));
        Member saved = memberRepository.findAll().get(0);
        assertThat(saved.getName(), is("jojoldu"));
    }
}

```

[권남님의 위키](http://kwonnam.pe.kr/wiki/gradle/multiproject)
[Gradle 레퍼런스](https://docs.gradle.org/current/userguide/multi_project_builds.html)
