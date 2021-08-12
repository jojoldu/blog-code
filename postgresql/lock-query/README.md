# (AWS Aurora) PostgreSQL Lock Query Kill

```sql
SELECT pid, query
FROM pg_stat_activity
where state = 'active'
  and wait_event_type = 'Lock';
```

```sql
SELECT pg_cancel_backend(pg_stat_activity.pid)
```

```sql
SELECT pg_cancel_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE state = 'active'
  and wait_event_type = 'Lock'
  AND pid <> pg_backend_pid();
```

```sql
select pg_terminate_backend(pid);
```