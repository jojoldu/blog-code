# Amazon RDS Aurora (PostgreSQL) 에서 pg_bigm 확장 사용하기

## 지원 대상

* PostgreSQL 13 : PostgreSQL 13.2 이상
* PostgreSQL 12 : PostgreSQL 12.6 이상
* PostgreSQL 11 : PostgreSQL 11.11 이상
* PostgreSQL 10.x : 지원 X
* PostgreSQL 9.x  : 지원 X

기존까지는 Aurora가 11.9 / 12.4 까지만 지원하여서 `pg_bigm` 을 쓰려면 PostgreSQL Amazon RDS 를 사용해야만 했는데요.  
최근 (2021.06.17) Aurora가 업데이트 되면서 드디어 Aurora에서도 `pg_bigm` 를 쓸 수 있게 되었습니다.

* https://aws.amazon.com/ko/about-aws/whats-new/2021/06/amazon-aurora-supports-postgresql-12-6-11-11-10-16-and-9-6-21/

## pg_bigm?

pg_bigm을 사용하면 2그램(바이그램) 인덱스를 생성하여 다중 바이트 문자로 인코딩된 텍스트의 전체 텍스트 검색 속도를 개선할 수 있습니다.


### pg_tram vs pg_bigm

## 설치

```sql
SELECT * FROM pg_extension;
```

```sql
CREATE EXTENSION pg_bigm;
```

```sql
SELECT * FROM pg_extension;
```

## 사용법

```sql
CREATE INDEX posts_body_gin ON posts USING gin (body gin_bigm_ops);
```

```sql
SELECT indexname, indexdef
  FROM pg_indexes
 WHERE tablename = 'posts';
```

![index1](./images/index1.png)




