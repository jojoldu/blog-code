# 실행 중인 프로세스 확인 및 실행 계획 보기

## 실행 중인 프로세스

```sql
show processlist;
```



## 실행중인 쿼리의 실행 계획 보기

MariaDB 10 버전부터 지원

> 현재는 10.4.x (2019.12.04) 까지 출시되었습니다.


```sql
show explain for 프로세스id;
```