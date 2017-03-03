# Validation 공통모듈 만들기
안녕하세요? 이번시간엔 유효성 체크(이하 validation) 공통 모듈 생성하는 예제를 진행해보려고 합니다.
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>

예를 들어 아래와 같은 회원 가입 양식이 있다고 가정해보겠습니다.  
![회원가입양식](./images/회원가입폼.png)

대부분의 입력 양식처럼 위 양식도 등록시 몇가지 조건들이 있습니다.  
* 이름/휴대폰번호/이메일은 필수값입니다.
* 휴대폰번호는 **10자리 혹은 11자리의 숫자**로 이루어져야 합니다.
* 이메일은 이메일 양식을 지켜야한다 (xxx@xxx)

* resources/static/index.html을 생성하면 "/" 접속시 자동으로 index.html을 호출한다

![IntelliJ 자동반영 설정](./images/자동반영설정.png)
