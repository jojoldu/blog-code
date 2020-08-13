# 화성에서 온 Querydsl, 금성에서 온 Mysql


![intro](./images/intro.png)

querydsl-jpa 를 소개하는 자리
querydsl-sql은 해당하지 않는다.

즉, JPQL에서 지원하지 않으면 querydsl이라고 뾰족한 수가 있진 않다.


## 1. 성능 이슈

### 테스트 환경

* Java 8
* Querydsl 4.2.1
* MySQL 5.6 (MariaDB 10.1)

MySQL 5.6 에는 [인덱스 컨디션 푸시다운](https://jojoldu.tistory.com/474) 최적화가 진행되었고,
5.7에서는 각종 인덱스와 옵티마이저 개선이 진행되었습니다.  
  
Querydsl의 경우에도 2,3,4 로 오면서 문법적인것에서 변화가 있었기 때문에 아래 내용을 적용시에는 꼭 현재 환경에서 테스트를 진행해시길 추천드립니다.

### exist 사용하지 않기

### select 필드에 entity 사용하지 않기

### QuerydslSupport의 페이징 사용하지 않기

### 페이징이 필요 없는 정렬 조회는 애플리케이션에서 정렬하기

### 정렬이 필요 없는 group by는 정렬 제거하기

### 서브쿼리 대신 Join으로

in 절에 select 쿼리를 넣을시 성능상 이슈가 발생할 수 있다.
서브쿼리로 조회된 결과 데이터를 임시 테이블에 적재해서 처리하는데,
이 데이터의 양이 **메모리에서 처리할 수 없는 규모일 경우** 디스크에 임시 테이블을 생성해서 처리하게 된다.

### 커버링 인덱스는 2번의 쿼리로

```sql
select *
from usertest
where chgdate like '2010%'
limit 100000, 100
```

```sql
select a.*
from (
      select userno
      from usertest
      where chgdate like '2012%'
      limit 100000, 100
) b join usertest a on b.userno = a.userno
```


* 카카오뱅크 MySQL DBA이신 [성동찬님의 커버링 인덱스 소개글](https://gywn.net/2012/04/mysql-covering-index/)
* Non Clustered Key, Clustered Key를 포함한 [커버링 인덱스 기본 지식](https://jojoldu.tistory.com/476)

Querydsl의 경우 from절의 subquery를 지원하지 않습니다.  
그래서 이럴 경우엔 다음처럼 2번의 쿼리로 나눠서 호출합니다.


## 2. 팀 컨벤션


### 상속 (extends) / 구현 (implements) 없이 사용하기

아래와 같은 형태의 Querydsl Repository는 사용하지 않는다.

```java
ARepository extends QuerydslRepositorySupport
```

```java
BRepositoryImpl implements BRepositoryCustom
```

core repsotory에 둘 것인가, 서비스 Repository에 둘 것인지 구분한다.  
서비스 repository들은 모두 아래와 같이 querydslFactory만 받아서 사용한다.

```java
@RequiredArgsConstructor
@Repository
public class AcademyQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Academy> findByName(String name) {
        return queryFactory.selectFrom(academy)
                .where(academy.name.eq(name))
                .fetch();
    }
}
```

결국 Querydsl은 ```JPAQueryFactory``` 만 있으면 사용할 수 있다.

### bean/constructor 대신에 fields 사용하기

### if문 대신 BooleanExpression

### 불필요한 캐스팅 제거

### constantAs 적극 사용하기

이미 특정값을 알고 있는 경우엔 constantAs로 불필요한 컬럼 조회를 제거한다.


## 3. 의도치 않은 쿼리 주의

### distinct dto

서로 다른 row라도 dto 필드 값이 같다면 distinct 대상이 되어버린다.

### 연관관계에선 alias 사용

oneToOne 에서 Cross Join 발생한다.

```java
from(parent).innerJoin(parent.child).fetch()
    .where(parent.something.gt(parent.child.somthing))
```

```java
from(parent).innerJoin(parent.child, child).fetch() // as 처리가 필요함.
    .where(parent.something.gt(child.somthing))
```



