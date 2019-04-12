# Hibernate Item Reader에서 failed to lazily initialize a collection 발생시

안녕하세요? 이번 시간엔 Spring batch에서 Hibernate Item Reader를 사용시에 failed to lazily initialize a collection 발생시
 어떻게 해결하는지 확인해봅니다.    
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/spring-batch-n1)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  

## 1. 문제 상황

Spring Batch에서는 여러 ItemReader들이 있고, 이 중


### StatelessSession?

Hibernate는 또한 사용자가 자신의 SQL 업데이트를 관리하는 것으로 간주하는 다른 유형의 Session을 제공합니다.  
이러한 유형의 Hibernate 세션에는 영속 컨텍스트의 개념 이 없습니다. 
따라서 최대 절전 모드는 자동 변경 ( 더티 체크 ) 을 감지하고 업데이트를 자동으로 예약 할 수 없습니다. 
데이터베이스에서로드 된 객체 의 캐시 가 없기 때문에 한 세션에서 동일한 SQL 쿼리를 실행하면 Java 메모리에 두 개의 다른 객체가 생길 수 있습니다.
지연된 쓰기, 1 차 레벨 캐시와 같은 세션 인터페이스의 모든 이점은 상실됩니다. 
이 인터페이스를 StatelessSession 이라고합니다


> 참고: https://docs.jboss.org/hibernate/orm/4.0/devguide/en-US/html/ch04.html 