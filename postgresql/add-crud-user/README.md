# PostgreSQL 사용자 계정에 CRUD 권한만 넣기

```sql
SELECT * FROM pg_catalog.pg_user;
```

PostgreSQL 과는 달리 Schema는 Database가 아닌 table의 집합을 schema 라고 표현하며 이 schema는 하나의 database를 논리적으로 나누는 개념입니다.  
즉, MySQL에서의 논리 database는 PostgreSQL에서의 schema라고 할 수 있습니다

```sql
CREATE ROLE developer WITH LOGIN ENCRYPTED PASSWORD 'password';

create user developer_service WITH LOGIN ENCRYPTED PASSWORD 'password@#4';

SELECT * FROM pg_user u;
SELECT * FROM pg_roles;

REVOKE ALL
ON ALL TABLES IN SCHEMA public
FROM PUBLIC;

REVOKE CONNECT ON DATABASE developer FROM PUBLIC;

GRANT CONNECT
ON DATABASE developer
TO database;

GRANT CONNECT
ON DATABASE developer
TO developer;

REVOKE ALL
ON ALL TABLES IN SCHEMA public
FROM developer;

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO developer;

-- grant schema to role
grant usage on schema public to developer;

-- grant dml
grant select, insert, update, delete on all tables in schema public to developer;

-- grant role to user
grant developer to developer_service;


```