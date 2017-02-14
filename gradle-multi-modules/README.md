# Gradle 멀티 모듈 관리

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
public class DomainApplicationTest {
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
