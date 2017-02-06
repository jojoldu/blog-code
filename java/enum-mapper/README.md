# Java Enum 활용하기
이번 시간엔 실제 업무에서 enum을 활용할 수 있는 방법을 소개해보려고 합니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review)를 방문하셔서 star를 부탁드립니다!)<br/>  
  
### 상황
여러 이사짐 센터를 중개하는 서비스를 만든다고 가정해보겠습니다.  
이 서비스를 통해서 이사짐 센터로 고객을 중개할 경우, 이사비용의 일정 부분을 중개료로 받는 BM(Business Model)을 가지고 있습니다.  
여러분은 각 이사짐 센터가 얼마의 중개료를 지불할지를 계약하는 **중개료 계약서 관리** 시스템을 만들어야 합니다. 
계약서 항목은 다음과 같습니다.
* 회사명
* 수수료
* 수수료타입
  - 기록된 수수료를 %로 볼지, 실제 원단위의 금액으로 볼지를 나타냅니다.
* 수수료절삭
  - 수수료의 일정금액 이하를 반올림 할것인지, 올림할 것인지, 버림할 것인지를 나타냅니다. 
  - 여기서는 10원 이하 단위를 절삭하겠습니다.

즉, 위 항목을 기준으로 예를 든다면  
> A라는 이사짐센터가 수수료: 1.0, 수수료타입: 퍼센테이지, 수수료절삭: 버림으로 계약하게 되면  
해당 센터에서는 중개될때마다 이사 비용의 1.0%를 수수료로 지불하며 10원 이하 단위는 버림하는 것입니다.  
  
여기서 우리는 최종 수수료가 얼마가 나올지 까지는 고려하지 않고, 순수하게 위에서 제시한대로 **중개료 계약서만** 고려해서 진행해보겠습니다.  
가장 쉽게 domain 클래스를 작성해보면 아래와 같습니다.  
  
**OldCompanyContract.java**  
  
```
@Entity
public class OldContract {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private double commission; // 수수료

    @Column(nullable = false)
    private String commissionType; // 수수료 타입 (예: 퍼센테이지, 금액)

    @Column(nullable = false)
    private String commissionCutting; // 수수료 절삭 (예: 반올림, 올림, 버림)

    public OldContract() {}

    public OldContract(String company, double commission, String commissionType, String commissionCutting) {
        this.company = company;
        this.commission = commission;
        this.commissionType = commissionType;
        this.commissionCutting = commissionCutting;
    }

    public Long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public double getCommission() {
        return commission;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public String getCommissionCutting() {
        return commissionCutting;
    }

}

```
대부분이 String으로 이루어진 간단한 domain입니다.  
(company의 경우 이번 시간에 주요 항목이 아니기 때문에 별도의 테이블 분리 없이 문자열로 다루겠습니다.)  
> 참고로 setter는 getter와 달리 무조건 생성하지 않습니다.  
해당 domain 인스턴스에 변경이 필요한 이벤트가 있을 경우 그 이벤트를 나타낼 수 있는 메소드를 만들지 무분별하게 값을 변경하는 setter는 최대한 멀리하시는게 좋습니다.  
(예를 들어, 주문취소 같은 경우 ```setOrderStatus()```를 만들고, 사용하는 것이 아니라 ```cancelOrder()```를 만들어서 사용하는 것입니다.  
똑같이 orderStatus를 변경할지라도, 그 의도와 사용범위가 명확한 메소드를 만드는것이 중요합니다.)  

그리고 이 domain을 다룰 repository를 생성하겠습니다.  
  
**OldCompanyContractRepository.java**  
  
```
public interface OldCompanyContractRepository extends JpaRepository<OldCompanyContract, Long>{}
```

domain클래스와 repository클래스가 생성되었으니 이를 테스트할 수 있도록 테스트 클래스를 생성하겠습니다.  

**ApplicationTests.java**  
  
```
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private OldCompanyContractRepository oldRepository;

	private OldCompanyContract oldContract;

	@Before
	public void setup() {
		oldContract = new OldCompanyContract(
				"우아한짐카",
				1.0,
				"percent",
				"round"
		);
	}

	@Test
	public void add() {
		oldRepository.save(oldContract);
		OldCompanyContract saved = oldRepository.findAll().get(0);
		assertThat(saved.getCommission(), is(1.0));
	}
}
```

save & find가 잘되는 것을 확인할 수 있습니다.  
기본적인 상황정리는 된 것 같습니다. 
위 코드를 토대로 관리 시스템을 만든다고 생각해보시면 어떠실까요?  
몇가지 문제점이 보이시나요?  
하나하나 문제점들을 수정해보겠습니다.

### 요구사항
위와 같은 domain에서 다음의 요구사항이 발생하면 어떻게 해결해야할까요?  
* 수수료타입, 수수료절삭으로 **검색**이 가능해야 한다.
* 입력화면에서 수수료타입, 수수료절삭은 **select box**로 표현한다.
* 백엔드는 여러 곳에서 사용할 수 있도록 **API**형태로 제공되어야 한다.
  

### 첨언
변경이 잦은 데이터일 경우 데이터베이스의 테이블로 관리하는 것이 좀 더 좋은 방법일 수 있습니다.  
다만, 변경이 거의 없는 데이터 그룹의 경우 enum은 좋은 방법이 될 수 있습니다.  
만약 위 기준만으로 결정하기가 힘들다면 2가지 방식의 장/단점을 보시고 결정하셔도 될것 같습니다.
* DB로 관리하게 될 경우, 변경에 용이하다는 장점을 얻지만 반면에 개발자가 개발/운영시에 전체 데이터를 한눈에 볼 수 없다는 단점이 있습니다.  
* enum으로 관리하게 될 경우, 변경에는 DB때보다 어렵지만 (변경이 필요할 경우 배포가 필요하게 됨) 개발자가 개발/운영시에 한눈에 전체 데이터를 확인하고, 컴파일러에서 직접 체크가 가능하기 떄문에 실수할 여지가 사라집니다.  
