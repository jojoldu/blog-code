# Subquery로 Update를 하면 안되는 이유

[지난 포스팅](https://jojoldu.tistory.com/520)으로 ```select ~ where in (서브쿼리)```는 MySQL 5.6 버전에서 대폭 최적화 되었음을 확인하였는데요.  



mysql에서 업데이트 쿼리 작성시 subquery를 사용할 경우 select와 동작방식이 다르기 때문에 주의가 필요하여 해당 테스트 스크립트를 작성했습니다.


## 0. 테스트 환경

* 메인 테이블 100만건
* 서브 테이블 1000건

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
-- 업데이트 조건 테이블 (인덱스 없음)
create table sub_table_noindex
(
    id int not null
)ENGINE=InnoDB;
```
 
```sql 
-- 업데이트 조건 테이블 (인덱스 있음)
create table sub_table_index
(
    id int not null ,
    primary key (id)
)ENGINE=InnoDB;
```

## 1. Subquery

### 1-1. No Index

인덱스가 없는 테이블로 서브쿼리로 작성 

```sql
update main_table
set target_id = 100000
where id in (select id from sub_table_noindex);
``` 

![subquery_noindex_plan](./images/subquery_noindex_plan.png)

target_table 전체를 full scan하게되며, temporary table을 사용한다.
엑세스하는 데이터가 1,000,000 * 1,000

### 1-2. Index



```sql
update main_table
set target_id = 100000
where id in (select id from sub_table_index);
```


![subquery_index_plan](./images/subquery_index_plan.png)

> type의 ```unique_subquery``` 는 WHERE 조건절에서 사용될 수 있는 IN (subquery) 형태의 쿼리를 위한 접근 방식입니다.  
> unique_subquery의 의미 그대로 서브 쿼리에서 중복되지 않은 유니크한 값만 반환할 때 이 접근 방법을 사용합니다.
> 위 쿼리 문장의 IN (subquery) 부분에서 subquery를 살펴보겠습니다. 
> emp_no=10001인 레코드 중에서 부서 번호는 중복이 없기 때문에(dept_emp 테이블에서 프라이머리 키가 dept_no + emp_no이므로) 실행 계획의 두 번째 라인의 dept_emp 테이블의 접근 방식은 unique_subquery로 표시된 것입니다.


target_table 전체를 full scan하게되며, temporary table을 사용한다.
엑세스하는 데이터가 1,000,000 * 1

## 2. Join

### 2-1. No Index

 
```sql
-- 서브쿼리 사용하지 않고 JOIN으로 작성 (인덱스 없음)
update target_table a
    join source_table_noindex b on a.id = b.id
set a.target_id = 100001
```

![join_noindex_plan](./images/join_noindex_plan.png)

### 2-2. Index

```sql
-- 서브쿼리 사용하지 않고 JOIN으로 작성 (인덱스 있음)
update target_table a
    join source_table_index b on a.id = b.id
set a.target_id = 100000
Query OK, 1000 rows affected (0.01 sec)
```

![join_index_plan](./images/join_index_plan.png)


target_table에서 변경하고자 하는 데이터만 인덱스를 이용해서 찾는다.
엑세스하는 데이터가 1,000 * 1

[공식문서](https://dev.mysql.com/doc/refman/5.6/en/subquery-optimization.html)

![docs](./images/docs.png)

> **단일 테이블을 수정** (UPDATE 및 DELETE) 하기 위한 서브 쿼리에는 옵티마이저가 세미 조인 또는 구체화 서브 쿼리 최적화를 사용하지 않는다는 것입니다.  
> 해결 방법으로, 여러 테이블로 다시 작성하려고 UPDATE하고 DELETEA는 하위 쿼리보다는 조인을 사용문.

## 결론

* MySQL 5.6 에서 서브쿼리가 개선되었지만, **Update/Delete** 에는 적용되지 않는다.
* 즉, ```update ~ where in (서브쿼리)``` 형태는 **JOIN으로 변경** 해야만 한다.
    * 서브쿼리를 이용하여 update가 실행될 경우 업데이트 대상의 테이블 전체를 읽게 되어 매우 느리게 처리된다.  
    * 서브쿼리를 이용할 경우 작은 테이블이라도 인덱스가 반드시 있어야 한다.


