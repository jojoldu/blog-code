# JPA 사용시 @Embedded 주의사항

간혹 JPA의 `@Embedded` 를 잘못사용하는 경우를 종종 보게 됩니다.  
  
이번 시간에는 어떤 식의

## 문제 상황

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

```java
@Getter
@NoArgsConstructor
@Embeddable
public class PayDetails {
    public static final PayDetails EMPTY = new PayDetails();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pay")
    private List<PayDetail> payDetails = new ArrayList<>();
```

```java
@Getter
@NoArgsConstructor
@Embeddable
public class PayEvents {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pay")
    private List<PayEvent> payEvents = new ArrayList<>();
```

### 테스트

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
## 해결책

