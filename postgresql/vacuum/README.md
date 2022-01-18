# PostgreSQL Vacuum

```sql
-- DB 전체 풀 실행
vacuum full analyze;

-- DB 전체 간단하게 실행
vacuum verbose analyze;

-- 해당 테이블만 간단하게 실행
vacuum analyse [테이블 명];

-- 특정 테이블만 풀 실행
vacuum full [테이블명];
```

사람마다 청소하는 방범이 있듯 PostgreSQL도 청소 기준을 가지고 있습니다.
그 기준을 FSM(Free Space Map)이라고하는데, 더 이상 필요하지 않는 행의 정보를 보유하고 있습니다.  
  
이 정보는 실제로 사용되지는 않지만 용량을 차지하고 있고, 새로운 행이 삽입될때 DBMS는 FSM의 여유 공간을 확인하여 해당 행을 사용하게 됩니다.
그리고 FSM 공간은 용량이 제한되어 있기때문에 주기적으로 청소하는게 좋습니다.

PostgreSQL 은 MVCC 에 의해 이전의 값들이 갱신되거나 삭제되더라도

새로운 데이터로 추가(갱신 경우)하고 기존의 데이터는 정리하지 않고 삭제표기만 남겨둡니다.

이렇게 사용하지 않는 값들로 인해 파일 사이즈가 커져 성능저하가 발생하게 되는걸 방지합니다.

 

Transaction ID 가 겹침으로 인해 자료의 손실이 발생하는 것을 방지합니다.

이전의 데이터가 정리되면서 실행계획에서 사용할 통계치 정보를 최신으로 갱신해 줍니다.

Visibility MAP ( VM = Dead Tuple 의 존재 여부를 표기 ) 의 정보를 갱신해 줍니다.


아래 쿼리는 튜플(Tuple)에 대한 정보를 확인 할 수 있는 쿼리문입니다.

```sql
SELECT
    n.nspname AS schema_name,
    c.relname AS table_name,
    pg_stat_get_live_tuples(c.oid) + pg_stat_get_dead_tuples(c.oid) as total_tuple,
    pg_stat_get_live_tuples(c.oid) AS live_tuple,
    pg_stat_get_dead_tuples(c.oid) AS dead_tupple,
    round(100*pg_stat_get_live_tuples(c.oid) / (pg_stat_get_live_tuples(c.oid) + pg_stat_get_dead_tuples(c.oid)),2) as live_tuple_rate,
    round(100*pg_stat_get_dead_tuples(c.oid) / (pg_stat_get_live_tuples(c.oid) + pg_stat_get_dead_tuples(c.oid)),2) as dead_tuple_rate,
    pg_size_pretty(pg_total_relation_size(c.oid)) as total_relation_size,
    pg_size_pretty(pg_relation_size(c.oid)) as relation_size
FROM pg_class AS c
JOIN pg_catalog.pg_namespace AS n ON n.oid = c.relnamespace
WHERE pg_stat_get_live_tuples(c.oid) > 0
AND c.relname NOT LIKE 'pg_%'
ORDER BY dead_tupple DESC;
```

## Vacuum 실행

### 자동

```sql
ALTER TABLE public.courses SET (autovacuum_enabled = on);
ALTER TABLE public.courses SET (autovacuum_enabled = off);
```

### 수동


```sql
vacuum VERBOSE public.courses;
```