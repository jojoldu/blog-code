# JPA 사용시 @Embedded 주의사항

간혹 JPA의 `@Embedded` 를 잘못사용하는 경우를 종종 보게 됩니다.  
  
이번 시간에는 `@Embedded` 를 사용하면서 주의해야할 점을 알아보겠습니다.

## 문제 상황

예를 들어서 다음과 같은 Entity 가 있습니다.

```java
@Getter
@NoArgsConstructor
@Entity
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long amount;
    private String orderNo;

    @Embedded
    private PayDetails payDetails = PayDetails.EMPTY;

    @Embedded
    private PayEvents payEvents = new PayEvents();
```

2개의 `@Embedded` 객체들이 있는데요.  
payEvents는 `new PayEvents`를 통해서 Entity가 생성되면 즉시 기본 객체가 생성될 수 있도록 사용했습니다.  
payDetails 역시 동일한 기능을 하지만, `new` 키워드를 매번 사용하기 귀찮아서 **전역 변수**인 `PayDetails.EMPTY;` 를 사용하도록 하였습니다.  
  
각각의 Embedded 클래스와 전역 변수는 다음과 같이 작업되어있습니다.  
  
**PayDetails**

```java
@Getter
@NoArgsConstructor
@Embeddable
public class PayDetails {
    public static final PayDetails EMPTY = new PayDetails(); // 전역 변수

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pay")
    private List<PayDetail> payDetails = new ArrayList<>();
```

**PayEvents**

```java
@Getter
@NoArgsConstructor
@Embeddable
public class PayEvents {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pay")
    private List<PayEvent> payEvents = new ArrayList<>();
```

자 2개의 `@Embedded` 어노테이션이 선언되어있는데요.  
이 중 어떤게 문제일지 한번 보겠습니다.

## 테스트

### Dto Test

```java
@Test
void PayDetails_EMPTY는_같은객체다() throws Exception {
    //given
    Pay pay1 = new Pay();
    Pay pay2 = new Pay();

    //then
    System.out.printf("pay1=%s, pay2=%s%n", pay1.getPayDetails(), pay2.getPayDetails());
    assertThat(pay1.getPayDetails()).isEqualTo(pay2.getPayDetails());
}
```

```java
@Test
void PayEvents는_매번_새로생성된다() throws Exception {
    //given
    Pay pay1 = new Pay();
    Pay pay2 = new Pay();

    //then
    System.out.printf("pay1=%s, pay2=%s%n", pay1.getPayEvents(), pay2.getPayEvents());
    assertThat(pay1.getPayEvents()).isNotEqualTo(pay2.getPayEvents());
}
```

### Jpa Test

영속성 컨텍스트 문제 발생

```java
detached entity passed to persist: com.jojoldu.blogcode.springbootjpa.domain.pay.PayDetail
```
## 해결책

