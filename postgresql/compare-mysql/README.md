# MySQL 개발자가 PostgreSQL을 사용할 때 알아야할 점들

## 타임존

* KST, UTC 등을 지정할 필요가 없다.
* `timestamp with time zone` 을 쓰면 된다
  * 수동으로 뽑더라도 접속을 해야 하니 그 세션에 타임존이 세팅되니까?

## 인코딩

* utf8만 있고, utf8mb4가 없다.
  * 이모지는 mb4가 아니여도 가능하다.

