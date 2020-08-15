# Bulk Update 시 주의사항

JPA로 가장 많이 받는 질문 중 하나가 대량의 데이터를 업데이트할 때 어떻게 하는게 

## 더티체킹 보다는 일괄처리로

## Subquery는 사용하지 않는다.

## 동적값 할당이면 Jdbc Batch로 처리

먼저 이 옵션이 JdbcUrl에 설정되어있지 않으면 **Bulk Operation이 수행되지 않습니다**.  

```sql
rewriteBatchedStatements=true
```

저 옵션 없이 수행해보시면 MySQL에서 수행되는 쿼리를 확인해보면 단건으로 처리되는 것을 확인할 수 있습니다.  
  
