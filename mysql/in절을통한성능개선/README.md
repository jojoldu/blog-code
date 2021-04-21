# MySQL IN절을 통한 성능 개선 방법

잘 사용되진 않는 범위 조건 중에 `IN` 이 있습니다.  
일반적으로 `BETWEEN`, `LIKE`, `<>` 에 비해서는 **하나씩 모든 Key를 입력**해야 되어 잘 선호되진 않는데요.  
  
이 `IN` 절을 통해 여러 성능 개선들이 가능해서 생각보다 활용하기에 따라 많은 성능 개선을 이룰 수 있습니다.  
  
이번 시간에는 `IN` 을 통한 2가지 개선 방법을 소개 드리겠습니다.

## 0. 소개

MySQL의 IN절은 UNION으로 처리
즉, **eq 조건을 여러번 나눠서** 실행하는 것과 같은 효과
(범위 조건의 비효율에 포함되지 않음)
비효율적인 쿼리들을 개선하게 해주는 마법의 키워드


## 1. 범위 검색 개선

아래와 같이 
* UNI_TX_ITEM_SUM_1: tx_date, settle_code, give_cycle_code, customer_id
* IDX_TX_ITEM_SUM_1: settle_code, customer_id, tx_date

![1-1](./images/1-1.png)

![1-2](./images/1-2.png)

![1-3](./images/1-3.png)

![1-4](./images/1-4.png)

범위 검색 (BETWEEN) 의 비효율을 동등 비교 검색 (IN) 으로 개선하여 인덱스 최적화
결과) 33분 -> 0.1초로 개선

## 2. Loose Index Scan

* IDX_GIVE_1: settle_code, cycle_date

![2-1](./images/2-1.png)

![2-2](./images/2-2.png)

![2-3](./images/2-3.png)

![2-4](./images/2-4.png)

settle_code 조회 조건이 없어 
인덱스를 사용하지 못하고, 
테이블 풀 스캔 발생
settle_code에 전체 조건을 넣어 
index scan을 유도

## 3. 적정 개수

* [MySQL5.6 IN(val1, ..., valN) 를 index range scan 작동원리](http://small-dbtalk.blogspot.com/2016/02/)

`eq_range_index_dive_limit` 
IN의 적정 사이즈는 200이하이며, 
네트워크 비용까지 고려해도 500정도가 적당.
1000개 이상할 경우 범위 검색 (between, <>, like 등) 으로 인덱스가 작동될 수 있다.

### JPA & Batch

하이버네이트를 쓸 경우 hibernate.default_batch_fetch_size를 100~200정도로 지정한다.
스프링 배치를 쓸 경우 chunkSize를 100~200정도로 지정한다.

## 4. 5.7 업데이트시 주의사항

MySQL 5.6에서 5.7로 업데이트시 아래와 같이 IN절 개수에 따라 갑자기 테이블 풀 스캔이 될 수 있음을 주의해야 합니다.

https://jobc.tistory.com/216