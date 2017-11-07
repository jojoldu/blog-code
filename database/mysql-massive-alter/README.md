# 대용량 테이블 스키마 변경하기

데이터가 100만, 1000만 정도일때는 테이블 스키마 변경은 ```alter table```로 가능했습니다.  
천만건이라도 ```alter table```은 5분안에 끝났기 때문입니다.  
하지만 1억건이 넘어가면 이야기가 달라집니다.  
3~4시간동안 진행될수도 있기 때문에 다른 방법으로 진행해야합니다.  
(대단한 방법은 아닙니다^^;)  

간단한 내용이지만 필요하실 분들이 계실것 같아 작성하였습니다.

## 본문

전체적인 과정은 간단합니다.  
복사테이블 생성 -> 복사테이블에 원본 테이블 데이터 복사 -> 테이블 이름 변경  
순으로 진행됩니다. 그 사이에 짜잘한 내용들을 추가하였습니다.  

> 모든 내용은 정기점검 등으로 **외부에서 DB 사용이 중지된때**라는 가정하에 진행합니다.  
소개드리는 방법이라 하더라도 40~50분정도의 시간이 필요합니다.  

### 1. 기존 테이블의 FK 삭제

기존 테이블에 FK 제약조건이 있을 경우엔 바로 카피 테이블을 생성할수 없습니다.  
**FK는 이름이 유니크해야하므로, 기존 테이블의 FK를 삭제해야만** 진행할 수 있습니다.  

```sql
ALTER TABLE 테이블명 DROP FOREIGN KEY FK이름
```

> FK 컬럼은 지우지 않습니다. 복사해야하므로..

### 2. 카피 테이블 생성

기존 테이블에서 스키마가 변경된 테이블을 새로 만듭니다.  
(예를 들어 컬럼이 하나 추가되어야 하거나, 삭제되어야 하는 등)  
이때 인덱스는 모두 추가하여 생성합니다.  
  
ex)  

```sql
CREATE TABLE item_copy (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  amount bigint(20) NOT NULL,
  code varchar(255) DEFAULT NULL,
  customer_id bigint(20),
  new_column varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK_ITEM_CUSTOMER (customer_id),
  CONSTRAINT FK_ITEM_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

### 3. Select Insert 실행

새로 생성된 카피 테이블에 기존 테이블 데이터를 ```select insert``` 합니다.

```sql
INSERT INTO 카피테이블 (카피테이블의 컬럼명들)
SELECT 원본테이블 컬럼명들 FROM 원본테이블;
```

여기서 원본 테이블 상태에 따라 FK 제약조건이 발생할 수 있습니다.  
예를 들어 **```item``` 테이블이 갖고 있는 FK 컬럼인 ```customer_id```가 ```customer```에는 없는 값** 이라 복사가 안되는 등의 문제입니다.  
이럴때는 복사하는 동안 제약조건 체크를 OFF 하고, 복사가 끝난 후 다시 ON 하는 것이 좋습니다.  
  
제약조건 OFF)

```sql
SET FOREIGN_KEY_CHECKS=0
```

제약조건 ON)

```sql
SET FOREIGN_KEY_CHECKS=1
```

### 4. 테이블명 변경

먼저 기존 테이블 명을 임의의 다른 이름으로 변경합니다.  

> 개인적으로는 백업을 위해서 원본테이블명_날짜 형식으로 이름짓습니다.  
복사가 잘못되었거나, 다시 복구해야하는 등의 문제가 발생할 수 있기 때문입니다.

```sql
RENAME TABLE `item` TO `item_20171108`;
```

이후 복사 테이블을 원본테이블 명으로 변경합니다.  

```sql
RENAME TABLE `item_copy` TO `item`;
```

## 마무리

이렇게 하시면 **1억 4천만 row를 가진 테이블도 스키마 변경에 40~50분**이면 완료되었습니다.  
혹시나 비슷하게 고민하고 계셨던 분들에게 도움이 되었으면 합니다.  
끝까지 읽어주셔서 고맙습니다^^
