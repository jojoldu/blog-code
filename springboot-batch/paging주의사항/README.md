# Spring Batch Paging 구현시 주의 사항

안녕하세요? 이번 시간엔 Spring Batch Paging 구현시 주의 사항을 공유드리려고 합니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/springboot-batch)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>
 
 
## 문제 상황

수백만의 데이터에서 조건에 맞는 데이터를 추출하여 가공하는 Spring Batch를 구현해야했습니다.  
Chunk Size를 설정한 후 Spring Batch를 실행하였는데 몇몇 데이터가 **누락**되기도 하고, **중복**되기도 하는 문제가 발생하였습니다.  
금액에 관련된 문제이기에 급하게 원인을 분석하기 시작했습니다.

* **총 갯수가 맞았습니다**.
  * 4만개가 호출되어야 하는 상황이였는데 정확히 4만개가 추출되었습니다.
  * 하지만 **총 합계 금액이 맞지 않았습니다**.

**총 수는 맞는데 총합이 맞지 않다는 것**을 확인후, Processor나 Writer의 문제가 아닌, **Reader의 문제**라고 생각하였습니다.  
(Writer나 processor의 문제였다면 1자리까지 갯수가 맞을 확률은 거의 없기 때문입니다.)  
즉, Reader에서 **Chunk Size만큼 불러올때 중복되거나 누락된 데이터**를 가져왔을 것이라 의심하였습니다.  
  
당시에 사용한 Reader는 아래와 같다고 가정하겠습니다.  
(실제로 이렇게 간단하진 않지만 문제가 되는 부분은 똑같다고 보시면 됩니다.)

```java
    private Step step() {

        return stepBuilderFactory.get(STEP_NAME)
                .<ShopOrder, OrderHistory>chunk(CHUNK_SIZE)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    private JpaPagingItemReader<ShopOrder> reader() {
        JpaPagingItemReader<ShopOrder> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select o from ShopOrder o join fetch o.customer c where c.id=1");
        reader.setPageSize(CHUNK_SIZE);

        return reader;
    }
```

많은 데이터를 Chunk로 분할 처리하셨던 분들이면 바로 아실 수 있으실텐데요,  
문제는 reader에 **정렬조건이 없었기 때문**입니다.  
위 reader를 Chunk Size까지 조합하여 실행시키는 쿼리를 확인해보면 아래와 같습니다.  

```sql
select o 
from ShopOrder o join fetch o.customer c 
where c.id=1
limit 시작포인트, CHUNK_SIZE
``` 

Chunk 를 통한 분할 조회가 문제였습니다.  
(문제라기 보단 제가 잘못 짠거죠^^;)  
예를 들어 위 쿼리의 총 결과 데이터수가 4만건이며 Chunk size가 1만이라면 **4번의 쿼리가 limit의 시작포인트만 변경**된채 수행됩니다.  

```sql
select o 
from ShopOrder o join fetch o.customer c 
where c.id=1
limit 0, 10000
``` 

```sql
select o 
from ShopOrder o join fetch o.customer c 
where c.id=1
limit 10000, 10000
``` 

```sql
select o 
from ShopOrder o join fetch o.customer c 
where c.id=1
limit 20000, 10000
``` 

....  
  
(이런식으로 총 4번이 수행됩니다.)  
  
각각 별도로 수행되는 쿼리다보니, **정렬기준이 정해져있지 않으면 쿼리마다 본인들만의 정렬기준을 만들어 실행**하게 됩니다.  
그러다보니 앞에서 조회했던 데이터가 다음 조회 쿼리의 정렬기준에 포함되기도 하고, 빠지기도 했던 것입니다.  

## 해결책

해결책은 가장 보편적인 방법과 번외편 2가지가 있습니다.

### 1. Order by

가장 보편적인 방법은 정렬기준을 포함시키는 것입니다.  

```order by id```와 같이 **queryString에 고유한 정렬기준을 포함**시키면 정상적으로 페이징 처리 결과를 받을 수 있습니다.  

### 2. CursorItemReader

보편적이진 않지만 굳이 **정렬기준을 포함시키고 싶지 않으신 분**들에게 제안드리고 싶은 방법은 ```CursorItemReader```를 사용하는 것입니다.  
  
Jpa의 구현체는 없지만 Jdbc, Hibernate, MyBatis에는 **CursorItemReader**라는 Reader구현체가 있습니다.  
ResultSet과 직접 연동하여 데이터를 읽어오는 것인데, 일종의 stream 과 같다고 보시면 됩니다.  
사실상 전체를 조회하여 stream처럼 지속적으로 데이터를 가져오는 방식이기에 페이징 이슈는 발생하지 않습니다.  
또한, 성능 역시 PagingItemReader보다 좋습니다.
  
보통 ```QuerydslCursorItemReader```, ```JpaCursorItemReader```, ```JooqCursorItemReader``` 등 팀내 **Query 프레임워크에 맞게 커스텀 구현체**를 생성하여 사용합니다.  
(다음 포스팅에선 해당 Reader의 구현체를 만들어보겠습니다.)

그럼 무조건 Cursor가 더 좋지 않냐고 생각하실텐데, 몇가지 문제가 있습니다.

* 한번에 가져오는 데이터 양이 많을 경우 Batch 가 뻗어버립니다.
  * Paging해서 조회하는것이 아니기에 전체 조회 결과 데이터가 클 경우 문제가 발생할 수 있습니다.
  * 약 400만 데이터에서 총 10만개, Chunk Size 2000으로  조회시에도 CursorItemReader에서 문제가 발생하지 않음을 사내 테스트로 확인하였습니다.  
* Thread Safe 하지 않습니다.
  * Multi Thread로 Batch를 구현해야하는 상황이라면 PagingItemReader를 사용하셔야 합니다.
  * [참고](https://stackoverflow.com/questions/28719836/spring-batch-problems-mix-data-when-converting-to-multithread)

위와 같은 단점이 있지만, 성능 자체가 뛰어나기에 **대량의 데이터가 아니고, 멀티쓰레드 환경이 아닌 곳**에서는 CursorItemReader를 사용하고 있습니다.

## 마무리

국내에 아직 SpringBatch 서적이 없어서 삽질을 많이 하는것 같습니다.  
소문으로는(!?) 아주 고수님께서 관련 서적을 준비중이신걸로 들었지만, 아직 출판되지 않아 간절히 기다리고 있습니다.  
저처럼 Spring Batch 사용시 삽질 하시지 않게, 문제가 발생하고 해결할때마다 열심히 기록하겠습니다.  
끝까지 봐주셔서 감사합니다^^

## 참고

* [SpringBatch JpaPagingItemReader SortOrder? - stackoverflow](https://stackoverflow.com/questions/37710535/springbatch-jpapagingitemreader-sortorder)