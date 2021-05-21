# PostgreSQL 사용자 계정에 CRUD 권한만 넣기

```sql
SELECT * FROM pg_catalog.pg_user;
```

PostgreSQL 과는 달리 Schema는 Database가 아닌 table의 집합을 schema 라고 표현하며 이 schema는 하나의 database를 논리적으로 나누는 개념입니다.  
즉, MySQL에서의 논리 database는 PostgreSQL에서의 schema라고 할 수 있습니다

```sql
CREATE ROLE developer WITH LOGIN ENCRYPTED PASSWORD 'password';

create user developer_service WITH LOGIN ENCRYPTED PASSWORD 'password@#4';
```

```sql
MASTER=newappo
SERVICE_USER=newsvc
DBNAME=newdb

-- 신규 유저 생성
psql -d postgres -U postgres -c "create user ${MASTER} WITH LOGIN ENCRYPTED PASSWORD 'secret'"
psql -d postgres -U postgres -c "create user ${SERVICE_USER} WITH LOGIN ENCRYPTED PASSWORD 'secret'"

-- db 접근 권한 및 public schema 접근 권한 revoke
psql -d ${DBNAME} -U ${MASTER} -c "revoke all on database ${DBNAME} from public"
psql -d ${DBNAME} -U postgres -c "revoke all on schema public from public"

-- 신규db에 schema 생성 
psql -d ${DBNAME} -U ${MASTER} -c "create schema ${MASTER} authorization ${MASTER}"

-- 서비스용 유저에 최소한의 권한만 부여
psql -d ${DBNAME} -U ${MASTER} -c "grant connect,TEMPORARY on database ${DBNAME} to ${SERVICE_USER}"
psql -d ${DBNAME} -U ${MASTER} -c "grant usage on schema ${MASTER} to ${SERVICE_USER}"
psql -d ${DBNAME} -U ${SERVICE_USER}  -c "alter role ${SERVICE_USER} set search_path to ${MASTER}"
psql -d ${DBNAME} -U ${MASTER}  -c "grant select, insert, update, delete on all tables in schema ${MASTER} to ${SERVICE_USER}"
psql -d ${DBNAME} -U ${MASTER}  -c "alter default privileges in schema ${MASTER} grant select, insert, update, delete on tables to ${SERVICE_USER}"
psql -d ${DBNAME} -U ${MASTER}  -c "grant usage on all sequences in schema ${MASTER} to ${SERVICE_USER}"
psql -d ${DBNAME} -U ${MASTER}  -c "alter default privileges in schema ${MASTER} grant usage on sequences to ${SERVICE_USER}"
```


* https://m.blog.naver.com/hanccii/221701395102