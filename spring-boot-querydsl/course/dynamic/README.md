# Querydsl 다이나믹 쿼리 사용하기

안녕하세요!  
이번 시간에는 Querydsl에서의 다이나믹 쿼리를 어떻게 작성하면 좋을지에 대해 진행합니다.  
처음 Querydsl을 쓰시는 분들이 가장 많이 실수하는 부분이니 
  
그럼 시작합니다!

> 모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/spring-boot-querydsl)에 있으니 참고하세요 :)

## 1. 문제 상황

예를 들어 다음과 같이 **상황에 따라 조건문이 생성** 되어야 한다고 보겠습니다.  


```java
@Override
    public List<Settler> getSettlerList(Long ownerId, String startDate, String endDate) {

        QSettler settler = QSettler.settler;

        BooleanBuilder builder = new BooleanBuilder();

        Long ownerId = param.getOwnerId();
        String startDate = param.getStartDate();
        String endDate = param.getEndDate();

        if(!Objects.isNull(ownerId) && ownerId <= 0){
            builder.and(settler.owner.id.eq(ownerId));
        }
        if(!Objects.isNull(startDate) && !"".equals(startDate)){
            builder.and(settler.startDate.eq(LocalDate.parse(startDate)));
        }
        if(!Objects.isNull(endDate) && !"".equals(endDate)){
            builder.and(settler.endDate.eq(LocalDate.parse(endDate)));
        }


        return nqueryFactory.select(settler)
                .from(settler)
                .where(builder)
                .fetch();
    }
```

## 2. 해결


```java
@Override
    public List<Academy> findDynamicQueryAdvance(String name, String address, String phoneNumber) {
        return queryFactory
                .selectFrom(academy)
                .where(eqName(name),
                        eqAddress(address),
                        eqPhoneNumber(phoneNumber))
                .fetch();
    }

    private BooleanExpression eqName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return academy.name.eq(name);
    }

    private BooleanExpression eqAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return null;
        }
        return academy.address.eq(address);
    }

    private BooleanExpression eqPhoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return null;
        }
        return academy.phoneNumber.eq(phoneNumber);
    }
```

```java
@Test
    public void 동적쿼리_개선_name() {
        //given
        String targetName = "name";
        academyRepository.saveAll(Arrays.asList(
                new Academy(targetName, targetName, ""),
                new Academy("not target", "", "")
        ));

        //when
        List<Academy> academies = academyRepository.findDynamicQueryAdvance(targetName, "", "");

        //then
        assertThat(academies.size(), is(1));
        assertThat(academies.get(0).getAddress(), is(targetName));
    }
```