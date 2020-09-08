# 페이징 성능 개선하기 (feat. Querydsl)

페이징을 어떻게 구현하느냐에 대해서는 이미 알고 계시다는 가정하에 진행하겠습니다.  

> 이 글은 이전에 사내 기술 블로그에 기고한 [Spring Batch와 Querydsl](https://woowabros.github.io/experience/2020/02/05/springbatch-querydsl.html)와 연결됩니다.

## 1. No Offset 으로 구조 변경하기

가장 먼저 진행해볼 방법은 기존 페이징 방식에서 No Offset으로 구조를 변경하는 것입니다.  
기존의 페이징 방식이 아래와 같이 **페이지 번호** (```offset```) 와 **페이지 사이즈** (```limit```) 를 기반으로 한다면

![legacy_pagination](./images/legacy_pagination.png)

No Offset은 아래와 같이 **페이지 번호 (offset)가 없는** 더보기 (More) 방식을 이야기 합니다.

![more_btn](./images/more_btn.png)

페이스북과 같은 대량의 데이터를 다루는 SNS의 오픈 API를 사용해보신 분들은 그와 **비슷**하다고 보시면 됩니다.  
(동일하진 않습니다.)

![facebook](./images/facebook.png)

## 1-1. No Offset은 왜 빠른가?

기존에 사용 하는 페이징 쿼리는 일반적으로 다음과 같은 형태입니다.

```sql
SELECT *
FROM items
WHERE 조건문
ORDER BY id DESC
OFFSET 페이지번호
LIMIT 페이지사이즈
```

> 제일 최신 데이터 -> 오래된 데이터 순으로 조회된다는 가정하에 ```desc```가 붙었습니다.  
> 모든 예제는 최신 -> 과거순으로 가정하고 진행합니다.

이와 같은 형태의 페이징 쿼리가 뒤로갈수록 느린 이유는 결국 **앞에서 읽었던 행을 다시 읽어야 하기 때문**인데요.

![nooffset_intro](./images/nooffset_intro.png)

예를 들어 ```limit 10000, 20``` 이라 하면 **10,020개의 행을 읽어야 합니다**.
그리고 이 중 앞의 10,000 개 행을 버리게 됩니다.
뒤로 갈수록 읽어야할 행의 개수가 많기 때문에 갈수록 느려지는 것입니다.

No Offset 방식은 바로 이 부분에서 **조회 시작 부분을 지정**해 매번 첫 페이지만 읽도록하는 방식입니다.

```sql
SELECT  *
FROM items
WHERE 조건문 
AND id < 마지막조회ID # 직전 조회 결과의 마지막 id
ORDER BY id DESC
LIMIT 페이지사이즈
```

이전에 조회된 결과를 한번에 건너뛸수 있게 마지막 조회 결과의 ID를 조건문에 사용하는 것으로 이는 쿼리가 매번 이전 페이지 전체를 건너 뛸 수 있음을 의미합니다.

즉, 아무리 페이지가 뒤로 가더라도 처음 페이지를 읽은 것과 동일한 효과를 가지게 됩니다.

> 좀 더 상세한 내용은 [fetch-next-page](https://use-the-index-luke.com/sql/partial-results/fetch-next-page) 를 참고해주세요.


## 1-2. 구현 컨셉



![nooffset_1](./images/nooffset_1.png)

![nooffset_2](./images/nooffset_2.png)

## 1-3. 실제 코드

## 1-4. 단점

* 회사 혹은 서비스 정책상 (or UX관점상) More 버튼형태로는 안된다고 하면 답이 없습니다.
  * 그럴땐 2번을 고려해주세요.
* 

## 2. 커버링 인덱스 사용하기

1번처럼 No Offset 방식으로 개선할 수 있다면 정말 좋겠지만, 회사 정책상 or 서비스 기획상 무조건 페이징 번호가 있어야 하는 방식이라면 사용할 순 없겠죠?  

> 커버링 인덱스란 **쿼리를 충족시키는 데 필요한 모든 데이터를 갖고 있는 인덱스**를 이야기합니다.  
> 이에 대한 상세한 내용은 이전에 작성된 포스팅 [커버링 인덱스 시리즈](https://jojoldu.tistory.com/476)를 참고해보시면 좋습니다.

## 3. 불필요한 Count 쿼리 제거하기

```sql
select
    book0_.id as id1_3_,
    book0_.book_no as book_no2_3_,
    book0_.book_type as book_typ3_3_,
    book0_.name as name4_3_ 
from
    book book0_ 
where
    book0_.book_type=? 
order by
    book0_.id desc limit ?, ?
```

불필요한 ```order by``` 와 ```select``` 을 제외해서 count쿼리를 실행해줍니다.

```sql
select
    count(book0_.id) as col_0_0_ 
from
    book book0_ 
where
    book0_.book_type=?
```

where로 걸러진 데이터가 1000만건, 1억건이여도 다 row를 읽어서 count를 구하기 때문입니다.  

### 3-1. 직접 Paging을 구현한 경우

### 3-2. QuerydslSupport의 Paging 사용할 경우


