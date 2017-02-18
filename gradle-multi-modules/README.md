# Gradle 멀티 프로젝트 관리
안녕하세요! 이번 시간에는 아시는 분들은 거의다 아시는(!?) Gradle을 이용한 멀티 프로젝트(모듈) 관리에 대해 소개하려고 합니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/gradle-multi-modules)에 있으니 참고하셔서 보시면 더 좋으실 것 같습니다.  
(공부한 내용을 정리하는 [blog-code](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [review](https://github.com/jojoldu/review), 이걸 모두 정리하는 [블로그](http://jojoldu.tistory.com/)가 있습니다.)<br/>  

대부분의 서비스는 단일 프로젝트로 구성되는 일이 거의 없습니다.  
아무리 작게 구성해도 일정 수준 이상의 트래픽을 감당하려면 사용자와의 접점을 담당하는 서버(이하 web프로젝트라고 하겠습니다.), DB와의 접점을 담당하는 서버(api프로젝트라 칭하겠습니다.)로 구분하여 구성하게 됩니다.  
이럴 경우 고민이 되는 것이 그럼 **web과 api 모두에서 사용되는 클래스들은 어떻게 다룰 것인가** 입니다.  

![서버구성](./images/서버구성.png)  

(공통으로 사용되는 클래스를 Member클래스라 칭하겠습니다.)  

가장 쉽고 흔한 방법은 **복사 & 붙여넣기**입니다.  
한 프로젝트에서 Member 클래스파일을 생성하고 이를 다른 프로젝트에서 코드를 복사하는 방식입니다.  
하지만 이럴 경우 연동되는 프로젝트가 늘어날 경우, 혹은 Member 클래스의 코드에 수정이 필요할 경우에 정말 **많은 양을 수정해야하고 실수할 여지가 많아집니다**.  

어떻게 하면 이 문제를 해결할 수 있을까요?  
하나의 공통 프로젝트를 두고, 이 프로젝트를 여러 프로젝트에서 가져가서 사용할 수 있으면 좋지 않을까요?  
위 처럼 해결하려면 몇가지 조건이 수반됩니다.
* 개발시에는 바로바로 공통 프로젝트 코드를 사용할 수 있어야 한다.
* 빌드시에는 자동으로 공통 프로젝트가 포함되어야 한다.

Gradle의 Multi Module 방식을 사용하여 문제를 해결해보겠습니다.

### 예제코드
프로젝트 구조는 root 프로젝트인 gradle-multi-modules란 프로젝트와 그 하위 프로젝트(혹은 모듈)들인 module-api, module-web, module-common 으로 구성합니다.  

![프로젝트 구조](./images/프로젝트구조.png)  

빌드는 항상 root 프로젝트를 기준으로 진행할 예정이기에 gradle-multi-modules 외에 나머지 프로젝트에는 gradle폴더, gradlew등의 파일이 없으며 build.gradle과 src폴더만 존재합니다.  
module-common이 다른 프로젝트에서 사용할 중요한 혹은 공통 클래스를 모아놓은 프로젝트라고 생각하시면 됩니다.  
공통으로 사용할 ```Member.java```와 ```MemberRepository.java```파일을 만들겠습니다.  

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

    public Member() {
    }

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

**MemberRepository.java**  
```
public interface MemberRepository extends JpaRepository<Member, Long> {
}
```

module-common의 build.gradle은 아래와 같습니다.  
```
dependencies {
	compile('org.springframework.boot:spring-boot-starter-data-jpa')
	runtime('com.h2database:h2')
	testCompile('org.springframework.boot:spring-boot-starter-test')
}
```
그럼에도 Entity 클래스를 작성하고, 이 Entity 클래스의 repository, 그리고 간단한 repository test까지는 가능해야 하기 때문에 위와 같이 의존성을 추가하였습니다.
이외 설정은 **현재는 추가하지 않습니다**.

그럼 이제 다음 프로젝트(모듈)인 module-api의 코드를 작성해보겠습니다.  
module-api은 실질적으로 module-common의 클래스들을 사용할 것이기 때문에 Service 를 작성하겠습니다  

**MemberServiceCustom.java**  
```
@Service
public class MemberServiceCustom {

    private MemberRepository memberRepository;

    public MemberServiceCustom(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long signup (Member member) {
        return memberRepository.save(member).getId();
    }
}
```

MemberRepository의 bean injection을 ```@Autowired```(이하 필드 injection)없이 생성자 injection을 사용하였습니다.  
이전에는 필드 injection을 많이 사용하였는데, 이럴 경우 **SpringMVC에 종속적**이게 된다는 점과 mock 의존성을 좀 더 자유롭게 사용하기가 힘든점이 있어 최근에 생성자 injection을 사용하고 있습니다.  
(IntelliJ의 경우 필드 injection을 사용하면 생성자 injection으로 교체하라는 메세지를 출력시키고 있습니다.)  

그리고 module-api의 build.gradle에도 사용할 의존성들을 추가하겠습니다.  
**build.gradle**  
```
dependencies {
	compile('org.springframework.boot:spring-boot-starter-web')
	testCompile('org.springframework.boot:spring-boot-starter-test')
}
```
자 여기까지 하시면 아마 엄청난 양의 빨간줄을 보실수 있으실 것입니다.  
이는 몇가지 이유가 있는데
* module-api가 사용하는 ```Member```와 ```MemberRepository```를 찾지 못했다.
* spring boot 관련 의존성이 관리되지 못하고 있다.
등의 이유가 있습니다. 그래서 이를 수정해보도록 하겠습니다.  

root 프로젝트인 gradle-multi-modules의 build.gradle을 아래와 같이 수정하겠습니다.  

**build.gradle**  

```
buildscript {
    ext {
        springBootVersion = '1.5.1.RELEASE'
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        classpath "io.spring.gradle:dependency-management-plugin:0.6.0.RELEASE"
    }
}

subprojects {
    group 'com.blogcode'
    version '1.0'

    apply plugin: 'java'
    apply plugin: 'spring-boot'
    apply plugin: 'io.spring.dependency-management'

    sourceCompatibility = 1.8

    repositories {
        mavenCentral()
    }

    dependencies {
        testCompile group: 'junit', name: 'junit', version: '4.12'
    }
}

project(':module-api') {
    dependencies {
        compile project(':module-common')
    }
}

project(':module-web') {
    dependencies {
        compile project(':module-common')
    }
}
```  

여기서 중요하게 보실 부분은 ```subprojects```와 ```project()```입니다.  
```subprojects```는 ```settings.gradle```에 include된 프로젝트 전부에 지정할 내용을 담당합니다.  
하위 프로젝트들 모두 SpringBoot와 Java에 의존성을 두고 있기에 관련된 plugin을 등록하였습니다.  
(root 프로젝트까지 적용하고 싶다면 ```allprojects```로 등록하시면 됩니다.)  
```project```의 경우 하위 프로젝트간의 의존성을 관리합니다.  
(참고로 ```:```은 디렉토리 path를 얘기합니다. root 프로젝트에서 하위 프로젝트 사이에 계층이 하나 존재하기 때문에 추가하였습니다.)   
```module-api```와 ```module-web```은 모두 ```module-common```에 의존하고 있기 때문에 이를 등록하였습니다.  
이렇게 설정할 경우 각 프로젝트는 이제 ```module-common``` 의 코드를 사용할 수 있게 됩니다.
이게 끝입니다.  
아주 간단하지 않으신가요?  
그럼 잘 적용되는지 한번 테스트 해보겠습니다.  
처음 테스트 해볼 내용은 ```module-common```입니다.  

**ModuleRepositoryTests**
```
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void add () {
        memberRepository.save(new Member("jojoldu", "jojoldu@gmail.com"));
        Member saved = memberRepository.findOne(1L);
        assertThat(saved.getName(), is("jojoldu"));
    }
}
```
```module-api```에 테스트코드를 작성하여 실제로 잘 되는지 테스트 해보겠습니다.  

**ModuleApiApplicationTests.java**  

```
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ModuleApiApplicationTests {

	@Autowired
	private MemberServiceCustom memberServiceCustom;

	@Test
	public void save() {
		Member member = new Member("jojoldu", "jojoldu@gmail.com");
		Long id = memberServiceCustom.signup(member);
		assertThat(id, is(1L));
	}
}
```

```MemberRepository```를 사용하는 MemberServiceCustom과 ```Member```를 사용하는 테스트 코드를 작성하였습니다. 이를 실행해보면!  

![]

gradle-multi-modules의 settings.gradle을 열어서 아래 코드를 추가합니다.  
```
rootProject.name = 'gradle-multi-modules'

include 'module-common', 'module-api', 'module-web'
```

이 코드는 gradle-multi-modules 프로젝트가 'module-common', 'module-api', 'module-web' 프로젝트를 하위 프로젝트로 관리하겠다는 의미입니다.  
settings.gradle의 내용은 여기서 끝입니다. 바로 build.gradle 코드를 작성하겠습니다.  

```

```
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


module-common 프로젝트는 단독으로 구동시킬 프로젝트가 아니기 때문에 spring context를 불러올 수 없습니다.   그래서 SpringBoot Test를 구동시킬 경우 아래와 같은 에러가 발생합니다.  

![Spring Context 불러오기 실패](./images/context불러오기실패.png)  

그래서 아래처럼 메인 테스트 클래스에 ```@SpringBootApplication```을 추가하였습니다.     



[권남님의 위키](http://kwonnam.pe.kr/wiki/gradle/multiproject)
[Gradle 레퍼런스](https://docs.gradle.org/current/userguide/multi_project_builds.html)
