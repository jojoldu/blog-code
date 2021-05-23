# (기본적인) PostgreSQL 실행 계획 보는 법

* AWS RDS PostgreSQL 10.14
* m4.2xlarge


## 실행 순서

* 동일한 위치에선 **위에서 아래**로 실행되며 
* **그룹내 계층구조**에선 **가장 아래부터 시작**합니다.

**ex 1)**

```bash
1 ======  
2 ======  
3 ======  
```
실행순서 : 1 -> 2 -> 3

* 동일한 위치로 1,2,3이 있으므로 위에서 아래로 실행됩니다.

**ex 2)**

```bash
1 ======  
2  =====  
3   ====
```  
실행순서 : 3 -> 2 -> 1

* 1,2,3이 한그룹의 계층구조로 되어 있으므로 가장 아래부터 실행됩니다.

**ex 3)**

```bash
1 ======  
2  =====  
3  =====
```

실행순서 : 2 -> 3 -> 1

* 1 은 2, 3을 갖고 있으니 순서상 둘 보다 뒤로 밀리며 2,3은 동등한 위치이므로 둘 중 위에 선언된 2가 실행된 뒤 3이 실행되고 마지막으로 1이 실행됩니다.

**ex 4)**

```bash
1 ======  
2  =====  
3  =====  
4   ====  
5    ===
```

실행순서 : 2 -> 5 -> 4 -> 3 -> 1

* 1 아래에 2 3이 동등한 위치에 있으므로 2가 먼저 실행되고 
* 3은 같은 그룹으로 4,5 가 있어서 5 -> 4 -> 3 이 되고
* 마지막으로 1이 실행됩니다.


## 실습 환경

### 테스트 스키마

```sql
create table test_order(
    id          bigserial not null constraint test_order_pk primary key,
    customer_id varchar(20),
    comment     varchar(100),
    order_date  timestamp with time zone
);

CREATE TABLE test_order_detail(
    id         bigserial not null constraint test_order_detail_pk primary key,
    order_id   BIGINT    NOT NULL,
    product_id BIGINT    NOT NULL,
    comment    varchar(100),
    amount     BIGINT
);

CREATE INDEX idx_test_order_detail_01 ON test_order_detail (order_id);
CREATE INDEX idx_test_order_detail_02 ON test_order_detail (product_id);

CREATE TABLE test_product(
    id   bigserial    not null constraint test_product_pk primary key,
    name VARCHAR(100) NOT NULL
);
```

### 테스트 데이터 만들기

```sql
insert into test_order (customer_id, comment, order_date)
select 'C' || mod(i, 10) as customer_id, 
       lpad('X', 10, 'Y') as comment, 
       timestamp '1970-01-01 00:00:01' + random() * (timestamp '1970-01-01 00:00:01' - timestamp '2021-05-23 23:59:59') as order_date
from generate_series(1, 1000000) a(i);

INSERT INTO test_order_detail (order_id, product_id, comment, amount)
SELECT mod(i, 1000000) as order_id, 
       MOD(i, 5) as product_id, 
       lpad('X', 10, 'Y') as comment, 
       case when i < 1000 then i * 100 else i end as amount
FROM generate_series(1, 10000000) a(i);

INSERT INTO test_product (id, name)
SELECT product_id, MAX(order_id) || 'TEST_NAME'
FROM test_order_detail
GROUP BY product_id;
```

## 상황별 실행 계획

### 기본적인 실행계획 보는법 (feat. 테이블 풀 스캔)

**실행쿼리**

```sql
select * from test_order;
```

**실행계획**

```sql
Seq Scan on test_order  (cost=0.00..17353.00 rows=1000000 width=30)
```

* `Seq Scan`
  * 객체에 어떤 방식으로 조작하는지를 나타냄
  * `Seq Scan` 은 **파일을 순차적으로 접근**해서 해당 테이블의 전체 데이터를 읽음을 의미 (즉, 테이블 풀 스캔)
* `on test_order`
  * 조작 대상 객체
    * 테이블, View 등
  * 여기서는 `test_order` 테이블을 대상으로 한다는 것을 의미
* `rows=1000000`
  * 조작 대상 row 수
  * 즉, 현 조건에 해당하는 `test_order` 에서 조회될 예상 건수를 의미

### 인덱스 스캔

**실행쿼리**

```sql
select * from test_order where id between 1 and 10000;
```

**실행계획**

```sql
Index Scan using test_order_pk on test_order  (cost=0.42..422.13 rows=10885 width=30)
  Index Cond: ((id >= 1) AND (id <= 10000))
```

* `Index Scan using test_order_pk on test_order`
  * 인덱스를 이용해 Scan을 했음을 의미
  * 사용한 인덱스는 `test_order_pk` 이며, 대상 객체는 `test_order` 가 되었음을 알 수 있음 
* `Index Cond`
  * 인덱스 스캔에 사용된 조회 조건

만약 여러 조회조건이 섞여 있고, 이 중 인덱스를 사용하는 조건과 그렇지 않은 조건이 같이 있다면 다음과 같이 결과가 나온다.

**실행쿼리**

```sql
select *
from test_order
where id between 1 and 10000
  and order_date > '2020-01-01 00:00:00';
```

**실행계획**

```sql
Index Scan using test_order_pk on test_order  (cost=0.42..449.34 rows=1 width=30)
  Index Cond: ((id >= 1) AND (id <= 10000))
  Filter: (order_date > '2020-01-01 00:00:00+09'::timestamp with time zone)
```

* `Filter`
  * 인덱스가 아닌 일반적인 조건을 통해 데이터를 걸렀음을 의미
* 실행 순서

### Table Join

* Nested Loop
* Sort Merge
* Hash

```sql
select p.name
from test_order_detail d
join test_product p on d.product_id = p.id;
```

```sql
Hash Join  (cost=17.20..220072.69 rows=9999987 width=218) (1)
  Hash Cond: (d.product_id = p.id) (2)
  ->  Seq Scan on test_order_detail d  (cost=0.00..193457.87 rows=9999987 width=8) (3)
  ->  Hash  (cost=13.20..13.20 rows=320 width=226) (4)
        ->  Seq Scan on test_product p  (cost=0.00..13.20 rows=320 width=226) (5)
```

실행 순서

(3) `Seq Scan on test_order_detail d`
(5) `Seq Scan on test_product p`
(4) `Hash  (cost=13.20..13.20 rows=320 width=226)`
(2) `Hash Cond: (d.product_id = p.id)`
(1) `Hash Join  (cost=17.20..220072.69 rows=9999987 width=218)`

* 결합 (`HashJoin`) 전에 테이블 접근이 먼저 수행