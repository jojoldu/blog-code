# Java Enum 활용하기
이번 시간엔 실제 업무에서 enum을 활용할 수 있는 방법을 소개해보려고 합니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review)를 방문하셔서 star를 부탁드립니다!)<br/>  
  
### 상황
여러 이사짐 센터를 중개하는 서비스를 만든다고 가정해보겠습니다.  
당신의 서비스를 통해서 이사짐 센터로 고객을 중개할 경우, 이사비용의 일정 부분을 중개료로 받을 수 있습니다.  
각 이사짐 센터가 얼마의 중개료를 지불할지를 계약하는 **중개료 계약서 관리** 시스템을 만들어야 합니다. 
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
  
여기서 우리는 최종 수수료가 얼마가 나올지 까지는 고려하지 않고, 순수하게 위에서 제시한대로 중개료 계약서만 고려해서 진행해보겠습니다.  
  
### 해결 1
가장 쉽게 도메인 클래스를 작성해보면 아래와 같습니다.  
  
**OldCompanyContract.java && OldCompanyContractRepository.java**  
  
```
@Entity
public class OldCompanyContract {

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
    
    public OldCompanyContract() {}

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

public interface OldCompanyContractRepository extends JpaRepository<OldCompanyContract, Long>{
}
```

대부분이 String으로 이루어진 간단한 도메인입니다.  
(company의 경우 이번 시간에 주요 항목이 아니기 때문에 별도의 테이블 분리 없이 문자열로 다루겠습니다.)  

변경이 잦은 데이터일 경우 데이터베이스의 테이블로 관리하는 것이 좀 더 좋은 방법일 수 있습니다.  
다만, 변경이 거의 없는 데이터 그룹의 경우 enum은 좋은 방법이 될 수 있습니다.  
