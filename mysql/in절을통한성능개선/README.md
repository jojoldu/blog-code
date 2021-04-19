# MySQL IN절을 통한 성능 개선 방법

MySQL의 IN절은 UNION으로 처리
즉, **eq 조건을 여러번 나눠서** 실행하는 것과 같은 효과
(범위 조건의 비효율에 포함되지 않음)
비효율적인 쿼리들을 개선하게 해주는 마법의 키워드


## 1. 범위 검색 개선

* UNI_TX_ITEM_SUM_1: tx_date, settle_code, give_cycle_code, customer_id
* IDX_TX_ITEM_SUM_1: settle_code, customer_id, tx_date


범위 검색 (BETWEEN) 의 비효율을 동등 비교 검색 (IN) 으로 개선하여 인덱스 최적화
결과) 33분 -> 0.1초로 개선

## 2. Loose Index Scan

* IDX_GIVE_1: settle_code, cycle_date

settle_code 조회 조건이 없어 
인덱스를 사용하지 못하고, 
테이블 풀 스캔 발생
settle_code에 전체 조건을 넣어 
index scan을 유도

## 3. 적정 개수

IN의 적정 사이즈는 200이하이며, 
네트워크 비용까지 고려해도 500정도가 적당.
1000개 이상할 경우 범위 검색 (between, <>, like 등) 으로 인덱스가 작동될 수 있다.

### JPA & Batch

하이버네이트를 쓸 경우 hibernate.default_batch_fetch_size를 100~200정도로 지정한다.
스프링 배치를 쓸 경우 chunkSize를 100~200정도로 지정한다.

## 4. 5.7 업데이트시 주의사항

MySQL 5.6에서 5.7로 업데이트시 
https://jobc.tistory.com/216