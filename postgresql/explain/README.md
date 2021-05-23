# PostgreSQL 실행 계획 보는 법


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

* 1 아래에 2 3이 동등한 위치에 있으므로 2가 먼저 실행되고 3은 아래에 4,5가 있습니다. 
* 4는 역시 5를 아래에 두고 있기 때문에 5 -> 4 -> 3 실행후 마지막으로 1이 실행됩니다.


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

### 테이블 풀 스캔

