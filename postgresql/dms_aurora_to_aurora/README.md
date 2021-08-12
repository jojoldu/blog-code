# DMS를 이용하여 별도의 Aurora PostgreSQL Replica 만들기

```sql
create user dms_migration PASSWORD '특수문자없는 비밀번호';
grant inflabreadonly to dms_migration;
```

```sql
create user dms_migration_subscriber PASSWORD '특수문자없는 비밀번호' valid until 'infinity';
grant rds_superuser to dms_migration_subscriber;
```



## Source (소스) DB 설정

* `logical_replication`: 1로 설정
* `rds_superuser` 및 `rds_replication` 이 Role에 있어야 함

```sql
grant rds_superuser to dms_migration;
grant rds_replication to dms_migration;
```

```sql
\du
```


* https://aws.amazon.com/ko/blogs/aws/amazon-rds-for-postgresql-new-minor-versions-logical-replication-dms-and-more/


