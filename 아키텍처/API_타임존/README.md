# API 타임존

우리 서비스
국제화를 하면서 기존 KST의 시간값들을 어떻게 다룰 것인가?

# 1. 데이터베이스와 경계선

## 1-1. 구조

User <-> Client (Web / App) <-> Server <-> Database 구조에서 경계선을 확실히 그어야 한다.

![1](./images/1.png)

- **User <-> Client (Web / App)** 는 **User의 타임존에 맞게** Client (Web / App)가 처리한다.
- **Server <-> Database** 는 **UTC**로만 통신한다.
- **Client (Web / App) <-> Server** 는 **UTC** 와 **KST**를 함께 전달한다.
  - Request Body, Response Body에는 UTC 타임 필드와 KST 타임 필드를 함께 포함시킨다.
  - 이는 국제화가 미 진행된 시스템이 있어 항상 기존 타임값을 보장하기 위함이다.
- **Server <-> Server** 에서도 **UTC** & **KST**로 통신한다.
  - 위와 마찬가지로 기존 시스템의 영향도를 최소화 하기 위함이다.

모든 비즈니스 로직을 UTC로 처리 한다.  
  
사용자가 보고 있는 화면에서는 사용자의 타임존에 맞게 (Web, APP에서) 처리해야하지만 사용자의 액션 전, 액션 이후 API 통신이 발생하는 시점에는 항상 UTC로 치환한 결과를 전달한다.  
그래야만 사용자의 타임존에 관련된 로직이 Client View 계층에서만 격리화 되어 관리 된다.  
  
격리 영역을 정해두지 않으면 타임존 치환 로직이 여기저기 흩어져 로직상 문제가 되는 지점이 분명히 발생한다.  
  
여러 계층에서 타임존 이슈를 고민하기 보다는 UTC로 처리하는 영역을 최대한 넓히고, 타임존 이슈를 최대한 격리화된 영역으로 몰아서 처리한다.
  
## 1-2. 데이터베이스

### RDBMS

- 모든 시간 데이터는 UTC 기준으로 전용 날짜 타입 (timestamp/datetime) 으로 데이터베이스에서 관리되어야 한다.
  - 만약 기존 시스템이 KST 등 기준으로 생성된 컬럼이 있었다면, **UTC 기반의 신규 컬럼을 추가**하여 점진적으로 신규 컬럼으로 API를 이관한다.
  - (현 시스템 내에는 없음)
  
- **주문/결제에 관련된 모든 프로세스(승인, 요청중, 완료, 취소 등) 에서 발생하는 관련된 시간값**은 UTC 로 값을 저장하고, 추가로 **해당 유저가 접속한 국가 코드와 타임존을 함께 DB에 보관**한다.
  - 이는 결제완료 등에 관련하여 이메일 발송, 문자 발송 등에 해당 이벤트 처리시간이 포함될 때 결제 당시의 국가의 타임존으로 노출시키기 위함이다.
  - 또한 CS 응대시에도 해당 유저 입장에서 언제 결제했다고 할 때 빠르게 응대하기 위함이다.

### MongoDB

ISODate 타입: MongoDB의 Date 타입은 기본적으로 UTC로 저장된다.  

ISODate()로 생성하면 자동으로 UTC 시간으로 저장되며, 클라이언트에서 가져온 데이터를 MongoDB에 저장할 때 UTC로 변환하는 추가 작업이 필요 없다

```js
db.collection.insertOne({ createdAt: new Date() });  // UTC 시간으로 저장
```

### Redis

UTC 시간 문자열 저장: Redis는 데이터 타입이 제한적이기 때문에, UTC 시간을 문자열 형식(예: ISO 8601 또는 UNIX 타임스탬프)으로 저장한다.  
클라이언트에서 UTC 시간을 가져와 문자열로 저장하고, 읽을 때 변환하는 방식으로 처리할 수 있다.

```bash
SET event:timestamp "2024-11-01T10:00:00Z"  # ISO 8601 UTC 형식
```

### 1-2-1. 유저 접속 국가 코드

[Cloudfront에서는 다음과 같이 현재 웹 페이지 접근자의 IP와 국가코드를 가져올 수 있는 방법](https://docs.aws.amazon.com/ko_kr/AmazonCloudFront/latest/DeveloperGuide/adding-cloudfront-headers.html) 을 지원한다.

- Public API는 위와 같이 CF를 통해 정보를 가져오고 (Public API에는 CF 가 다 추가될 예정)
  - CSR, SPA, APP 에서는 국가 코드와 IP를 전달하지 않는다.
  
- Private API는 Next.js 에서 Request Body로 전달받는다.
  - Next.js에서는 이 정보를 가져와서 전달한다.

## 1-3. 서버 애플리케이션

서버 애플리케이션의 타임존도 **원칙대로라면 UTC로 고정 시켜야만 한다**.

다만 이렇게 되면, 기존에 처리하던 KST 기준의 모든 API들에 문제가 발생한다.  
그래서 당장의 서버 애플리케이션 설정들은 모두 기존과 동일하게 서버 타임존에 의존하게 한다.  
(Docker 등 애플리케이션 구동 환경에 애플리케이션 타임존이 세팅된다)

### 1-3-1. 마이그레이션

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



# 2. 기준 시간 (신규 컬럼)

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
팀 내 규칙으로 `createdAt`과 같이 기존 시간 값들은 무조건 KST 타임존이라고 명확하게 하는 것이 혼란을 덜 일으킨다.  
  
어떤 시스템은 `createdAt` 이 KST이고, 어떤 시스템은 `createdAt`이 UTC 이면 **동일한 필드명이 사내에서 서로 다른 의미로 사용** 된다.  
  
**좋은 규칙이 아니더라도 명확한 규칙이 어설픈 좋은 규칙보다 낫다**.    
  
물론 API를 버저닝 하여 v1까지는 KST, v2부터는 UTC로 해도 된다.  
다만, 이럴 경우 레거시 시스템 개편전까지 2개 버전의 API를 계속 관리 해야한다.  
  
시스템 개편은 비즈니스 목표에 의존된다.  
전체적인 시스템이 신규 컬럼에 맞춰 UTC 기준으로 처리하도록 개편이 되면 이후에 버저닝으로 정상적인 컬럼명으로 변경한다.

> Q) 그럼 앞으로 `createdAtUTC` 를 영영 날짜 필드에 대한 컨벤션으로 가져가나?  
> A) `createdAtUTC` 로 모든 날짜 사용이 통일 되어서 그 어디에도 `createdAt` 필드를 사용하지 않을때 2차 마이그레이션 시작한다.  
> 어디에도 `createdAt` 을 사용하지 않으면 `createdAt` 필드를 다시 추가하여 이 필드도 UTC값을 가지도록 한다.
> 즉, `createdAtUTC` & `createdAt` 이 둘다 UTC를 사용함
레거시 & Next.js 등 `createdAtUTC` 를 쓰는 시스템들은 어느 값을 사용하든 똑같이 UTC이기 때문에 점진적으로 `createdAt` 으로 이관한다.
모든 시스템이 UTC로 된 `createdAt` 을 사용하게 되면 그때 `createdAtUTC` 를 제거한다.

# 3. 날짜 포맷

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

국가별 로컬 형식(예: MM/DD/YYYY와 YYYY/MM/DD)으로 제공하면 데이터 해석 오류가 발생할 수 있고, 다른 문화권에서 혼란을 초래할 가능성이 높다.   

해당 포맷을 사용할 경우 

- 날짜를 식별하는데 모호함이 없다.
- 다양한 시간대에 대해 명시적으로 표시할 수 있다.
- 사람이 읽을 수 있어 개발 및 문제 해결에 도움이 된다.
- 표준 날짜 포맷이기에 대부분의 언어 표준 라이브러리는 이 형식으로 날짜를 구문 분석하고 직렬화하는 방법을 제공한다.

## UI 날짜 포맷

국가별 최적화된 포맷을 지원하기 보다는 가능한 단일 포맷을 지원한다.  
국가별로 표준 포맷에 대해 로컬라이징을 너무 하면 다음과 같이 width 잡기가 어렵다.

- [기본값] ko: 2024. 12. 25. // en: 12/25/2024
- [축약형] ko: 24. 12. 25. // en: 12/25/24
- [+시간값] ko: 2024. 12. 25. 21:00 // en: 12/25/2024 21:00

# 4. 백엔드간 통신


UTC로 시간을 표기한다.  

기본적으로 백엔드간 통신에서도 API 규칙을 그대로 따른다.

- UTC 기반의 신규 필드 (`xxxAtUTC`) 추가
- ISO 8601 (`2024-09-13T05:52:34.000Z`) 사용

기존 레거시 시스템에 별도로 문제를 일으키지 않도록 기존 시간 필드는 수정하지 않는다.

## 4-1. 예외적 타임존 처리 - Email 등

한국 유저에게는 KST, 그 외 유저에게는 UTC로 시간을 표기한다.
유데미, 페이팔 등에서도 이메일 속 시간을 사용자별 타임존에 맞춰 노출시키지 않고, 기준시간을 정하고 사용한다.


# 5. 서머타임 (Daylight Saving Time, DST) 처리

이유: 특정 지역에서는 DST를 적용하며, 시기에 따라 시간이 변동될 수 있다. 이를 미리 고려하지 않으면 잘못된 시간대 데이터가 반환될 수 있다. 따라서 DST를 고려해 타임존 데이터를 자동으로 업데이트하거나 DST 대응이 가능한 라이브러리를 사용하는 것이 중요하다.

예시:
타임존 변환 라이브러리를 사용할 때 DST 처리를 지원하는 API를 활용하거나, 필요한 경우 주기적으로 DST 변경 정보를 업데이트한다.

문제점: DST 변동을 고려하지 않으면 예상과 다른 시간대 데이터가 출력되거나, 일부 국가에서는 혼란을 야기할 수 있다. 이로 인해 중요한 일정에서 시간이 뒤틀리거나 잘못된 알림이 전송될 위험이 있다.

# 참고)

- [The 5 laws of API dates and times](https://apiux.com/2013/03/20/5-laws-api-dates-and-times/) 
- [REST API date format best practices](https://criteria.sh/blog/rest-api-date-format-best-practices) 