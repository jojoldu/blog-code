# Subquery로 Where Select할 경우 (MySQL 5.6)

mysql에서 업데이트 쿼리 작성시 subquery를 사용할 경우 select와 동작방식이 다르기 때문에 주의가 필요하여 해당 테스트 스크립트를 작성했습니다.

결론
서브쿼리는 JOIN으로 변경하여 작성
서브쿼리를 이용하여 update가 실행될 경우 업데이트 대상의 테이블 전체를 읽게 되어 매우 느리게 처리된다.
서브쿼리를 이용할 경우 작은 테이블이라도 인덱스가 반드시 있어야 한다.



* 업데이트 대상 테이블 100만건
* 업데이트 조건 테이블 1000건

```sql
-- 업데이트 대상 테이블
create table main_table
(
    id int not null auto_increment,
    target_id int not NULL,
    primary key (id)
)ENGINE=InnoDB;
```

```sql
-- 100만건 insert
insert into main_table (target_id)
select (FLOOR( 1 + RAND( ) *100000000 ))
from information_schema.tables a, information_schema.tables b, information_schema.tables c
limit 1000000;
```

```sql
-- 업데이트 조건 테이블 (인덱스 없음)
create table sub_table_noindex
(
    id int not null
)ENGINE=InnoDB;
```

```sql
-- 1000건 삽입
insert into sub_table_noindex select id from main_table limit 1000;
``` 

```sql 
-- 업데이트 조건 테이블 (인덱스 있음)
create table sub_table_index
(
    id int not null ,
    primary key (id)
)ENGINE=InnoDB;
```

```sql
-- 1000건 삽입
insert into sub_table_index select id from main_table limit 1000;
```


```sql
EXPLAIN EXTENDED

SHOW WARNINGS;
```