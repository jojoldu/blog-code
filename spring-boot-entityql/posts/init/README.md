# (MySQL) Auto Increment에서 TypeSafe Bulk Insert 진행하기 (feat.EntityQL, JPA)

앞서 여러 글에서 언급하고 있지만, JPA환경에서 키 생성 전략을 Auto Increment로 할 경우 BulkInsert가 지원되지 않는다.

* [Spring Batch Item Writer 성능 비교](https://jojoldu.tistory.com/507)
* [MySQL 환경의 스프링부트에 하이버네이트 배치 설정 해보기](https://woowabros.github.io/experience/2020/09/23/hibernate-batch.html)

모든 내용은 **MySQL** 기반 하에 이야기 합니다. 

> 즉, 이외 DBMS 에서는 다를 수 있습니다.

## 1. (MySQL) JPA Bulk Insert 의 문제

먼저 JPA Bulk Insert의 문제에 대해서 처음 들어보신 분들을 위해 정리하자면 다음과 같습니다.

* BulkInsert란 Insert 쿼리를 한번에 처리하는 것을 의미한다.
    * MySQL에서는 아래와 같이 Insert 합치기 옵션을 통하면 비약적인 성능 향상을 가진다. 

```sql
INSERT INTO person (name) VALUES
('name1'),
('name2'),
('name3');
```

* 이렇게 Insert 합치기를 하려면 JdbcUrl 옵션에 `rewriteBatchedStatements=true`이 필수로 설정 되어있어야 한다.
* 단, **Id 채번(생성) 전략이 auto_increment**일 경우 JPA를 통한 save는 **Insert 합치기가 적용되지 않는다**.
    * `Table Sequence` 로 채번 전략을 선택할 경우 JPA로도 Insert 합치기가 가능하다.
    * 하지만 운영 환경에서는 사용이 어렵다. 
        * 이미 auto_increment 로 수억~수십억건 쌓여있는 테이블에서 채번 전략을 변경/마이그레이션 하는 것에 대한 부담
        * 데드락 이슈 케이스들 ([HikariCP Dead lock에서 벗어나기](https://woowabros.github.io/experience/2020/02/06/hikaricp-avoid-dead-lock-2.html))
    

결론은 Auto_increment이면 JPA가 아닌 `JdbcTemplate`과 같은 네이티브 쿼리를 작성하는 경우에만 insert합치기를 통한 bulk insert가 지원된다.


다만, 그걸 계속 사용하기엔 단점들이 너무 강한데,

* JdbcTemplate (+ MyBatis)와 같이 문자열 기반의 SQL 프레임워크는 IDE 자동 지원이 제한적이다.
* Entity (Table) 컬럼 추가/수정이 있을때마다 연관된 쿼리 문자열을 모두 찾아서 반영해야 한다. 
    * 잠재적 장애 발생 요인이며, 생산성 저하에 가장 큰 요인이다.

JOOQ나 Querydsl-SQL 같은 도구를 선택해야만 컴파일체크/타입지원/IDE 자동완성 등의 TypeSafe한 Bulk Insert가 가능하다.


그래서 일단은 Querydsl-SQL을 이용해서 BulkInsert를 구현하고 싶은데,

Querydsl-SQL은 그럼 사용하기 편한가? - No

* 일단 QClass 생성 과정이 괴랄
    * JPA 기반이 아니라서 테이블 스캔해야만 한다.
    * 즉, local이든 베타이든 DB를 실행해서, 테이블들을 Querydsl-SQL이 scan할 수 있도록 Gradle 설정이 들어간다.
    * 이건 JOOQ도 마찬가지의 단점이다.
* 이게 싫어서 QClass를 버전관리 하는데 이건 완전 안티패턴이다.
    * 제너레이트 클래스를 버저닝하고, 커밋 내역에 항상 반영하는건 전혀 좋은 패턴으로 보지 않는다.
* 부가 설정이 너무 많이 필요하다.
    * Querydsl 버전 업데이트가 안되는 것도 큰 이유이다.

## 이슈 케이스


```java
Caused by: java.lang.NoSuchMethodError: com.google.common.collect.Sets$SetView.iterator()Lcom/google/common/collect/UnmodifiableIterator;
```

이럴 경우 `reflections:0.9.11` 에서 필요한 Guava의 버전이 `20.0` 인데, 현재 프로젝트의 다른 의존성 때문에 Guava가 다른 버전으로 의존하고 있을 경우에 발생한다. 
이럴 경우 아래와 같이 강제로 Guava 버전을 고정한다.

```groovy
configurations {
    all {
        resolutionStrategy {

            // 특정 모듈의 버전을 강제 지정(최상위건 이행적 의존성이건 무관함)
            force  'com.google.guava:guava:20.0'
        }
    }
}
```
