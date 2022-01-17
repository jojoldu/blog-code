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

아래 쿼리는 튜플(Tuple)에 대한 정보를 확인 할 수 있는 쿼리문입니다.

