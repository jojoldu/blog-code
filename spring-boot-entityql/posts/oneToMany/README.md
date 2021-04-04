# EntityQL로 OneToMany (1:N) Bulk Insert 구현하기

[지난 시간](https://jojoldu.tistory.com/558)에는 EntityQL 환경을 적용해보았습니다.  
  
간단한 예제로 **단일 Entity**의 Bulk Insert를 보여드렸는데요.  
이번 시간에는 **OneToMany** 환경에서 어떻게 Bulk Insert를 구현할지 알아보겠습니다.

## 1. 해결책

EntityQL이 전환해주는 Querydsl-SQL은 JPA 기반이 아닙니다.  
  
그래서 OneToMany와 같은 연관관계 Insert/Update 등은 **JdbcTemplate**처럼 직접 구현을 해야하는데요.  
  
> 원래 JdbcTemplate으로 작성하던 코드를 단순히 정적 타입 개발이 가능하도록 지원할 뿐인것 이제는 다들 아시죠?

그래서 꼭 Querydsl-SQL이 아니더라도, JdbcTemplate로 구현한다 하여도, OneToMany 를 BulkInsert를 하려면 다음의 과정들로 해야만 합니다.  

1) 부모 Entity (one) 을 모아 bulk insert
2) 1)의 insert 결과로 채번 (`auto increment`) 된 id들을 리턴 받는다.
3) 해당 id를 기반으로 자식 Entity (toMany)를 생성하여 bulk insert한다.

여기서 두번째 내용인 `1)의 insert 결과로 채번 된 id들을 리턴 받는다` 가 핵심인데요.  
이는 JDBC 스펙에 있는 `generatedKeys()` 를 통해 전달 받을 수 있습니다.  
  
간단하게 코드로 구현하면 다음과 같습니다.

```java
String QUERY = "insert into persons (name) values (?)";
try (PreparedStatement ps = connection.prepareStatement(QUERY, Statement.RETURN_GENERATED_KEYS)) { // (1)
    for (i in 1..10) {
      ps.setString(1, "이름");
      ps.addBatch();
    }
    ps.executeBatch();

    // omitted
} catch (SQLException e) {
    // handle the database related exception appropriately
}

try (ResultSet keys = statement.getGeneratedKeys()) { // (2)
  if(keys.next()) {
      list.add(keys.getLong(1));
  }
  ...
}

```

> 위 코드는 [baeldung](https://www.baeldung.com/jdbc-returning-generated-keys) 의 코드를 일부 사용하였습니다.

(1) `Statement.RETURN_GENERATED_KEYS`

* PrepareStatement를 생성할 때 파라미터 `Statement.RETURN_GENERATED_KEYS` 을 추가
* 이렇게 추가하면 `ps.executeBatch()`를 실행한 이후에 `getGeneratedKeys()` 메서드를 통해서 insert 대상들의 채번된 키값을 확인할 수 있습니다.
* 단, JDBC 스펙에서는 `getGeneratedKeys()`와 `executeBatch()`를 **같이 사용하는 것이 필수 구현 사항이 아니기 때문에** 특정 DB나 구 버전에서는 지원하지 않습니다.
  * 다행히 MySQL의 JDBC 드라이버는 지원 합니다.
  * MSSQL의 JDBC 드라이버는 지원하지 않습니다.

> [SimpleJdbcInsert](https://www.baeldung.com/spring-jdbc-jdbctemplate#1-simplejdbcinsert) 에서 `executeAndReturnKey()` 와 `executeBatch()`를 지원해서 oneToMany bulk insert를 구현할 수 있지않을까 했지만 **사용하지 못했습니다.**  
> SimpleJdbcInsert 의 `executeBatch` 가 **return 타입에 특정 컬럼 지정이 안됩니다**.  
> return 타입으로 int[]만 가능 (즉, 반영된 row 수만 받을 수 있습니다)  
> `executeAndReturnKey` 는 **단일 row만 가능**

(2) `statement.getGeneratedKeys()`

* 방금 batch insert가 된 값들의 채번 (`auto increment`) 된 Key들을 리턴 받을 수 있습니다.
* JDBC의 `getGeneratedKeys()` 를 Querydsl-SQL에서는 `executeWithKeys` 메소드로 사용할 수 있습니다.
* **JdbcTemplate에는 이 메소드가 구현되어 있지 않습니다**

## 2. 구현 코드


(save되고 받은 부모의 id가 어느 자식들과 매핑이 되어야하는지 구분이 필요하기 때문에)

1. 부모데이터를 save하기 전에 UUID를 발급해서 임시 컬럼에 저장한다.

UUID는 1천건 생성시 0.046초 정도의 수행시간이 소요된다. (즉, UUID 생성에는 성능 저하가 거의 없다)
부모번호 + 상태만 가지고 구분할 순 없다.
부분취소 도입이 예정되어 있다.
즉, 같은 부모에 여러 취소 상태의 row가 적재 가능하다.

2. 별도의 컬렉션 변수에 UUID를 가진 부모 데이터들을 저장한다.

3. 부모만 (자식 제외) 만 먼저 BulkInsert 한다.

4. BulkInsert하고 나온 부모의 PK 를 통해 PK와 UUID 값을 별도로 조회한다.

5. 조회된 UUID를 컬렉션에 미리 담아둔 부모의 UUID와 매칭해서 자식/부가정보등 다른 Entity를 찾는다

6. 매칭 결과로 찾아낸 자식/부가정보에 부모 PK값을 담아서 bulk insert한다. 

## ManyToMany

oneToMany 방식을 더 풀어서 ManyToMany도 마찬가지로 구현할수도 있겠지만, 코드 복잡도가 너무 괴랄하다. 
중간 매핑 테이블까지 BulkInsert 대상으로 만들어서 처리해야하는데, 

1) 처음 부모 Entity BulkInsert
2) BulkInsert된 부모 Entity Key조회
3) 부모에 포함되어 있는 자식 Entity BulkInsert
4) BulkInsert된 자식 Entity Key조회
5) 2와 4에서 조회된 Key들을 서로 매칭 (멀티쓰레드상에서 키 매칭에 문제가 없어야 함) 
6) 
  
일단은 JPA saveAll을 선택했다.

약간 개선한다면, Many 쪽은 JPA SaveAll하고, 생성된 Entity를 통해 나머지 하위 ToMany쪽은 BulkInsert하는 방식으로 하게 되면

수행 시간이 절반이하로 줄어들 정도로 향상이 가능하다.

