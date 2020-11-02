# Xtrabackup으로 DB 백업하기

## 로그 적용

Xtrabackup 을 이용해서 백업이 수행되면 **백업 파일을 생성하는 과정에 추가된 데이터**들이 남아있을 수 있습니다.  


```bash
innobackupex —-apply-log 백업디렉토리
```

예를 들어 저같은 경우 아래와 같이 적용합니다.

```bash
innobackupex --apply-log /data/mysql/backup/
```


```bash
InnoDB: Shutdown completed; log sequence number 215489192
```