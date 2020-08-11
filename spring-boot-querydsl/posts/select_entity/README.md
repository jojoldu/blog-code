# Querydsl Select절에서 Entity 사용시 주의 사항

JPA 기반의 애플리케이션에서 복잡한 조회에 한해서는 Querydsl을 많이 사용하곤 합니다.


Customer(parent) - Shop(child) 관계라면 객체 생성시 N+1 쿼리가 발생한다.

![n+1_query](./images/n+1_query.png)