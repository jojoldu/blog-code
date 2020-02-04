# 커버링 인덱스

커버링 인덱스란?

* SELECT 구문의 요청 칼럼과 WHERE 필터 등이 특정 인덱스의 구성 칼럼인 경우
* 데이터에 대한 접근 없이 인덱스 만으로 쿼리의 결과 생성 가능
* EXPLAIN 결과의 Extra 필드에 “Using index” 표시
* Clustered Key (PK) 의 값은 **모든 Non-Clustered Key에 포함**되어 있음
  * Clustered Key는 테이블당 1개만 존재한다.
  * PK가 없을 경우 유니크 키가 Clustered Key로
  * PK와 유니크키 둘다 없을 경우 6 byte의 Hidden Key를 생성 (rowid)
  * Non-Clustered Key에는 **데이터 블록의 위치가 없다**.
    * 즉, 인덱스외 다른 필드를 찾을때는 Non-Clustered Key에 있는 Clustered Key 값으로 데이터블록을 찾는 과정이 필요하다.

![clusterindex](./images/clusterindex.png)

(index-age는 age순으로 정렬되어있고, pk는 id순으로 정렬되어있다.)  
  
* 즉, **인덱스 키 조합에는 Clustered Key가 항상 포함**되어 있다.
* 다만 PK를 사용할 경우 인덱스 탐색 시간이 없어지기 때문에 향상된 데이터 파일 접근이 가능하다



|       | 표기        | 설명                               |
|-------|-------------|------------------------------------|
| Extra | Using index | 커버링 인덱스, 인덱스 range 스캔  |
| type  | index       | 인덱스 풀 스캔 (range 스캔이 아님) |

인덱스 풀 스캔 발생하는 경우

* range, const, ref와 같은 접근 방식으로 인덱스를 사용하지 못하는 경우
  * 위 조건과 더불어 아래 조건 중 하나가 동시 만족될 경우
    * 인덱스에 포함된 컬럼만으로 처리할 수 있는 쿼리인 경우 (즉, 데이터 파일을 안읽어도 되는 경우)
    * 인덱스를 이용해 정렬이나 그룹핑 작업이 가능한 경우 (즉, 별도의 정렬 작업을 피할 수 있는 경우)

* 커버링 인덱스 적용 순서
  * WHERE 조건에 있는 컬럼 최우선
  * ORDER BY / GROPU BY / HAVING 컬럼 그 다음
  * SELECT 에 있는 컬럼을 마지막

> ```Using temporary;```?  
> [공식 문서](https://dev.mysql.com/doc/refman/8.0/en/internal-temporary-tables.html) 에선 다음과 같은 조건에서 임시 테이블을 만들 수 있다고 합니다.  
> ORDER BY 절과 다른 GROUP BY 절이 있거나 ORDER BY 또는 GROUP BY에 조인 큐의 첫 번째 테이블 이외의 테이블의 열이 포함 된 경우 임시 테이블이 작성됩니다.
> ORDER BY와 결합된 DISTINCT에는 임시 테이블이 필요할 수 있습니다.
