# [Querydsl] Case When 사용하기

안녕하세요? 이번 시간엔 spring-boot-querydsl 예제를 진행해보려고 합니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/spring-boot-querydsl)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>


## 

코드

```java
@RequiredArgsConstructor
public class PointEventRepositoryImpl implements PointEventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PointCalculateAmount> calculateAmounts() {
        return queryFactory
                .select(Projections.fields(PointCalculateAmount.class,
                        new CaseBuilder()
                                .when(pointEvent.pointStatus.in(PointStatus.USE, PointStatus.USE_CANCEL))
                                .then(pointEvent.pointAmount.multiply(-1))
                                .otherwise(pointEvent.pointAmount).as("pointAmount"),
                        pointEvent.pointStatus
                ))
                .from(pointEvent)
                .fetch();
    }
}
```

테스트 코드

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class CaseWhenTest {

    @Autowired
    private PointEventRepository pointEventRepository;

    @After
    public void tearDown() throws Exception {
        pointEventRepository.deleteAll();
    }

    @Test
    public void 조건에따라_값이_변경된다() {
        //given
        long usePointAmount = 500;
        long useCancelPointAmount = -900;
        pointEventRepository.saveAll(
                Arrays.asList(
                        new PointEvent(PointStatus.EARN, 100),
                        new PointEvent(PointStatus.USE, usePointAmount),
                        new PointEvent(PointStatus.USE_CANCEL, useCancelPointAmount)
                ));
        //when
        List<PointCalculateAmount> result = pointEventRepository.calculateAmounts();
        result.sort(Comparator.comparingLong(PointCalculateAmount::getPointAmount));

        //then
        assertThat(result.get(0).getPointStatus(), is(PointStatus.USE));
        assertThat(result.get(0).getPointAmount(), is(-usePointAmount)); // 500원이 -500원으로

        assertThat(result.get(1).getPointStatus(), is(PointStatus.EARN));
        assertThat(result.get(1).getPointAmount(), is(100L));

        assertThat(result.get(2).getPointStatus(), is(PointStatus.USE_CANCEL));
        assertThat(result.get(2).getPointAmount(), is(-useCancelPointAmount)); // -900 원이 900원으로

    }
}
```

