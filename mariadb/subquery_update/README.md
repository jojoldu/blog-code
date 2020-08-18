# Subquery로 Update를 하면 안되는 이유

MySQL 5.6 부터는 서브쿼리 구문이 대폭 개선되어 더이상 서브쿼리로 인한 성능 저하는 없는 것처럼 보입니다.  


mysql에서 업데이트 쿼리 작성시 subquery를 사용할 경우 select와 동작방식이 다르기 때문에 주의가 필요하여 해당 테스트 스크립트를 작성했습니다.

결론
서브쿼리는 JOIN으로 변경하여 작성
서브쿼리를 이용하여 update가 실행될 경우 업데이트 대상의 테이블 전체를 읽게 되어 매우 느리게 처리된다.
서브쿼리를 이용할 경우 작은 테이블이라도 인덱스가 반드시 있어야 한다.



* 업데이트 대상 테이블 100만건
* 업데이트 조건 테이블 1000건

```sql
-- 업데이트 대상 테이블
create table target_table
(
    id int not null auto_increment,
    target_id int not NULL,
    primary key (id)
)ENGINE=InnoDB;
```

```sql
-- 100만건 insert
insert into target_table (target_id)
select (FLOOR( 1 + RAND( ) *100000000 ))
from information_schema.tables a, information_schema.tables b, information_schema.tables c
limit 1000000;
```

```sql
-- 업데이트 조건 테이블 (인덱스 없음)
create table source_table_noindex
(
    id int not null
)ENGINE=InnoDB;
```

```sql
-- 1000건 삽입
insert into source_table_noindex select id from target_table limit 1000;
``` 

```sql 
-- 업데이트 조건 테이블 (인덱스 있음)
create table source_table_index
(
    id int not null ,
    primary key (id)
)ENGINE=InnoDB;
```

```sql
-- 1000건 삽입
insert into source_table_index select id from target_table limit 1000;
```


Case 1
인덱스가 없는 테이블로 서브쿼리로 작성 

```sql
update target_table
set target_id = 100000
where id in (select id from source_table_noindex);
Query OK, 1000 rows affected (5 min 25.64 sec)
``` 

```sql 
-- 실행계획
+----+--------------------+----------------------+------------+-------+---------------+---------+---------+------+---------+----------+------------------------------+
| id | select_type        | table                | partitions | type  | possible_keys | key     | key_len | ref  | rows    | filtered | Extra                        |
+----+--------------------+----------------------+------------+-------+---------------+---------+---------+------+---------+----------+------------------------------+
|  1 | UPDATE             | target_table         | NULL       | index | NULL          | PRIMARY | 4       | NULL | 1000000 |   100.00 | Using where; Using temporary |
|  2 | DEPENDENT SUBQUERY | source_table_noindex | NULL       | ALL   | NULL          | NULL    | NULL    | NULL |    1000 |    10.00 | Using where                  |
+----+--------------------+----------------------+------------+-------+---------------+---------+---------+------+---------+----------+------------------------------+
실행시간 : 5분 25초
```

target_table 전체를 full scan하게되며, temporary table을 사용한다.
엑세스하는 데이터가 1,000,000 * 1,000

Case 2
인덱스가 있는 테이블로 서브쿼리로 작성

```sql
update target_table
set target_id = 100000
where id in (select id from source_table_index);
Query OK, 1000 rows affected (2.64 sec)
```

```sql 
-- 실행계획
+----+--------------------+--------------------+------------+-----------------+---------------+---------+---------+------+---------+----------+------------------------------+
| id | select_type        | table              | partitions | type            | possible_keys | key     | key_len | ref  | rows    | filtered | Extra                        |
+----+--------------------+--------------------+------------+-----------------+---------------+---------+---------+------+---------+----------+------------------------------+
|  1 | UPDATE             | target_table       | NULL       | index           | NULL          | PRIMARY | 4       | NULL | 1000000 |   100.00 | Using where; Using temporary |
|  2 | DEPENDENT SUBQUERY | source_table_index | NULL       | unique_subquery | PRIMARY       | PRIMARY | 4       | func |       1 |   100.00 | Using index                  |
+----+--------------------+--------------------+------------+-----------------+---------------+---------+---------+------+---------+----------+------------------------------+
실행시간 : 2.64초

```

target_table 전체를 full scan하게되며, temporary table을 사용한다.
엑세스하는 데이터가 1,000,000 * 1

Case 3
Join 쿼리

```sql
-- 서브쿼리 사용하지 않고 JOIN으로 작성 (인덱스 있음)
update target_table a
    join source_table_index b on a.id = b.id
set a.target_id = 100000
Query OK, 1000 rows affected (0.01 sec)
```

```sql 
-- 실행계획
+----+-------------+-------+------------+--------+---------------+---------+---------+------------+------+----------+-------------+
| id | select_type | table | partitions | type   | possible_keys | key     | key_len | ref        | rows | filtered | Extra       |
+----+-------------+-------+------------+--------+---------------+---------+---------+------------+------+----------+-------------+
|  1 | SIMPLE      | b     | NULL       | index  | PRIMARY       | PRIMARY | 4       | NULL       | 1000 |   100.00 | Using index |
|  1 | UPDATE      | a     | NULL       | eq_ref | PRIMARY       | PRIMARY | 4       | point.b.id |    1 |   100.00 | NULL        |
+----+-------------+-------+------------+--------+---------------+---------+---------+------------+------+----------+-------------+
```
 
```sql
-- 서브쿼리 사용하지 않고 JOIN으로 작성 (인덱스 없음)
update target_table a
    join source_table_noindex b on a.id = b.id
set a.target_id = 100001
Query OK, 1000 rows affected (0.01 sec)
```

```sql
-- 실행계획
+----+-------------+-------+------------+--------+---------------+---------+---------+------------+------+----------+-------+
| id | select_type | table | partitions | type   | possible_keys | key     | key_len | ref        | rows | filtered | Extra |
+----+-------------+-------+------------+--------+---------------+---------+---------+------------+------+----------+-------+
|  1 | SIMPLE      | b     | NULL       | ALL    | NULL          | NULL    | NULL    | NULL       | 1000 |   100.00 | NULL  |
|  1 | UPDATE      | a     | NULL       | eq_ref | PRIMARY       | PRIMARY | 4       | point.b.id |    1 |   100.00 | NULL  |
+----+-------------+-------+------------+--------+---------------+---------+---------+------------+------+----------+-------+
실행시간 : 0.01초
```

target_table에서 변경하고자 하는 데이터만 인덱스를 이용해서 찾는다.
엑세스하는 데이터가 1,000 * 1


[공식문서](https://dev.mysql.com/doc/refman/5.6/en/subquery-optimization.html)

![docs](./images/docs.png)

> **단일 테이블을 수정** (UPDATE 및 DELETE) 하기 위한 서브 쿼리에는 옵티마이저가 세미 조인 또는 구체화 서브 쿼리 최적화를 사용하지 않는다는 것입니다.  
> 해결 방법으로, 여러 테이블로 다시 작성하려고 UPDATE하고 DELETEA는 하위 쿼리보다는 조인을 사용 문.
