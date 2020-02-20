
```java
public List<Integer> getGroupOne() {
    return queryFactory.select(Expressions.ONE)
            .from(pointEvent)
            .groupBy(pointEvent.pointStatus)
            .fetch();
}
```

```java
public List<Integer> getGroupOne() {
    return queryFactory.select(Expressions.ONE)
            .from(pointEvent)
            .groupBy(pointEvent.pointStatus)
            .fetch();
}
```