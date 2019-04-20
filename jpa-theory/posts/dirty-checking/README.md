# 더티 체킹이란?

데이터베이스에서 간단한 Entity를로드하고 업데이트하는 아래 코드를 고려하십시오.
public  static void testUpdate () {
    세션 세션 = sessionFactory.openSession ();
    트랜잭션 트랜잭션 = session.beginTransaction ();    
    엔티티 엔티티 = (엔티티) session.load (엔티티 클래스, 1);
    entity.setData ( "데이터 업데이트" );
    transaction.commit ();
    session.close ();
}
session.update (entity) 호출을하지 않았지만 로그는 데이터베이스 레코드가 성공적으로 업데이트되었음을 ​​나타냅니다.
3094 [main] DEBUG org.hibernate.persister.entity.AbstractEntityPersister - Upda
ting entity : [com.model.Entity # 1]
3094 [main] DEBUG org.hibernate.jdbc.AbstractBatcher - 약 PreparedSt를 열려고합니다.
atement (열린 PreparedStatements : 0, 전역 적으로 : 0)
3094 [main] DEBUG org.hibernate.SQL - 
    최신 정보
        실재 
    세트
        DATA =? 
    어디에
        id =?
업데이트 호출이 수행되지 않은 경우 업데이트는 어떻게 수행 되었습니까? 성공적으로 저장되는 이유는 자동 더티 검사 입니다.
모든 트랜잭션이 끝날 때, Hibernate는이 트랜잭션에서 변경된 모든 객체를 데이터베이스에 유지시키기 위해 그 자체를 취한다 . Hibernate는 변경되거나 더러운 모든 객체를 탐지 할 수있다 . 이것은 PersistenceContext 의 도움으로 수행 됩니다. PersistenceContext 내에서 Hibernate는 데이터베이스로부터로드 된 모든 영속 객체들의 복사본을 가지고 있다. 영구 오브젝트와이 오브젝트를 비교하여 수정 된 오브젝트를 감지합니다 . 이것은 기본 구현입니다. 


트랜잭션이 끝날 때, Hibernate는 적절한 테이블 잠금을 획득하고 테이블의 레코드를 업데이트하며 획득 된 모든 잠금을 해제하여 트랜잭션을 완료한다.
Hibernate는 우리 자신의 커스텀 dirty checking 알고리즘의 구현을 허용한다. 이것은 세션을위한 org.hibernate.Interceptor 인터페이스 의 findDirty () 메소드를 구현함으로써 이루어진다 .
class DirtyChecker 는 인터셉터 {

```java

    @보수
    공개  INT [] findDirty ( 객체 엔티티 직렬화 ID는
             객체 []의 currentState을 개체 [] previousState는
             문자열 {[]로 propertyNames은 [] 타입 타입)
         // 반환 값은 엔티티가 갱신되었는지 여부를 판단한다 
        // 그것은 배열을 반환 엔티티를 나타내는 속성 인덱스 더럽 
        // 또는 빈 어레이 - 엔티티가 깨끗한 지         
        복귀  널 ;
    }
// 더 많은 메소드 ...
}

```
