# Querydsl 에서 Cross Join 주의

oneToOne 에서 Cross Join 발생한다.

```java
from(parent).innerJoin(parent.child).fetch()
    .where(parent.something.gt(parent.child.somthing))
```

```java
from(parent).innerJoin(parent.child, child).fetch() // as 처리가 필요함.
    .where(parent.something.gt(child.somthing))
```