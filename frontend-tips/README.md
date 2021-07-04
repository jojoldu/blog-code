# 프론트엔드에서 단위 테스트 쉽게 시작하기

기본적으로 프론트엔드에서의 테스트 코드 작성은 쉽지 않다.

그렇지만 그건 어디까지나 통합테스트일 경우이며, 단위 테스트는 백엔드와 마찬가지로 쉽다

좋은 구조의 코드를 수치화할 순 없지만,

모든 대가들이 명확하게 얘기하는 기준이 있는데,

그게 바로 테스트 코드 작성이 얼마나 쉽냐가 Production Code가 좋은 디자인이냐 아니냐의 기준이다.

즉, 내 코드가 현재 단위 테스트 코드 작성이 너무 어렵다면 그건 좋은 디자인의 코드가 아닌 것이다.

## 대전제

단위 테스트의 대전제는 테스트하기 어려운 코드에서 테스트하기 쉬운것만 분리하는 것이다.

아래 예제는 어떤 코드가 테스트하기 어려운 코드인지 / 테스트하기 쉬운 코드는 어떤 것들이 있는지 / 그 어려운 코드에서 테스트하기 쉬운 코드는 어떻게 분리하는지 등의 사례들을 이야기한다.

이를 참고하여 프론트엔드에서도 최대한의 단위 테스트를 작성한다.


### 단위 테스트 코드 작성이 필요한 경우 (코드를 분리해야 하는 경우)

* 중요도가 높은 비지니스 로직이 포함된 부분
    * HTML 렌더링 / API 핸들링 등은 포함되지 않는다.
* 과거엔 없었지만, 현재 버그가 발생(발견)된 경우
    * 해당 버그 상황을 테스트 코드로 재현 / 수정 후 통과하는지 확인
* 로직은 복잡하지만, 외부에 대한 결합이 낮은 경우
    * 정산 금액 계산 / A가 없을 경우엔 C or A가 있을 경우엔 D 등의 로직

## 테스트 스멜

### 제어할 수 없는 영역을 다룰 경우 

```javascript
// Bad
export class Cookie {
  constructor(name, value) {
    this.name = name;
    this.value = value;
  }
  ...

  setCookie() {
    document.cookie = `${this.name}=${this.value};expires=${this.expires};path=/`;
  }
}
```
이 코드에서 setCookie function은 어떻게 검증할 수 있을까?

document.cookie는 브라우저 위에서만 존재한다.

단위 테스트에서는 브라우저를 재현하기가 너무나 복잡하다 (이러면 그냥 통합테스트가 된다)

그래서 이럴 경우 다음과 같이 테스트대상과 테스트하기 어려운 코드를 분리한다. 

```javascript
// Good
export class Cookie {
  constructor(name, value) {
    this.name = name;
    this.value = value;
  }
  ...

  getCookie() {
    return `${this.name}=${this.value};expires=${this.expires};path=/`;
  }
}
```
이렇게 작성하면 기존의 setCookie는 별도의 function으로만 빼놓으면 된다.

물론 Cookie 클래스와 setCookie function은 동일한 디렉토리에 둔다.


```javascript
// setCookie.js
export const setCookie = cookie => {
  document.cookie = cookie.getCookie();
};

```

그리고 테스트 코드에서는 getCookie만 검증하면 실제 생성되어야할 cookie String에 대해 검증이 쉽게 된다.

### 매번 실행때마다 변경 되는 값이 있는 경우

```javascript
// Bad
export class Cookie {
  constructor(name, value) {
    this.name = name;
    this.value = value;
  }
  ...

  setExpiresByDay(day) {
    this.expires = new Date(Date.now() + day * 24 * 60 * 60 * 1000);
  }
  ...
}
```

위 코드에서 setExpiresByDay 는 테스트하기 어려운 코드이다.

이유는 Date.now()가 포함되어 매번 실행할때마다 서로 다른 결과가 나오기 때문이다.

이럴 경우 assert 구문 (expect) 에서는 정확한 값을 검증할 수 없고 대략적으로 검증해야만 한다. 

그래서 아래와 같이 개선이 필요하다.

```javascript
// Good
export class Cookie {
  constructor(name, value) {
    this.name = name;
    this.value = value;
  }
  ...
  
  // 기존 function은 신규 function에 `now`만 주입
  setExpiresByDay(day) {
    this.setExpiresByDayWithNow(new Date(), day); 
  }
  
  setExpiresByDayWithNow(now, day) { // Date.now() 대신에 인자값으로 현재 날짜를 
    this.expires = new Date(now + day * 24 * 60 * 60 * 1000);
  }
  ...
}
```

이렇게 작성하면 기존에 function을 호출하는 코드들의 변경 없이 테스트 하기 어려운 코드와 쉬운 코드를 분리할 수 있다.

그리고 테스트는 setExpiresByDay가 아닌 setExpiresByDayWithNow만 검증하면 된다.

setExpiresByDay 는 new Date() 외에는 하는 것이 없고, 실제 로직은 setExpiresByDayWithNow 에 있기 때문

그리고 setExpiresByDayWithNow는 특정 날짜를 기준으로 +X Day라는 로직의 검증을 명확하게 할 수 있다.

```javascript
  it('지정된 일자만큼 expireDay가 추가된다.', () => {
    const cookie = new Cookie('_ia', '123');
    cookie.setExpiresByDayWithNow(new Date('2021-07-01'), 7);

    expect(cookie.expires).toEqual(new Date('2021-07-08'));
  });
```

### 프레임워크에 종속적일 경우

아래 코드는 왜 테스트하기 어려울까?

```javascript
// Bad
function getUrlQueryObject() {
  const { url } = this.props.form;
  const queryObject = url.split('?').slice(1).join('&');
  return command(queryObject);
}
(React 예제)
```

이유는 React 컴포넌트 (this.props) 에 종속적이기 때문이다. 

getUrlQueryObject만 가지고는 단위 테스트를 작성할 수 없다.

this.props 가 Test Context에 존재해야만 검증할 수 있는데, 이럴 경우 이미 테스트환경 구축이 통합 테스트환경으로 확장되고, 복잡한 상황에 놓이게 된다.

이럴 경우 아래와 같이 프레임워크에 종속적인 코드와 비지니스 로직을 분리한다.

```javascript
// Good
function getUrlQueryObject(url = {}) {
  const queryObject = url.split('?').slice(1).join('&');
  return command(queryObject);
}
```

이럴 경우 단위 테스트 코드는 getUrlQueryObject 만으로 검증이 가능하다.

 

### 로직에 따라 UI가 변경되는 경우

가장 빈번하게 나오는 경우이다.

```javascript
// Bad
class SignupFormComponent extends Component {
  handleSubmit = async () => {
    const { inputUser } = this.props.form;
    
    if(!inputUser.name || !inputUser.password || validateEmail(inputUser.email)) {
      Modal.error({ ... });
    }
    
    ....
    
    this.closetModal();
  }
}
```
 

```javascript
// Good
class SignupFormComponent extends Component {
  handleSubmit = async () => {
    const { inputUser } = this.props.form;
    
    if(isValidUser(inputUser)) {
      Modal.error({ ... });
    }
    
    ....
    
    this.closetModal();
  }
}

.....
function isValidUser(inputUser) {
  return !inputUser.name || !inputUser.password || validateEmail(inputUser.email)
}
```