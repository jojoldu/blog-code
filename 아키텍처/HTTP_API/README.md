# 1. Web API 디자인 - URL편

[마틴 파울러의 블로그](https://martinfowler.com/articles/richardsonMaturityModel.html)를 가보면 Leonard Richardson이 제안한 [Web API 성숙도 모델 (Richardson Maturity Model - RMM)](https://martinfowler.com/articles/richardsonMaturityModel.html)을 소개한다.  
여기서는 다음과 같이 4단계를 설명한다.
  
- 레벨 0: 하나의 URI를 정의하고 모든 작업은 이 URI에 대한 POST 요청  
- 레벨 1: 개별 리소스에 대해 별도의 URI를 만든다.  
- 레벨 2: HTTP 메서드를 사용하여 리소스에 대한 작업을 정의
- 레벨 3: 하이퍼미디어(HATEOAS 등)를 사용

Roy Fielding 의 정의에 따르면 레벨 3이 진정한 RESTful API에 해당한다.  
[대부분의 RESTful API는 진짜 RESTful API는 아니며 보통 레벨2 부근](https://www.youtube.com/watch?v=RP_f5dMoHFc)에 해당한다.      

아래는 마틴 파울러 블로그에 나와있는 [성숙도 레벨이 무엇을 의미하는지에 대햔 섹션](https://martinfowler.com/articles/richardsonMaturityModel.html#TheMeaningOfTheLevels)이다.  
본문을 읽기전 먼저 읽어보면 좋다.  

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

여기서는 레벨2를 기준으로 팀에서 사용하는 Web API 컨벤션을 소개한다.  
  
## 기본

좋은 Web API는 아래 규칙들을 전재로 한다.

### 모든 API는 예측 가능해야한다.  

이 URL만 봐도 무엇을 하는 것인지 상세 스펙을 보지 않아도 알 수 있어야 한다.  

### 리소스는 테이블과 1:1 매칭 대상이 아니다.

API는 리소스를 중심으로 디자인되며, 여기서 이야기 하는 **리소스는 클라이언트에서 접근할 수 있는 모든 종류의 개체, 데이터 또는 서비스 등 모든 것이 포함**된다.  
  
이를 잊으면 안된다.  
리소스는 추상화된 계층이지, **테이블과 1:1 매칭을 해야하는 것으로 오해해선 안된다**.  

### Stateless API 를 구성한다.

(REST API를 가장한) **HTTP API는 Stateless (상태 비저장) 요청 모델**을 사용해야 한다.  

HTTP 요청은 독립적이어야 하며 어떤 순서로든 발생할 수 있으므로 **요청 간에 일시적인 상태 정보를 유지하는 것은 불가능**하다.  

이러한 제약 조건이 있기 때문에 웹 서비스의 확장성이 우수하다.  
클라이언트가 어느 특정 서버로 연결 해야할지 별도로 관리할 필요가 없기 때문이다.  
**모든 서버는 모든 클라이언트의 모든 요청을 처리할 수 있다**.  

이 전제들을 기반으로 좀 더 상세한 규칙들을 소개한다.

## URL에 API 을 표기한다

API 임을 URL에서 표현되어야 한다.  
크게 2가지로 나눠진다.  

- **별도의 백엔드 API 서버**로 분리되어있다면
  - `api.example.com/v1/xxx`
  - (분산 백엔드 API 환경이라면) `order-api.example.com/v1/xxx`
- **단일 프로젝트에서 웹 페이지와 API**를 모두 다루고 있다면
  - `www.example.com/api/v1/xxx`

**3차 도메인 혹은 API Path 에서 api 요청**임을 표기해야한다.  

물론 인프라 계층에서 분기 처리를 한다면 단일 서비스에서도 서로 다른 도메인을 가지고 `api.example.com/v1` 과 같은 형태를 서비스할 수 있으나, 이럴 경우 API 컨벤션을 인프라팀과 함께 관리해야 한다.  
  
API의 관리 주체는 가능하면 애플리케이션 개발자들이 관리하는 것이 좋다.  
이를 위해 인프라 계층에서 분기 처리하는 것을 최소화 하는 것이 좋다.  
이후에 백엔드 API 서버를 별도로 분리하는 것이 더 쉽고 권장되니 단일 서버에서는 `/api/v1/xxx` 로 처리한다.

## 리소스 중심 (테이블 중심 X)

API의 리소스가 테이블과 1:1로 매핑될 필요가 없다.  
  
예를 들어 주문 리소스는 내부적으로 관계형 데이터베이스의 여러 테이블로 구현할 수 있지만 클라이언트에 대해서는 단일 엔터티로 표시된다.  

**단순히 데이터베이스의 내부 구조를 반영하는 API를 만들면 안된다**.  
  
Web API의 목적은 Entity 및 해당 Entity에서 애플리케이션이 수행할 수 있는 작업을 모델링하는 것이다.  
  
**클라이언트는 내부 구현에 노출되면 안 된다**.

Web API와 기본 데이터 원본 사이에 종속성이 발생하지 않도록 해야 한다.  
예를 들어 데이터가 관계형 데이터베이스에 저장되는 경우 Web API는 각 테이블을 리소스 컬렉션으로 표시할 필요가 없다.  

이런 디자인은 제대로 된 디자인이라고 보긴 어렵다.  
그 대신 Web API를 데이터베이스의 추상화라고 생각해보자.   

필요하다면 데이터베이스와 Web API 사이에 매핑 계층을 도입한다.  
이 방법을 사용하면 클라이언트 애플리케이션이 기본 데이터베이스 스키마의 변경 내용으로부터 격리된다.

## Path에 리소스 ID는 1개만 사용한다

찾고자 하는 리소스의 ID는 1개만 Path Variable 에 포함한다.  

```bash
// good
/v1/orders/{orderId} - 특정 주문 조회
/v1/orders/{orderId}/courses - 특정 주문의 모든 강의 조회
/v1/courses/{courseId} - 특정 강의 조회

// bad
/v1/orders/{orderId}/courses/{courseId} - 특정 주문의 특정 강의 조회
```

bad에서처럼 특정 리소스의 특정 하위 리소스를 찾는게 필요하면 **상위 리소스의 ID 없이 하위 리소스의 ID로 직접 조회**한다.  
  
대부분의 리소스 조회는 유니크한 기준값으로 조회한다.  
상위 리소스의 ID가 조회에 필요하다면 **그건 찾고자 하는 리소스의 ID가 유니크하지 않음을 의미**한다.  



좀 더 복잡한 시스템에서는 `/customers/1/orders/99/products` 처럼 클라이언트가 여러 관계 수준을 탐색할 수 있는 URI를 제공하고 싶을 수 있다.  
그러나 이 수준의 복잡성은 유지하기 어려울 수 있으며 나중에 리소스 사이의 관계가 변하면 유연성이 떨어진다.  
그 대신 URI를 비교적 간단하게 유지한다.  
애플리케이션이 리소스 참조를 지정한 후에는 이 참조를 사용하여 해당 리소스와 관련된 항목을 찾을 수 있어야 한다.  
이전 쿼리를 `/customers/1/orders` URI로 바꿔서 고객 1의 모든 주문을 찾은 후 `/orders/99/products` 로 바꿔서 이 주문의 제품을 찾을 수 있다.

**리소스 URI를 컬렉션/ID/컬렉션보다 더 복잡하게 요구하지 않는 것이 좋다.**

## 잦은 요청 vs 불필요한 데이터

또 다른 요소는 모든 웹 요청이 웹 서버의 부하를 높인다는 점이다.  
요청이 많을수록 부하가 커진다.  
따라서 다수의 작은 리소스를 표시하는 "번잡한" Web API를 피하도록 노력해야 한다.  
이러한 API는 클라이언트 애플리케이션이 필요한 모든 데이터를 찾기 위해 여러 요청을 보내야 할 수 있다.  

그 대신, 데이터를 비정규화하고 단일 요청을 통해 관련 정보를 검색할 수 있는 더 큰 리소스로 결합하는 것이 좋다.  

단, 이 접근 방식과 클라이언트에 필요 없는 데이터를 가져오는 오버헤드의 균형을 조정해야 한다.  

큰 개체를 검색하면 요청의 대기 시간이 증가하고 추가 대역폭 비용이 발생할 수 있다. 


## GET, PUT, DELETE, HEAD 및 PATCH 작업은 멱등해야 한다.

- 위 HTTP 메소드들은 모두 부수 효과가 없어야 한다. 
- 동일한 리소스에 대해 동일한 요청이 반복되면 그 결과는 동일한 상태여야한다. 
- 응답 메시지의 HTTP 상태 코드는 다를 수 있지만 여러 DELETE 요청을 동일한 URI로 보내는 것은 동일한 효과가 있어야 한다. 
  - 예를 들어 첫 번째 DELETE 요청은 상태 코드 204(콘텐츠 없음)를 반환하고, 그 이후 DELETE 요청은 상태 코드 404(찾을 수 없음)를 반환할 수 있다.
  - 단, 서버 혹은 데이터베이스 등에서는 첫번째나 그 이후의 요청에서 변경되는 부분이 없어야 한다.

## 새 리소스를 생성하는 POST 에는 연관 관계가 있는 리소스에만 영향을 줘야한다

- POST 요청이 새 리소스를 만들기 위한 것인 경우 요청의 효과는 새 리소스로 제한되어야 한다. 
  - 관련 링크의 종류가 있는 경우 직접 관련된 리소스일 수 있음 
- 예를 들어 전자 상거래 시스템에서 고객에게 새 주문을 만드는 POST 요청은 재고 수준을 수정하고 청구 정보를 생성할 수도 있지만 주문과 직접 관련되지 않은 정보를 수정하거나 시스템 전체 상태에 다른 부작용이 있으면 안된다.

## 번잡한 POST, PUT 및 DELETE 작업을 구현하지 않는다

- 리소스 컬렉션에 대한 POST, PUT 및 DELETE 요청을 지원한다. 
- POST 요청은 다수의 새로운 리소스에 대한 세부 정보를 포함할 수 있으며 그 모두를 동일한 컬렉션에 추가할 수 있고, PUT 요청은 컬렉션에 포함된 전체 리소스를 바꿀 수 있고, DELETE 요청은 컬렉션 전체를 제거할 수 있다.



## Version


## URL 네이밍 컨벤션

URL 의 컨벤션은 **kebab-case**를 사용한다.
RFC 3986과 같은 URI를 정의하는 표준 문서에서는 URL에 대문자, 소문자, 숫자, 일부 특수 문자(`-, _, ., ~`)의 사용을 권장한다.  
kebab-case는 이러한 표준 규칙에 잘 부합한다.  
또한 여러 검색엔진에서 단어간 구별을 `-`로 할 경우 훨씬 더 잘 구분한다.  

> 구글의 경우 에전엔 `_` 는 제대로 구분하지 못했는데, 이제는 잘 구분한다고 발표했다.  
> 다만, 여전히 SEO를 전문적으로 하는 분들의 의견은 `-` 가 훨씬 더 효과적이라고 한다.

**kebab-case**를 사용하기 위해 다음의 추가 규칙을 따른다.

- camelCase를 사용하지 않는다.
- snake_case를 사용하지 않는다.
- 공백을 포함하지 않는다.
- 특수 문자를 포함하지 않는다.

```bash
ex) https://stackoverflow.com/questions/48495260/sql-interpolated-strings
```

```bash
// good
www.example.com/this-is/a-nice/url-name

// bad
www.example.com/this_is/a_bad/url_name 
www.example.com/ThisIs/aBad/URLName 
www.example.com/DO_NOT/EVER_DO/THIS 
www.example.com/this is/going 
```


## 리소스

리소스는 **복수형**으로 표현한다.

```bash
// good
www.example.com/reviews

// bad
www.example.com/review
```

## Parameter, Body

RequestParameter와 Request Body에서 사용되는 변수, 객체는 **camelCase**를 사용한다.  

JSON 객체에서 키를 명명할 때 camelCase를 사용하는 것이 일반적이다.  
JSON은 API에서 데이터를 전송하는 데 가장 많이 사용되는 형식 중 하나이기 때문에, camelCase를 사용하면 JSON 페이로드와 일관된 명명 규칙을 유지할 수 있다.  
  
그 외에도 각종 API 표준 및 모범 사례에서 **camelCase를 권장**한다.  

- [Google의 JSON 스타일 가이드](https://google.github.io/styleguide/jsoncstyleguide.xml)
- [Microsoft의 REST API 가이드라인](https://learn.microsoft.com/ko-kr/azure/architecture/best-practices/api-design) 

```bash
// good
www.example.com/reviews?userId=

// bad
www.example.com/reviews?user_id=
```

## 상태 변화

- Get: 상태 변화를 주지 않는 기능
- Post: 상태 변화를 주는 기능

## 멱등성 지원하기

POST는 멱등하지 않다.  
PUT은 멱등하다.  
여러번 반복해도 동일한 결과를 보장한다.  
이를 활용하면 **리소스 생성에 대해 멱등성을 보장하는 것을 고려해볼 수 있다**.  

```bash
POST /reviews

PUT /reviews/1009
If-None-Match: *
```

- `If-None-Match: *`: URL에 기존 리소스가 없으면 제공된 URL에 새로운 리소스를 생성하라.

다만, PUT은 특성상 

## 도메인 행위 표현하기 (다양한 상태 표현)

주문을 삭제하는 것과 주문을 취소하는 것은 다르다.  
HTTP Method로 모든 도메인의 행위를 표현하려면 이와 같이 삭제와 취소를 함께 표현하기가 모호하다.  


```bash
// good
POST /orders/:orderId/cancel

// bad
POST /orders-cancel/:orderId/
```

## 일관성 유지하기

```bash
// good
POST /courses/:courseId/refund
POST /mentorings/:mentoringId/refund

// bad
POST /courses/:courseId/refund
POST /mentorings/:mentoringId/cancel
```



