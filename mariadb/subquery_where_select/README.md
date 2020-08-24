# MySQL 서브쿼리 vs 조인 조회 성능 비교 (Ver 5.5 & 5.6)

MySQL 5.5에서 5.6으로 업데이트가 되면서 서브쿼리(Subquery) 성능 개선이 많이 이루어졌습니다.  
  
이번 시간에는 MySQL 2개의 버전 (5.5, 5.6) 에서 서브쿼리를 통한 조회 (Select)와 Join에서의 조회간의 성능 차이를 비교해보겠습니다.  

> MySQL의 정석과도 같은 [Real MySQL](https://coupa.ng/bIKNBN) 책이 MySQL 5.5 버전을 기준으로 하다보니 5.6 변경분에 대해서 별도로 포스팅하게 되었습니다. 

## 0. 테스트 환경

테스트용 테이블은 2개를 만들었습니다.

* 메인 테이블 100만건
* 서브 테이블1 (인덱스 O) 1000건
* 서브 테이블2 (인덱스 X) 1000건

DDL 쿼리는 다음과 같습니다.  
  
**메인 테이블**

```sql
-- 업데이트 대상 테이블
create table main_table
(
    id int not null auto_increment,
    target_id int not NULL,
    primary key (id)
)ENGINE=InnoDB;
```

**서브 테이블1 (인덱스 O)**

```sql
-- 업데이트 조건 테이블 (인덱스 없음)
create table sub_table_noindex
(
    id int not null
)ENGINE=InnoDB;
```

**서브 테이블2 (인덱스 X)**

```sql 
-- 업데이트 조건 테이블 (인덱스 있음)
create table sub_table_index
(
    id int not null ,
    primary key (id)
)ENGINE=InnoDB;
```

자 그럼 실험을 해보겠습니다.

## 1. MySQL 5.5

거의 대부분의 분들이 알고 계시듯이 MySQL 5.5에서는 서브쿼리 최적화가 많은 문제를 갖고 있었습니다.  
  
> MSSQL이나 Oracle 쓰다가 MySQL 5.5 버전을 쓰시게되면 못쓸정도라고 평가하실 정도였습니다.

일반적으로 생각했을때 아래의 서브쿼리는 1번 쿼리가 먼저 수행되고, 그 결과를 2번 쿼리에서 수행할것처럼 보입니다.

즉, 아래와 같은 형태로 예상되는것이죠.

![이상적인_서브쿼리](./images/이상적인_서브쿼리.png)

하지만, MySQL 5.5 에서는 **서브쿼리가 우선 수행되지 않습니다**.  
  
> 서브쿼리 최적화가 5.6에서 적용되었습니다.

### Subquery

테스트할 쿼리는 아래와 같습니다.

```sql
select * 
from main_table_55
where target_id in (
    select id 
    from sub_table_noindex_55 
    where id < 500
);
```

많은 분들이 즐겨쓰시는 **세미조인 서브쿼리** 입니다.  
  
**No Index**

![55_no_index](./images/55_no_index.png)

![55_no_index_time](./images/55_no_index_time.png)

**Index**

![55_index](./images/55_index.png)

![55_index_time](./images/55_index_time.png)

그렇다면 위 쿼리를 Join으로 풀면 얼마나 성능 차이가 발생할까요?  

### Join

**No Index**

```sql
select *
from main_table_55 m
    join sub_table_noindex_55 s
        on m.target_id = s.id
where s.id < 500;
```

![55_no_index_time_join](./images/55_no_index_time_join.png)

**Index**

```sql
select *
from main_table_55 m
    join sub_table_index_55 s
        on m.target_id = s.id
where s.id < 500;
```

![55_index_time_join](./images/55_index_time_join.png)

> Join 외에도 [Exist로 서브쿼리 최적화](https://weicomes.tistory.com/325)를 할 수도 있습니다.

## 2. MySQL 5.6

### Subquery

**No Index**

![56_no_index](./images/56_no_index.png)

![56_no_index_time](./images/56_no_index_time.png)

![56_no_index_extended](./images/56_no_index_extended.png)

**Index**

![56_index](./images/56_index.png)

![56_index_time](./images/56_index_time.png)

![56_index_extended](./images/56_index_extended.png)


## 결론

* 버전 관계 없이 좋은 성능을 내려면 최대한 Join을 이용하자
* Join을 사용하기가 너무 어렵다면 Subquery는 사용하되, MySQL 5.5 이하라면 절대 사용하지 않는다.
  * 5.5 이하이면 차라리 쿼리를 나눠서 실행하는게 낫다.
