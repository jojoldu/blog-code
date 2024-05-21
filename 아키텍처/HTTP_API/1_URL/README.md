# 1. HTTP API 디자인 - URI편

[마틴 파울러의 블로그](https://martinfowler.com/articles/richardsonMaturityModel.html)를 가보면 Leonard Richardson이 제안한 [HTTP API 성숙도 모델 (Richardson Maturity Model - RMM)](https://martinfowler.com/articles/richardsonMaturityModel.html)을 소개한다.  
여기서는 다음과 같이 4단계를 설명한다.
  
- 레벨 0: 하나의 URI를 정의하고 모든 작업은 이 URI에 대한 POST 요청  
- 레벨 1: 개별 리소스에 대해 별도의 URI를 만든다.  
- 레벨 2: HTTP 메서드를 사용하여 리소스에 대한 작업을 정의
- 레벨 3: 하이퍼미디어(HATEOAS 등)를 사용

Roy Fielding 의 정의에 따르면 레벨 3이 진정한 RESTful API에 해당한다.  
[대부분의 RESTful API는 진짜 RESTful API는 아니며 보통 레벨2 부근](https://www.youtube.com/watch?v=RP_f5dMoHFc)에 해당한다.      

아래는 마틴 파울러 블로그에 나와있는 [성숙도 레벨이 무엇을 의미하는지에 대햔 섹션](https://martinfowler.com/articles/richardsonMaturityModel.html#TheMeaningOfTheLevels)이다.  
본문을 읽기 전 읽어보면 좋다.  

> RMM (Richardson 의 성숙도 모델) 은 REST의 요소가 무엇인지 생각하는 좋은 방법이지만 **REST 자체의 수준을 정의하는 것은 아니라는 점**을 강조하고 싶습니다.  
> **Roy Fielding은 레벨 3이 REST의 전제 조건**임을 분명히 했습니다.  
> 소프트웨어의 많은 용어와 마찬가지로 REST에도 많은 정의가 있지만 Roy Fielding이 이 용어를 만든 이후로 그의 정의는 대부분의 것보다 더 많은 의미를 지닙니다.  
> 이 성숙도 모델의 유용한 점은 **RESTful 사고의 기본 아이디어를 단계별로 이해할 수 있는 좋은 방법**을 제공한다는 점입니다.  
> 따라서 이 모델은 평가 메커니즘에 사용해야 하는 것이 아니라 개념을 배우는 데 도움이 되는 도구라고 생각합니다.  
> 아직은 RESTful 접근 방식이 시스템을 통합하는 올바른 방법이라고 확신하기에는 아직 충분한 사례가 없다고 생각하지만, 매우 매력적인 접근법이며 대부분의 상황에서 권장하고 싶은 접근법이라고 생각합니다.  
> 
> Ian Robinson과 이에 대해 이야기하면서 그는 Leonard Richardson이 이 모델을 처음 발표했을 때 이 모델에 대해 매력을 느꼈던 점은 일반적인 디자인 기술과의 관계였다고 강조했습니다.  
>   
> **레벨 1**은 **분할 및 정복을 사용하여 대규모 서비스 엔드포인트를 여러 리소스로 분할함**으로써 복잡성을 처리하는 문제를 해결합니다.
**레벨 2**는 **표준 동사 집합을 도입하여 유사한 상황을 동일한 방식으로 처리**하여 불필요한 변형을 제거합니다.
**레벨 3**은 발견 가능성을 도입하여 **프로토콜을 보다 자체적으로 문서화**할 수 있는 방법을 제공합니다.  
> 
> 그 결과 우리가 제공하고자 하는 HTTP 서비스의 종류에 대해 생각하고 이 서비스와 상호 작용하고자 하는 사람들의 기대치를 설정하는 데 도움이 되는 모델이 탄생했습니다.

여기서는 레벨2를 기준으로 팀에서 사용하는  HTTP API  컨벤션을 소개한다.  

> 모든 컨벤션에는 절대적인 것은 없으며, 팀의 상황, 프로젝트의 상황에 따라 언제든 **우리만의 규칙을 만드는 것**이 중요하다.
  
## 기본

좋은 HTTP API는 아래 규칙들을 전재로 한다.  
이 전제들을 기반으로 좀 더 상세한 규칙들을 소개한다.

#### 모든 API는 직관적이어야 한다  

이 URI만 봐도 무엇을 하는 것인지 상세 스펙을 보지 않아도 알 수 있어야 한다.  

#### 리소스는 테이블과 1:1 매칭 대상이 아니다

API는 리소스를 중심으로 디자인되며, 여기서 이야기 하는 **리소스는 클라이언트에서 접근할 수 있는 모든 종류의 개체, 데이터 또는 서비스 등 모든 것이 포함**된다.  
  
리소스는 추상화된 계층이지, **테이블과 1:1 매칭을 해야하는 것으로 오해해선 안된다**.  

#### Stateless API 를 구성한다

(REST API를 가장한) **HTTP API는 Stateless (상태 비저장) 요청 모델**을 사용해야 한다.  
  
 **요청 간에 일시적인 상태 정보를 유지하는 것은 불가능**하다.
HTTP 요청은 독립적이라 요청 순서가 정해져있다던가 해선 안되며, 언제, 어느순서로도 요청할 수 있다.    
  
이로 인해 클라이언트가 어느 특정 서버로 연결 해야할지 별도로 관리할 필요가 없기 때문에 **모든 서버는 모든 클라이언트의 모든 요청을 처리할 수 있다**.  
그리고 이로 인해 확장성이 우수한 API를 구축할 수 있다 
  
  
아래부터 자세하게 이야기한다.

## URI에 API 을 표기한다

API 임을 URI에서 표현되어야 한다.  
크게 2가지로 나눠진다.  

- **별도의 백엔드 API 서버**로 분리되어있다면
  - `api.example.com/v1/xxx`
  - (분산 백엔드 API 환경이라면) `order-api.example.com/v1/xxx`
- **단일 프로젝트에서 웹 페이지와 API**를 모두 다루고 있다면
  - `www.example.com/api/v1/xxx`

**3차 도메인 혹은 API Path 에서 api 요청**임을 표기해야한다.  

```bash
// good
api.example.com/v1/orders
www.example.com/api/v1/orders
order-api.example.com/v1/orders
order.example.com/api/v1/orders

// bad
www.example.com/v1/orders
order.example.com/v1/orders
```

물론 인프라 계층에서 분기 처리를 한다면 단일 프로젝트에서도 서로 다른 도메인을 가지고 `api.example.com/v1` 과 같은 형태를 서비스할 수 있으나, 이럴 경우 API 컨벤션을 인프라팀과 함께 관리해야 한다.  
  
**API는 애플리케이션 개발자들이 관리해야 한다**.  
팀의 상황에 따라 인프라팀과 개발팀이 강결합 되어있거나,  
개발팀이 인프라를 직접 관리하고 있다면 이와 같이 해도 무방하다.  
  
다만, 그게 아니라 개발팀과 인프라팀이 완전히 별도로 움직이고 있다면 인프라 계층에서 분기 처리하는 것을 최소화 하는 것이 좋다.  
그런 조직 구조내에서 단일 프로젝트 환경이라면 `/api/v1/xxx` 로 처리한다.

## URI에 버전을 표기한다

API에 버전을 명시할 수 있는 방법은 크게 3가지 정도가 있지만 **URI 경로에 버전을 포함**시키는 것을 추천한다.  

```bash
// good
api.example.com/v1/orders/{orderId}
www.example.com/api/v1/orders/{orderId}
```

이는 다음과 같은 장/단점이 있다.

- 장점
  - 명확성: 버전이 URI에 명시되어 있어 직관적으로 이해하기 쉽다.
  - **캐싱 용이성**: URI 자체가 독립적으로 관리 되니 캐싱 전략을 쉽게 적용할 수 있다.
- 단점
  - URI 길이 증가: 버전 정보를 포함하면서 URI이 길어질 수 있다.
  - URI 변경: 버전이 올라갈 때마다 URI이 변경되므로, 클라이언트 측에서 수정이 필요하다.

URI에 버전을 표기하는 것 외에도 2가지 방법이 더 있다.

#### 파라미터에 버전 표기

```bash
GET /orders?version=1
GET /orders?version=2
```

#### Header에 버전 표기

```bash
GET /orders
Headers: 
    Accept: application/vnd.myapi.v1+json
GET /orders
Headers: 
    Accept: application/vnd.myapi.v2+json
```

다만, 위 2가지 방식은
**URI만 보고는 버전을 직관적으로 알기 어렵고, URI 경로가 동일해 API를 캐싱하기가 어려워** 개인적으로 선호하지 않는다.  

> [API의 캐싱에 대해서는 이전 글](https://jojoldu.tistory.com/779)을 참고한다.

## /api/{version}/리소스 템플릿을 쓴다.

버저닝 표기는 `/api/{version}/리소스` 템플릿을 사용한다.

```bash
// good
api.example.com/v1/orders/{orderId}
www.example.com/api/v1/orders/{orderId}

// bad
www.example.com/v1/api/orders/{orderId}
```

URI은 상위 -> 하위 계층으로 내려가야 한다.  
좀 더 직관적으로 이야기하자면 **변화가 더 적은 곳이 앞에 선언 되어야 한다**.  
  
이후 URI별 접근 권한 관리 등 URI을 그룹별로 관리할 여지가 많은데, 이럴때도 위와 같이 설계하면 그룹별 규칙을 관리하기가 편하다.

## Path에 리소스 ID는 가능한 1개만 사용한다

찾고자 하는 리소스의 ID를 URI에 포함시킬때는 가능한 1개만 Path Variable 에 포함시킨다.  
즉, `/리소스/{리소스 ID}/하위 리소스` 템플릿을 사용한다.

```bash
// good
/v1/orders/{orderId} - 특정 주문 조회
/v1/orders/{orderId}/courses - 특정 주문의 모든 강의 조회
/v1/courses/{courseId} - 특정 강의 조회

// bad
/v1/orders/{orderId}/courses/{courseId} - 특정 주문의 특정 강의 조회
```

이럴 경우 `bad` 사례에 대해 의견이 분분할 수 있다.  
좀 더 복잡한 시스템에서는 `/orders/1/courses/100/videos` 처럼 클라이언트가 여러 관계 수준을 탐색할 수 있는 URI를 제공하고 싶을 수 있다.  
  
그러나 이정도 수준의 복잡성은 오랫 동안 유지하기 어려울 수 있으며 나중에 **리소스 사이의 관계가 변하면 유연성이 떨어진다**.  
  
예를 들어 `orders` 와 `courses` 사이에 `carts` 라는 중간 리소스가 계층으로 하나 더 추가 되면 어떻게 될까?  
이럴 경우 `/orders/{orderId}/courses/{courseId}` 로 구성된 모든 API는 구조를 변경해야 한다.  
  
그래서 **가능한 URI를 비교적 간단하게 유지**한다.  
  
특정 리소스의 특정 하위 리소스를 찾는게 필요하면 **상위 리소스의 ID 없이 하위 리소스를 중심으로 한 API를 통해 하위 리소스 ID로 직접 조회**한다.  
  
이전 조회를 `/orders/1/courses` URI로 바꿔서 고객 1의 모든 강의을 찾은 후 `/courses/100/videos` 로 바꿔서 이 강의의 영상을 찾을 수 있다.  
   
대부분의 리소스 조회는 유니크한 기준값으로 조회한다.  
하위 리소스를 조회하는데 상위 리소스의 ID까지 필요 하다면 그건 이미 **하위 리소스의 ID가 유니크 하지 않음을 의미**한다.  
  
모든 리소스는 유니크한 ID로 조회 가능해야 한다.  
유니크한 ID가 발급 되지 않았는지,  
유니크한 ID로 조회 가능한 API가 없는지 먼저 고민해본다.   

**리소스 URI를 리소스/리소스ID/하위리소스보다 더 복잡하게 구성하지 않는 것이 좋다.**

## URI는 kebab-case

URI은 **kebab-case**를 사용한다.  

```bash
// good
api.example.com/this-is/a-nice/URI-name
www.example.com/api/v1/this-is/a-nice/URI-name

// bad
www.example.com/this_is/a_bad/URI_name 
www.example.com/ThisIs/aBad/URIName 
www.example.com/DO_NOT/EVER_DO/THIS 
www.example.com/this is/going 
```

[RFC 3986](https://datatracker.ietf.org/doc/html/rfc3986)과 같은 URI를 정의하는 표준 문서에서는 URI에 대문자, 소문자, 숫자, 일부 특수 문자(`-, _, ., ~`)의 사용을 권장한다.  
  
kebab-case는 이러한 표준 규칙에 잘 부합한다.  
또한 여러 검색엔진에서 단어간 구별을 `-`로 할 경우 훨씬 더 잘 구분한다.  

> 구글의 경우 에전엔 `_` 는 제대로 구분하지 못했는데, 이제는 잘 구분한다고 발표했다.  
> 다만, 여전히 SEO를 전문적으로 하는 분들의 의견은 `-` 가 훨씬 더 효과적이라고 한다.

**kebab-case**를 사용하기 위해 다음의 추가 규칙을 따른다.

- camelCase를 사용하지 않는다.
- snake_case를 사용하지 않는다.
- 공백을 포함하지 않는다.
- 특수 문자를 포함하지 않는다.

API는 아니지만, 스택오버플로우 등에서는 웹 페이지의 URI에서도 kebab-case를 유지한다.

- https://stackoverflow.com/questions/48495260/sql-interpolated-strings

## Parameter, Body는 camelCase

Request Parameter와 Request Body에서 사용되는 변수, 객체는 **camelCase**를 사용한다.  

```bash
// good
www.example.com/reviews?userId=

// bad
www.example.com/reviews?user_id=
```

JSON 객체에서 키를 명명할 때 camelCase를 사용하는 것이 일반적이다.  
JSON은 API에서 데이터를 전송하는 데 가장 많이 사용되는 형식 중 하나이기 때문에, camelCase를 사용하면 JSON 페이로드와 일관된 명명 규칙을 유지할 수 있다.  
  
그 외에도 각종 API 표준 및 모범 사례에서 **camelCase를 권장**한다.  

- [Google의 JSON 스타일 가이드](https://google.github.io/styleguide/jsoncstyleguide.xml)
- [Microsoft의 REST API 가이드라인](https://learn.microsoft.com/ko-kr/azure/architecture/best-practices/api-design) 

## 테이블이 아닌 리소스가 중심이 되어야 한다

API의 리소스가 테이블과 1:1로 매핑될 필요가 없다.  
  
예를 들어 주문 리소스는 내부적으로 관계형 데이터베이스의 여러 테이블로 구현할 수 있지만 클라이언트에 대해서는 단일 엔터티로 표시된다.  

**단순히 데이터베이스의 내부 구조를 반영하는 API를 만들면 안된다**.  
  
HTTP API의 목적은 Entity 및 해당 Entity에서 애플리케이션이 수행할 수 있는 작업을 모델링하는 것이다.  
  
**클라이언트는 내부 구현에 노출되면 안 된다**.

HTTP API와 기본 데이터 원본 사이에 종속성이 발생하지 않도록 해야 한다.  
예를 들어 데이터가 관계형 데이터베이스에 저장되는 경우 HTTP API는 각 테이블을 리소스 컬렉션으로 표시할 필요가 없다.  

이런 디자인은 제대로 된 디자인이라고 보긴 어렵다.  
그 대신 HTTP API를 데이터베이스의 추상화라고 생각해보자.   

필요하다면 데이터베이스와 HTTP API 사이에 매핑 계층을 도입한다.  
이 방법을 사용하면 클라이언트 애플리케이션이 기본 데이터베이스 스키마의 변경 내용으로부터 격리된다.

## 리소스는 복수형으로

리소스는 **복수형**으로 표현한다.

```bash
// good
www.example.com/reviews
www.example.com/reviews/${reviewId}

// bad
www.example.com/review
www.example.com/review/${reviewId}
```

대부분의 API는 **어느 리소스 그룹에서 특정 리소스 ID를 통해 작업을 진행한다**는 의미이다.

- `GET /users`: **사용자 컬렉션을 조회**한다.
- `POST /users`: **사용자 컬렉션에 새로운 사용자를 추가**한다.

RESTful API 설계 원칙을 따르는 많은 가이드와 베스트 프랙티스 문서에서 리소스를 복수형으로 사용하는 것을 권장한다.  
이는 RESTful 디자인 패턴의 일관성과 예측 가능성을 높이게 된다.  

## URI은 제공하는 정보에 집중한다

같은 리소스를 조회하지만, 사용자 유형에 따라 다른 버전의 데이터를 제공하는 API가 필요할 경우 URI을 통해 이를 명확하게 구분하는 것이 중요하다.  

다만, 이때 **API의 URI은 어떤 권한의 사용자가 호출해야하는지를 표현해선 안된다**.  
**어떤 결과를 줄 수 있는 API인지 URI에 표현되어야 한다**.    
  
예를 들어 다음과 같은 목적을 가진 2개의 API를 만들어야 한다고 해보자.  
- 첫번째는 **일반 유저**들이 조회하고, 조회 결과가 **요약 버전**
- 두번째는 **관리자 유저**들이 조회하고, 조회 결과가 **상세한 버전**

```bash
// good
GET /users/${userId}/summary // 일반 유저 조회
GET /users//${userId}/detail // 관리자 유저 조회

// bad
GET /users/${userId}/by-user // 일반 유저 조회
GET /users/${userId}/by-admin // 관리자 유저 조회
```

`bad`의 방식은 **어떤 클라이언트인지에게 필요한지 URI 로 표현**한다.  
(`by` 키워드가 잘못되었음은 논외로 하더라도)  
  
이는 API가 재사용할 여지를 제한시킨다.  

- 새로운 권한으로 비즈니스 회원이 신설되고, 해당 권한이 기존의 API는  추가되고, 해당 권한은 기존의 API를 사용한다면 그 API의 URI이 적절한가?  
- 이 API를 통해 얻어갈 수 있는 정보가 무엇인지 한 눈에 예측 가능할까?  
- 관리자 유저가 유저 정보가 필요하면 무조건 `/by-admin` 를 사용하면 되는가?   
- 관리자 유저도 요약 버전이 필요할때 `/by-user` 를 호출해야 한다면 그 URI은 정말 적절한 URI이 맞는가?  
  
등에 고민해볼 필요가 있다.  
  
동일한 리소스에 권한에 따라 서로 다른 형태의 Response 가 필요할 경우 이와 같은 실수를 할 때가 있다.  
  
**URI은 해당 API가 제공하는 정보에 집중**한다.

## 불필요한, 당연한 이름은 포함시키지 않는다.

```bash
// good

// bad
```

## 복잡한 행위에는 동사를 URI에 포함시킨다

CRUD 외 다양한 도메인 행위를 표현하기 위해 **URI에 동사를 포함시키는 것을 용인**한다.  
  
주문을 삭제하는 것과 주문을 취소하는 것은 다르다.  
HTTP Method로 모든 도메인의 행위를 표현하려면 삭제와 취소를 함께 표현하기가 모호하다.  
그래서 단순한 행위외 복잡한 행위에 대해서는 동사를 사용한다.  

단, 규칙없는 URI을 피하기 위해 `/리소스/{리소스ID}/행위` 의 템플릿을 따른다.

```bash
// good
POST /orders/{orderId}/cancel

// bad
POST /orders-cancel/{orderId}
```

## 일관성 유지하기

일관된 용어를 사용한다.  
예를 들어 '내역'을 `history` 로 하기로 했다면 내역에 대해서는 `history` 를 유지해야 한다.  
어디는 `history`, 어디는 `record` 등으로 진행해서는 일관성이 떨어지고, 처음 API를 보는 사람마다 서로 다르게 해석할 수 있게 된다.

```bash
// good
POST /courses/:courseId/history
POST /mentorings/:mentoringId/history

// bad
POST /courses/:courseId/history
POST /mentorings/:mentoringId/record
```

## 마무리

이외에도 HTTP API에 관한 규칙들이 여럿있다.  
이어서 2부에서 추가적인 규칙들을 소개한다.



