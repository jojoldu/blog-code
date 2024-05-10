# Web API 디자인

2008년에 Leonard Richardson은 Web API에 대해 다음과 같은 [성숙도 모델](https://martinfowler.com/articles/richardsonMaturityModel.html)을 제안했다.  
  
- 레벨 0: 하나의 URI를 정의하고 모든 작업은 이 URI에 대한 POST 요청  
- 레벨 1: 개별 리소스에 대해 별도의 URI를 만든다.  
- 레벨 2: HTTP 메서드를 사용하여 리소스에 대한 작업을 정의
- 레벨 3: 하이퍼미디어(HATEOAS 등)를 사용

레벨 3은 Roy Fielding 의 정의에 따르면 진정한 RESTful API에 해당한다.  
대부분의 Web API는 레벨2 부근에 해당한다.  
(진짜 RESTful API를 구현해서 활용하는 경우는 거의 없다.)  
  
여기서는 레벨2를 기준으로 한다.  

## 기본 디자인

모든 API는 예측 가능해야한다.  
이 URL만 봐도 무엇을 하는 것인지 상세 스펙을 보지 않아도 알 수 있어야 한다.

## 리소스 중심 (테이블 중심 X)

리소스가 단일 실제 테이블을 기반으로 할 필요는 없다.  
  
예를 들어 주문 리소스는 내부적으로 관계형 데이터베이스의 여러 테이블로 구현할 수 있지만 클라이언트에 대해서는 단일 엔터티로 표시된다.  

**단순히 데이터베이스의 내부 구조를 반영하는 API를 만들면 안된다**.  
  
Web API의 목적은 Entity 및 해당 Entity에서 애플리케이션이 수행할 수 있는 작업을 모델링하는 것이다.  
  
**클라이언트는 내부 구현에 노출되면 안 된다**.

## 리소스 ID

해당 리소스의 고유키 까지만 path variable에 포함시킨다.
그 외 고유키가 아닌 것은 모두 Request Param/body에 포함시킨다
단순함과 일관성을 중시할 수 있다.

```bash
/v1/orders/courses/{courseId}
```

좀 더 복잡한 시스템에서는 /customers/1/orders/99/products처럼 클라이언트가 여러 관계 수준을 탐색할 수 있는 URI를 제공하고 싶을 수 있다.  
그러나 이 수준의 복잡성은 유지하기 어려울 수 있으며 나중에 리소스 사이의 관계가 변하면 유연성이 떨어진다.  
그 대신 URI를 비교적 간단하게 유지해 보세요. 애플리케이션이 리소스 참조를 지정한 후에는 이 참조를 사용하여 해당 리소스와 관련된 항목을 찾을 수 있어야 한다.  
이전 쿼리를 /customers/1/orders URI로 바꿔서 고객 1의 모든 주문을 찾은 후 /orders/99/products로 바꿔서 이 주문의 제품을 찾을 수 있다.

**리소스 URI를 컬렉션/ID/컬렉션보다 더 복잡하게 요구하지 않는 것이 좋다.**

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

## api path

API 임을 URL에서 표현되어야 한다.  
크게 2가지로 나눠진다.  

별도의 백엔드 API 서버로 분리되어있다면
- `api.example.com/v1/xxx`

단일 서비스에서 웹 페이지와 API를 모두 다루고 있다면
- `www.example.com/api/v1/xxx`

물론 인프라 계층에서 분기 처리를 한다면 단일 서비스에서도 서로 다른 도메인을 가지고 `api.example.com/v1` 과 같은 형태를 서비스할 수 있으나, API의 관리 주체가 가능한 애플리케이션 개발자들에 있으려면 인프라 계층에서 분기 처리하는 것을 최소화 하는 것이 좋다.  
  
이후에 백엔드 API 서버를 별도로 분리하는 것이 더 쉽고 권장되니 단일 서버에서 `/api/v1/xxx` 로 처리한다.

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



