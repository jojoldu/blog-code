# SpringBoot Test 사용하기
[공식문서](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html) 를 참고하며 기록하는 SpringBoot Test 적용하기
 
## 1. SpringBootTest

## 2. @WebMvcTest

## 3. @DataJpaTest

### 3.1 상황1
* 사용자(Member)는 0개 혹은 다수의 글(Post)를 가질수 있다. 대신 Post는 꼭 Member가 있어야 하는것은 아니다.
* 이럴 경우 Member와 Post는 선택적(Optional) 참조이므로 조인컬럼으로는 해결할수가 없다.
* 그래서 조인테이블을 사용해 문제를 해결해야 한다.


