# EntityQL로 OneToMany (1:N) Bulk Insert 구현하기

Querydsl-SQL은 JPA 기반이 아니다.

원래 jdbcTemplate으로 작성하던 코드를 단순히 정적 타입 개발이 가능하도록 지원할 뿐이다.

즉, JPA에서 지원하는 cascade save 등의 기능이 없다.

그래서 oneToMany와 같은 연관관계 save도 본인이 직접 구현해야 한다 .



컨셉은 다음과 같다.

1) 부모 Entity (one) 을 모아 bulkInsert한다

2) 1)의 결과로 채번된 id 를 리턴 받는다.

3) 해당 id를 기반으로 자식 Entity (toMany)를 생성하여 bulk Insert한다.



여기서 2)는 JDBC 스펙에 있는 generatedKeys() 를 통해 전달 받는다.

날 것 그대로 작성하면 아래와 같다.

// Kotlin
fun batchInsert() {
val sql = """
insert into product(
name, price, create_date
) values (
?, ?, now()
)
""".trimIndent()

dataSource.connection.use { conn ->
val ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) // (1) Statement.RETURN_GENERATED_KEYS 이부분

    for (i in 1..10) {
      ps.setString(1, "상품명")
      ps.setInt(2, 5_000)
      ps.addBatch()
    }
    ps.executeBatch()
 
    val keys = ps.generatedKeys.use { // (2) ps.generatedKeys 이부분
      generateSequence {
        if (it.next()) it.getLong(1) else null
      }.toList()
    }
}
}


PrepareStatement를 생성할 때 파라미터 Statement.RETURN_GENERATED_KEYS 을 추가
이렇게 추가하면 ps.executeBatch() 실행한 이후에 getGeneratedKeys() 메서드를 통해서 insert 대상들의 채번된 키값을 확인할 수 있다.
단, JDBC 스펙에서는 getGeneratedKeys()와 executeBatch()를 같이 사용하는 것이 필수 구현 사항이 아니기 때문에 특정 DB나 구 버전에서는 지원하지 않는다.
다행히 MySQL의 jdbc 드라이버는 지원 한다
MSSQL의 jdbc 드라이버는 지원하지 않는다.
SimpleJdbcInsert 에서 executeAndReturnKey()와 executeBatch()를 지원해서 oneToMany bulk insert를 구현할 수 있지않을까했는데.. => No
executeBatch가 return 타입에 특정 컬럼 지정이 안된다

return 타입으로 int[]만 가능 (즉, 반영된 row 수만 받을 수 있다)

executeAndReturnKey는 단일 row만 가능



위에서 사용된 Jdbc의 getGeneratedKeys()를 Querydsl-sql에서는 executeWithKeys 메소드로 사용할 수 있다.



그래서 정산에서는 다음과 같이 구현했다.

(save되고 받은 부모의 id가 어느 자식들과 매핑이 되어야하는지 구분이 필요하기 때문에)



1. 주문데이터를 save하기 전에 UUID를 발급해서 임시 컬럼에 저장한다.

UUID는 1천건 생성시 0.046초 정도의 수행시간이 소요된다. (즉, UUID 생성에는 성능 저하가 거의 없다)
주문번호 + 상태만 가지고 구분할 순 없다.
부분취소 도입이 예정되어 있다.
즉, 같은 주문에 여러 취소 상태의 row가 적재 가능하다.
2. 별도의 컬렉션 변수에 UUID를 가진 주문 데이터들을 저장한다.

3. 주문만 (결제/부가 제외) 만 먼저 BulkInsert 한다.

4. BulkInsert하고 나온 주문의 PK 를 통해 PK와 UUID 값을 별도로 조회한다.

5. 조회된 UUID를 컬렉션에 미리 담아둔 주문의 UUID와 매칭해서 결제/부가정보등 다른 Entity를 찾는다

6. 매칭 결과로 찾아낸 결제/부가정보에 주문 PK값을 담아서 bulk insert한다. 

## ManyToMany는?

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

