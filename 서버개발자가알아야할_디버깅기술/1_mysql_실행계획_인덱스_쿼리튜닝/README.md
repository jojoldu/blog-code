# 1. 서버 개발자가 알아야할 디버깅 기술 - 인덱스/실행계획/쿼리튜닝

## 실행 계획 (EXPLAIN)

* MySQL이 어떻게 쿼리를 실행하는지에 관한 정보를 보여주는 명령어
* 5.6.3이전 버전은 SELECT만 가능(Update, Delete의 경우 SELECT로 변환하여 수행해야 함)
* 5.6.3 이후 버전은 SELECT, DELETE, INSERT, UPDATE, REPLACE 가능

* id: SELECT identifier
* select_type: **SELECT 유형**
* table: 테이블명 또는 Alias
* type: **JOIN 유형(Access 유형)**
* possible_keys: 쿼리처리를 위해 옵티마이저가 고려한 인덱스 후보
* key: **옵티마이저가 실제 선택한 인덱스**
* key_len: 선택된 인덱스에서 실제 사용한 인덱스의 길이
* ref: 조회를위해사용된상수또는컬럼
* rows: 쿼리를 수행하기 위해 처리해야 하는 Row의 개수( 옵티마이저의 추정치 )
* Extra: **쿼리 처리에 사용된 부가정보**


