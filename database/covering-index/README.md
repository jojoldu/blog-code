# 커버링 인덱스



* 커버링 인덱스 적용 순서
    * WHERE 조건에 있는 컬럼 최우선
    * ORDER BY / GROPU BY 컬럼 그 다음
    * SELECT 에 있는 컬럼을 마지막