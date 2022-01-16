# NOT IN 쿼리 성능 개선하기 (PostgreSQL)


```sql
SELECT id 
FROM table1 
WHERE id NOT IN (
    SELECT itemid FROM table2
);
```

```sql
SELECT id 
FROM table1
WHERE NOT EXISTS (
   SELECT * FROM TABLE2 WHERE table2.itemid=table1.id
  );
```

```sql
SELECT id 
FROM table1
LEFT JOIN table2 ON table1.id=table2.itemid
WHERE table2.itemid IS NULL
```
