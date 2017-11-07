# 대용량 테이블 스키마 변경하기

## 본문

### 1. 기존 테이블의 FK 삭제

만약 기존 테이블에 FK 제약조건이 있을 경우 FK 이름이 유니크해야하므로, 기존 테이블의 
### 2. 카피 테이블 생성

기존 테이블에서 스키마가 변경된 테이블을 새로 만듭니다.  
이때 인덱스는 모두 추가하여 생성합니다.  
  
ex)  

```sql
CREATE TABLE table_copy (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount bigint(20) NOT NULL,
  code varchar(255) DEFAULT NULL,
  customer_id bigint(20),
  PRIMARY KEY (id),
  KEY FK_TABLE_CUSTOMER (customer_id),
  CONSTRAINT FK_TABLE_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

### 3. Select Insert 실행

```sql
INSERT INTO 카피테이블 (카피테이블의 컬럼명들)
SELECT 원본테이블 컬럼명들 FROM 원본테이블;
```

```sql
SET FOREIGN_KEY_CHECKS=0
```

```sql
SET FOREIGN_KEY_CHECKS=1
```

### 4. 테이블명 변경

