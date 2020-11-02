# Index Merge Intersection 실행 계획 의심하기

MySQL은 예외없이 주어진 쿼리에서 테이블 당 하나의 인덱스 만 사용할 수있었습니다. 이 제한을 이해하지 못한 사람들은 종종 WHERE 절에 일반적으로 나타나는 열에 단일 열 인덱스가 많은 테이블을 가지고 있으며 주어진 SELECT에 대한 EXPLAIN 계획이 N 개의 가능한 인덱스 선택 사항을 표시하는 이유를 궁금해합니다. 실제로 사용되는 인덱스 하나.

Index Merge Intersection

MySQL 5.0 이상은 "인덱스 병합"최적화 계획의 도입으로이 상황을 어느 정도 변경했습니다. 인덱스 병합의 기본 개념은 단일 열 인덱스가있는 열이있는 WHERE 절을 포함하는 특정 유형의 쿼리에 대해 MySQL 이때때로 여러 인덱스를 사용합니다. 예를 들어,“SELECT foo FROM bar WHERE indexed_colA = X OR indexed_colB = Y”는 * 동시에 * (아래에서 볼 수 있듯이 중요합니다) indexed_colA 및 indexed_colB의 인덱스를 스캔 한 다음 인덱스 병합 통합 알고리즘을 사용할 수 있습니다. 두 결과 집합의 집합 이론적 결합을 수행합니다. "OR"을 "AND"로 바꾸면 결과 집합의 집합 이론적 교차를 볼 수 있습니다. 아마도 이것은 상당한 성능 승리가 될 수 있습니다. 때때로 그렇습니다. 다른 경우에는 주요 성능 킬러입니다.

MySQL이이 작업을 수행하는시기를 알기는 매우 간단합니다. SELECT에서 EXPLAIN을 실행하면 "추가"필드에 쿼리 유형과 인덱스 병합 알고리즘 유형으로 "index_merge"가 표시됩니다. 다음은 실제로 index_merge를 사용하려고 시도했지만 결과가 실망스러운 최근에 만난 테이블과 쿼리의 예입니다.


**두 조건이 모두 효율적이지 않은 경우** MySQL 옵티마이저는 **2개의 조건을 분리해서 각각의 인덱스로 검색을 2번 실행**한다.
여러 Merge 알고리즘이 있지만, 여기서 소개할 "Index Merge Intersection" 는 WHERE 조건이 OR로 연결된게 아닌, AND로 연결된 경우에 사용할 수 있는 옵션이다.

* [The Optimization That (Often) Isn’t: Index Merge Intersection](https://www.percona.com/blog/2012/12/14/the-optimization-that-often-isnt-index-merge-intersection/)