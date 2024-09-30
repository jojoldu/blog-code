# API 타임존

국제화를 하면서 기존 KST의 시간값들을 어떻게 다룰 것인가?

## 0. 데이터베이스와 경계선

### 데이터베이스

모든 시간 데이터를 데이터베이스에서는 UTC 기준으로 전용 날짜 타입 (`timestamp` 등) 으로 관리되어야 한다.  
만약 KST 등 특정 시간대로 컬럼이 생성되고 관리되고 있다면, UTC 기반의 신규 컬럼을 추가하여 점진적으로 신규 컬럼으로 API를 이관한다. 

### 구조

User <-> Client (Web / App) <-> Server <-> Database 구조에서 경계선을 확실히 그어야한다.  
  
- Server <-> Database 는 UTC로만 통신한다.
- Client (Web / App) <-> Server 도 UTC로만 통신한다.
- User <-> Client (Web / App) 는 User의 타임존에 맞게 Client (Web / App)가 처리한다.

## 1. 기준 시간
기존 날짜 필드는 그대로 두고 신규 UTC 필드를 추가한다.

신규 필드명은 XXXAtUTC 로 정한다.

asis)

```
"createdAt": "2024-09-13 14:52:34"
```

tobe)

```
"createdAt": "2024-09-13 14:52:34",
"createdAtUTC": "2024-09-13T05:52:34.000Z" // 신규 필드
```

### 1-1. 사유
아직 국제화가 진행되지 않는 다수의 페이지들과 앞으로도 국제화가 진행되지 않을 페이지들이 존재하는 상황에서 기존 API들을 수정하면 날짜 오류가 발생할 여지가 있다.

웹 페이지들 중 일부는 날짜 변환을 하지 않고, API 에서 넘겨준 데이터를 그대로 노출시키는 경우가 존재하는데, 이걸 위해 기존의 KST 타임존 값을 변경시키지 않고 유지시킨다.

## 2. 날짜 포맷

ISO 8601 포맷을 사용한다.

asis)

```
"createdAt": "2024-09-13 14:52:34"
```

tobe)

```
"createdAtUTC": "2024-09-13T05:52:34.000Z" 
```

### 2-1. 사유

날짜를 식별하는데 모호함이 없다.

다양한 시간대에 대해 명시적으로 표시할 수 있다.

사람이 읽을 수 있어 개발 및 문제 해결에 도움이 된다.

표준 날짜 포맷이기에 대부분의 언어 표준 라이브러리는 이 형식으로 날짜를 구문 분석하고 직렬화하는 방법을 제공한다.

 

## 참고)

- [The 5 laws of API dates and times](https://apiux.com/2013/03/20/5-laws-api-dates-and-times/) 
- [REST API date format best practices](https://criteria.sh/blog/rest-api-date-format-best-practices) 