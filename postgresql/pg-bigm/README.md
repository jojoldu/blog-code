# Amazon Aurora PostgreSQL 에서 pg_bigm 확장 사용하기

![intro](./images/intro.png)

2021.06.17 드디어 Amazon Aurora PostgreSQL 에서 [pg_bigm](https://pgbigm.osdn.jp/pg_bigm_en-1-2.html)을 지원하게 되었습니다.

* [release notes](https://aws.amazon.com/ko/about-aws/whats-new/2021/06/amazon-aurora-postgresql-supports-pg-bigm-extension-for-faster-full-text-search/)

기존까지는 Aurora가 11.9 / 12.4 까지만 지원하여서 `pg_bigm` 을 쓰려면 **PostgreSQL Amazon RDS** 를 사용해야만 했는데요.  
  
Aurora PostgreSQL 버전이 업데이트 되면서 (11.11 / 12.6 / 13.2) 가 드디어 Aurora에서도 `pg_bigm` 를 쓸 수 있게 되었습니다.

## 1. 지원 대상

모든 Aurora PostgreSQL에서 `pg_bigm`을 사용할 수 있는것은 아닙니다.  
아래 PostgreSQL 호환 버전에서만 가능한데요.

* PostgreSQL 13 : PostgreSQL 13.2 이상
* PostgreSQL 12 : PostgreSQL 12.6 이상
* PostgreSQL 11 : PostgreSQL 11.11 이상
* PostgreSQL 10.x : 지원 X
* PostgreSQL 9.x  : 지원 X

현재는 **각 메이저의 가장 최신 버전** Aurora에서만 사용할 수 있습니다.  

## 2. pg_bigm?

이를테면 아래와 같은 쿼리는 일반적인 RDBMS에서 **인덱스를 사용할 수 없습니다**.  

```sql
select *
from posts
where body like '%튜닝%'
```

일반적인 B-Tree 인덱스의 경우, `LIKE` 를 통한 검색은 `검색어%`만 인덱스를 탈 수 있습니다.  
이유는 인덱스가 **LEFT-TO-RIGHT** 방식이기 때문인데요.  
지금처럼 검색어 (`튜닝`) 앞에 `%`가 붙은 경우에는 인덱스를 사용할 수가 없게 됩니다.  
  
뿐만 아니라 B-Tree 인덱스는 **짧은 문자열 혹은 숫자** 타입의 컬럼에서 효과적인데, 대량의 문자열이 있는 (`TEXT`)의 경우에 B-Tree 인덱스는 효과적이지 못합니다.  
(이를테면 블로그의 본문에서 `like` 검색을 해야한다면 **HTML과 텍스트가 섞여있다**보니 엄청나게 많은 문자열이 있는 컬럼에서 해야하는것이죠.)  
  
그래서 기존 하위 버전에서는 이 문제를 `pg_trgm`을 통해 전체 텍스트 검색 기능에 대한 성능 문제와 인덱스 문제를 해결하곤 했는데요.  
  
그럼 이번 버전부터 사용가능한 `pg_bigm`과 기존에도 사용가능했던 `pg_trgm` 는 어떤 차이가 있을까요?

### 2-1. pg_trgm vs pg_bigm

3-gram(trigram) 모델을 이용한 전체 텍스트 검색 기능을 제공 하는 pg_trgm contrib 모듈이 포함되어 있습니다. pg_bigm은 pg_trgm을 기반으로 개발되었습니다. 다음과 같은 차이점이 있습니다.


| 기능                                   | `pg_grgm`       | `pg_bigm`       |
| -------------------------------------- | --------------- | --------------- |
| 전체 텍스트 검색에 필요한 최소 단어수  | 3단어           | 2단어           |
| 사용 가능한 인덱스                     | `GIN`,  `GiST`  | `GIN`           |
| 사용 가능한 검색 연산자                | `like`, `ilike` | `like`          |
| 한국어 지원 여부                       | X               | O               |
| 1-2자 키워드로 전체 텍스트 검색시 성능 | 느림            | 빠름            |
| 유사성 검색                            | O               | O (1.1버전부터) |
| 최대 인덱스 컬럼 사이즈                | ~228MB          | ~102MB          |


2-gram

pg_bigm 모듈은 PostgreSQL 에서 전체 텍스트 검색 기능을 제공 합니다.  
이 모듈을 사용하면 더 빠른 전체 텍스트 검색 을 위해 2그램 (빅 그램 ) 인덱스 를 만들 수 있습니다.  

pg_bigm을 사용하면 2그램(바이그램) 인덱스를 생성하여 다중 바이트 문자로 인코딩된 텍스트의 전체 텍스트 검색 속도를 개선할 수 있습니다.

## 3. 설치

```sql
SELECT * FROM pg_extension;
```

![extension1](./images/extension1.png)

```sql
CREATE EXTENSION pg_bigm;
```

```sql
SELECT * FROM pg_extension;
```

![extension2](./images/extension2.png)


## 4. 사용법

```sql
CREATE INDEX posts_body_gin ON posts USING gin (body gin_bigm_ops);
```

```sql
SELECT indexname, indexdef
  FROM pg_indexes
 WHERE tablename = 'posts';
```


전체 텍스트 검색에있어서 pg_bigm을 도입하여 효과가있는 것은 나름대로 데이터의 양이 많거나 자주 액세스가있어 한번의 검색 당 처리량을 줄이는 데 의의가있는 경우라고 할 수 있습니다. 인덱스가 더 해지면 INSERT 또는 UPDATE에는 인덱스 업데이트 처리가 가해지게되므로 도입 할 것인지 신중한 판단이 필요합니다.

![index1](./images/index1.png)

![index2](./images/index2.png)

![index3](./images/index3.png)

### 4-1. 성능

![index4](./images/index4.png)

![index5](./images/index5.png)


### 4-2. likequery

```sql
select * from posts where body like '%pg_bigm은 전체 텍스트 검색 성능을 200% 향상시켰습니다%';
```

![likequery1](./images/likequery1.png)

```sql
select * from posts where body like '%pg_bigm은 전체 텍스트 검색 성능을 200\% 향상시켰습니다%';
```

![likequery2](./images/likequery2.png)


```sql
select * from posts where body like likequery('pg_bigm은 전체 텍스트 검색 성능을 200% 향상시켰습니다');
```

![likequery3](./images/likequery3.png)

## 참고

* [pg_bigm 1.2 공식 문서](https://pgbigm.osdn.jp/pg_bigm_en-1-2.html)
