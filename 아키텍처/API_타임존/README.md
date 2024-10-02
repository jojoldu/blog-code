# API 타임존

우리 서비스
국제화를 하면서 기존 KST의 시간값들을 어떻게 다룰 것인가?

## 0. 데이터베이스와 경계선

### 데이터베이스

모든 시간 데이터를 데이터베이스에서는 UTC 기준으로 전용 날짜 타입 (`timestamp/datetime`) 으로 관리되어야 한다.  
  
만약 기존 시스템이 `KST` 등 특정 국가의 시간대로 컬럼이 관리되고 있었다면, UTC 기반의 신규 컬럼을 추가하여 점진적으로 신규 컬럼으로 API를 이관한다.  

### 서버 애플리케이션

PostgreSQL 에서는 `Timestamp with timezone` 타입이 지원되기 때문에 UTC 컬럼을 사용해도 큰 영향은 없다.  
  
Spring & Hibernate 와 같은 환경에서는 다음과 같이 애플리케이션에서 UTC 기반으로 처리할 수 있도록 지원한다.

```bash
#for plain hibernate
hibernate.jdbc.time_zone=UTC

#for Spring boot jpa
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
```

UTC(Docker 기본값은 UTC)에서 데이터베이스를 실행하는 경우 항상 TZ 환경 변수를 사용하여 시간대를 변경할 수 있다.  
기본 Docker 컨테이너는 UTC로 구성한다.  
다른 사용 사례의 경우 DB가 서버 시스템에서 실행 중인 경우 로컬 시스템 기본 시간대를 사용하지만 대부분의 최신 RDBMS에서는 시간대를 쉽게 변경할 수 있다.

### 구조

User <-> Client (Web / App) <-> Server <-> Database 구조에서 경계선을 확실히 그어야한다.  
  
- Server <-> Database 는 UTC로만 통신한다.
- Client (Web / App) <-> Server 도 UTC로만 통신한다.
- User <-> Client (Web / App) 는 User의 타임존에 맞게 Client (Web / App)가 처리한다.


## 1. 기준 시간 (신규 컬럼)

기존 날짜 필드 (KST) 는 그대로 두고 신규 UTC 필드를 추가한다.  
신규 필드명은 `XXXAtUTC` 로 정한다.

asis)

```
"createdAt": "2024-09-13 14:52:34"
```

tobe)

```
"createdAt": "2024-09-13 14:52:34",
"createdAtUTC": "2024-09-13T05:52:34.000Z" // 신규 필드
```

좋은 방법은 아니나, 레거시 시스템에 대한 개편 일정을 알 수 없는 상태에서 기존 시스템의 사이드 이펙트를 최대한 줄이면서 갈 수 있는 방법을 선택한다.  
  
현재 가지고 있는 레거시 시스템은 다음과 같은 상황이다.

- KST 타임존을 가지고 Web, APP 등 여러 환경에서 사용중이다.
- 모든 시스템이 국제화로 개편될 것이 아니라서, 이후에도 해당 시스템을 개편할지 미지수이다.
- 기존 API의 날짜 포맷을 그대로 화면에 노출하는 경우가 존재한다.

이런 상황에서 기존 필드를 바로 UTC로 수정하는 것은 여러 사이드 이펙트를 일으킨다.  
  
물론 API를 버저닝 하여 v1까지는 KST, v2부터는 UTC로 해도 된다.  
다만, 이럴 경우 레거시 시스템 개편전까지 2개 버전의 API를 계속 관리 해야한다.  
  
시스템 개편은 비즈니스 목표에 의존된다.  
전체적인 시스템이 신규 컬럼에 맞춰 UTC 기준으로 처리하도록 개편이 되면 이후에 버저닝으로 정상적인 컬럼명으로 변경한다.

> Q) 그럼 앞으로 `createdAtUTC` 를 영영 날짜 필드에 대한 컨벤션으로 가져가나?  
> `createdAtUTC` 로 모든 날짜 사용이 통일 되어서 그 어디에도 `createdAt` 필드를 사용하지 않을때 2차 마이그레이션 시작
> 어디에도 `createdAt` 을 사용하지 않으면 `createdAt` 필드를 다시 추가하여 이 필드도 UTC값을 가지도록 한다.
> 즉, `createdAtUTC` & `createdAt` 이 둘다 UTC를 사용함
앤트맨 & Next.js 등 `createdAtUTC` 를 쓰는 시스템들은 어느값을 사용하든 똑같이 UTC이기 때문에 점진적으로 `createdAt` 으로 이관한다.
모든 시스템이 UTC로 된 `createdAt` 을 사용하게 되면 그때 `createdAtUTC` 를 제거한다.

## 2. 날짜 포맷

날짜와 시간을 문자열로 형식화하기 위해 국제적으로 채택된 표준인 [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601)이 이미 있다.  
그러니 ISO 8601 포맷을 사용한다.

asis)

```
"createdAt": "2024-09-13 14:52:34"
```

tobe)

```
"createdAtUTC": "2024-09-13T05:52:34.000Z" 
```

해당 포맷을 사용할 경우 

- 날짜를 식별하는데 모호함이 없다.
- 다양한 시간대에 대해 명시적으로 표시할 수 있다.
- 사람이 읽을 수 있어 개발 및 문제 해결에 도움이 된다.
- 표준 날짜 포맷이기에 대부분의 언어 표준 라이브러리는 이 형식으로 날짜를 구문 분석하고 직렬화하는 방법을 제공한다.

 

## 참고)

- [The 5 laws of API dates and times](https://apiux.com/2013/03/20/5-laws-api-dates-and-times/) 
- [REST API date format best practices](https://criteria.sh/blog/rest-api-date-format-best-practices) 