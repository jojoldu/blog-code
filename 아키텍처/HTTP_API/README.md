# HTTP API 디자인

모든 API는 예측 가능해야한다.  
이 URL만 봐도 무엇을 하는 것인지 상세 스펙을 보지 않아도 알 수 있어야 한다.

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

- Google의 JSON 스타일 가이드
- Microsoft의 REST API 가이드라인 

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

```bash
POST /reviews

PUT /reviews/1009
If-None-Match: *
```

- `If-None-Match: *`: URL에 기존 리소스가 없으면 제공된 URL에 새로운 리소스를 생성하라.

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



## PathVariable

해당 리소스의 고유키 까지만 path variable에 포함시킨다.
그 외 고유키가 아닌 것은 모두 Request Param/body에 포함시킨다
단순함과 일관성을 중시할 수 있다.

```bash
/v1/orders/courses/{courseId}
```