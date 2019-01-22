# Querydsl 다이나믹 쿼리 사용하기

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