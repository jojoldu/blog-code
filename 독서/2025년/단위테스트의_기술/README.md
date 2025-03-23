# 단위 테스트의 기술

## 단위 테스트의 기초

- 단위 테스트는 진입점을 통해 작업 단위를 호출한 후 그 종료점을 확인하는 자동화된 코드다.
- 단위 테스트는 거의 항상 단위 테스트 프레임워크를 사용하여 쉽게 작성할 수 있고 빠르게 실행할 수 있다.
- 잘 작성된 단위 테스트는 신뢰성이 높고 가독성도 좋아서 유지보수하기에 용이하다.
- 우리가 운영하는 코드가 변경되지 않는 한 동일한 결과를 보장한다.
  
### 용어

- SUT
  - 테스트 중인 주체, 시스템, 테스트의 모음 (Suite)을 의미하며, 일부 사람들은 테스트 중인 컴포넌트, 클래스, 코드를 의미하는 CUT (Component, Class, Code Under Test)라는 용어를 사용하기도 한다.
  - 무언가를 테스트할 때, 테스트하고자 하는 주요 대상을 SUT라고 한다.
- 작업 단위
  - 진입점의 호출부터 하나 이상의 종료점까지, 눈에 띄는 결과가 나타날 때까지 발생하는 모든 작업을 의미한다.
  - 진입점은 어떤 것이 되어도 상관없으며 설정하기 나름이다.
  - 가령 어떤 동작을 하는 함수가 있다고 가정해보자. 
  - 해당 함수는 다음 구조로 나뉜다.
    - 함수의 바디는 작업 단위 전체나 일부를 의미한다.
    - 함수의 선언과 서명은 바디로의 진입점이다.
    - 함수의 출력이나 실행결과는 함수의 종료점이다.
- 의존성 호출 (서드 파티 호출)
  - 단위 테스트 중 온전히 제어할 수 없는 것을 의미한다.
    - 파일에 무언가를 기록
    - 네트워크와 통신
    - 다른 팀이 관리하는 코드
    - 데이터베이스에 접근하는 행위
    - 오래 걸리는 계산 작업
  - 의존성이 아닌 것
    - 어떤 행위를 쉽게 제어할 수 있는 경우
    - 메모리 내에서 실행되는 경우
    - 빠른 속도로 처리되는 경우

### 다른 종료법, 다른 기법

단위 테스트는 작업 단위를 호출하고, 그 작업 단위의 최종 결과로서 **하나의 특정 종료점을 테스트 검증 목표로 사용**한다.  

```js
const winston = require('winston');

const makeLogger = () => {
  return winston.createLogger({
    level: 'info',
    transports: new winston.transports.Console(),
  });
};

const logger = makeLogger();

let total = 0;

const totalSoFar = () => {
  return total;
};

const sum = (numbers) => {
  const [a, b] = numbers.split(',');
  logger.info('this is a very important log output', {
    firstNumWas: a,
    secondNumWas: b,
  }); // 서드 파티를 호출하는 종료점

  const result = Number.parseInt(a, 10) + Number.parseInt(b, 10);
  total += result; // 상태 값을 변경하는 종료점
  return result; // 반환 값이 있는 종료점
};
```

- **반환 값이 있는 종료점**
  - 테스트하기 가장 쉬운 타입
  - 작업 단위를 실행하여 진입점을 호출하고 실행 결과 값을 받아 그 값을 확인하면 된다.
- **상태 값을 변경하는 종료점**
  - 상태 값을 바꾸는 경우 (간접 출력)은 좀 더 많은 작업이 필요하다.
  - 어떤 것을 호출한 후 다른 것을 호출하여 무언가를 확인하거나
  - 이전에 호출한 것을 다시 호출하여 모든 것이 의도대로 흘러갔는지 확인해야한다.
- **서드 파티를 호출하는 종료점**
  - 종료점 중 가장 많은 작업이 필요한 타입이다.
  - 서드 파티는 외부에 실행 주도권이 있는 상황이라 직접적인 코드 간섭이 힘들다.
  - 이 경우 모의객체를 만들어 테스트 결과를 임의로 조작하는 방법이 있다.

### 좋은 단위 테스트

좋은 단위 테스트는 다음을 만족한다.
- 테스트 작성자 의도를 이해하기 쉬워야 한다.
- 읽고 쓰기 쉬워야 한다.
- 테스트를 자동화할 수 있어야 한다.
- 같은 조건에서 실행 결과는 항상 같아야 한다.
- 의미 있는 테스트여야 하고, 구체적인 결과를 제공하여 문제를 쉽게 파악하고 해결할 수 있어야 한다.
- 누구나 쉽게 실행할 수 있어야 한다.
- 실패할 경우 무엇이 잘못되었는지 쉽게 알 수 있어야 한다.

좋은 단위 테스트는 다음과 같은 특징이 있다.
- **빠르게** 실행 되어야 한다.
- 테스트 환경을 일관되게 유지하고, **테스트 결과가 항상 예측 가능**해야 한다.
- **다른 테스트와 완전히 독립적으로 실행**되어야 한다.
- 시스템 파일, 네트워크, 데이터베이스가 없어도 **메모리 내에서 실행**되어야 한다.
- 가능한 한 **동기적인 흐름으로 실행**되어야 한다.
  - 가능하면 **병렬 스레드를 사용하지 않아야 한다**.

### 통합 테스트

- 다른 팀이 만든 모듈, 외부 API나 서비스, 네트워크, 데이터베이스, 스레드 등 실제 의존성을 완전히 제어할 수 없는 상태에서 작업 단위를 테스트하는 것이다.
- **통합 테스트는 실제 의존성**을 사용하고, **단위 테스트는 작업 단위를 의존성에서 격리시켜 항상 일관된 결과를 받을 수 있도록** 하여 작업 단위의 모든 측면을 쉽게 조작할 수 있게 한다.

## 테스트 프레임워크

### AAA 패턴
- 준비 (Arrange) -> 실행 (Act) -> 검증 (Assert)
- 테스트를 구조적으로 작성할 수 있도록 도와주는 패턴
  - 준비 단계는 테스트하려는 시스템과 그 종속성을 원하는 상태로 설정하는 단계
  - 실행 단계는 메서드를 호출하고 필요한 데이터를 전달
  - 검증 단계는 결과를 검증
- 테스트 코드를 작성할 때 '테스트에 필요한 변수를 초기화하는 부분이 너무 복잡하지 않은가?' 라거나 '테스트 실행은 어떻게 해야 할까?' 에 대한 고민에 대한 해결책

### USE 전략

테스트 코드 이름이 너무 불친절할 경우 USE 전략을 사용한다.  
제목만 읽어도 무엇을 하려고 하는지 바로 알아차릴 수 있어야 한다.
이를 위한 3가지 요소는 다음과 같다.

- 테스트하려는 대상 (Unit: 대상이 되는 함수/클래스/컴포넌트 등)
- 입력 값이나 상황에 대한 설명 (Senario)
- 기댓값이나 결과에 대한 설명 (Return)

'**테스트 대상을 명시하고, 어떤 입력이나 상황이 주어지면 어떤 결과로 이어져야 하는지 간결하고 명확하게 적는다**'  
  
- 빌드 과정에서 테스트가 실패하면 주석이나 전체 테스트 코드를 볼 수 없게 된다.  
- 보통은 테스트 이름과 성공 여부만 터미널에 간략히 표시하므로 테스트의 이름이나 제목을 잘 짓는 것이 중요하다.  
- 테스트 이름을 명확하게 지을수록 코드에서 문제가 생겼을때 코드 전문을 자세히 보지 않아도 어디에 문제가 있는지 쉽게 예측할 수 있다.

### Describe

- 테스트할 작업 단위 (테스트하려는 대상) 를 나타낼 수 있다. (ex: `verifyPassword()`)
- 이를 통해 각 테스트 이름에서는 작업 단위를 생략할 수 있다.
- 중첩해서 사용한다면 입력값 (상황)에 대한 것도 공유해서 사용할 수 있다.

기존 코드가 아래와 같다면 

```js
// as-is
test('badly named test', () => {
  const fakeRule = (input) => ({ passed: false, reason: 'fake reason' });

  const errors = verifyPassword('any value', [fakeRule]);

  expect(errors[0]).toMatch('fake reason');
});
```

Describe를 통해 다음과 같이 개선할 수 있다.
```js
// to-be 1)
describe('verifyPassword', () => {
  describe('with a failing rule', () => {
    const fakeRule = () => ({
        passed: false,
        reason: 'fake reason',
    });
    
    it('has an error message based on the rule.reason', () => {
        const errors = verifyPassword('any value', [fakeRule]);

        expect(errors[0]).toContain('fake reason');
    });
  });
});

// to-be 2)
describe('verifyPassword', () => {
  describe('with a failing rule', () => {
    it('has an error message based on the rule.reason', () => {
        const fakeRule = () => ({
            passed: false,
            reason: 'fake reason',
        });

        const errors = verifyPassword('any value', [fakeRule]);

        expect(errors[0]).toContain('fake reason');
    });
  });
});
```

- 동일한 시나리오에서 동일한 진입점에 대해 **여러 결과를 검증할때**는 `describe()` 구문이 도움이 된다.

## 의존성 분리와 스텁

### 목, 스텁, 페이크

- 스텁 (Stub)
  - **간접 입력 의존성을 끊어준다**
  - 미리 정의된 가짜 데이터를 제공하여 **테스트 대상 코드의 입력을 시뮬레이션**한다.
  - 가짜 모듈이나 객체 및 가짜 동작이나 데이터를 코드 내부로 보내는 가짜 함수
  - 테스트 대상 코드가 외부 시스템이나 데이터에 의존하지 않고도 동작할 수 있게 해준다
  - **스텁은 검증 대상이 아니다**
  - 하나의 테스트에서 여러 스텁을 사용할 수 있다
- 목 (Mock)
  - **간접 출력 또는 종료점의 의존성을 끊어준다**
  - 테스트 대상 코드가 **외부 시스템과 상호 작용할 때, 호출 여부와 인수를 검증**한다.
  - 가짜 모듈이나 객체 및 호출 여부를 검증하는 함수
  - 단위 테스트에서 **종료점**을 나타낸다
  - **하나의 테스트에서 목은 하나만 사용하는 것**이 일반적
- 페이크 (Fake)
  - 실제 구현을 대체하는 가벼운 버전의 구성요소
  - 실제 운영 데이터베이스 대신 인메모리 데이터베이스를 사용하는 등
  - 실제 구현과 동일한 인터페이스를 제공하지만 훨씬 가볍고 빠르게 동작한다
  
  **목과 스텁의 차이를 구분하지 못하거나 정확히 짚고 넘어가지 않으면, 한 작업 단위 안에 여러 종료점이 있을 때처럼 복잡한 테스트를 만들 때 가독성과 유지보수성이 떨어지는 테스트를 만들 수 있다**.  

### 로거 함수에 대한 의존성 분리하기

```js
const verifyPassword = (input, rules) => {
const failed = rules
  .map((rule) => rule(input))
  .filter((result) => result === false);

  console.log(failed);
  if (failed.length === 0) {
    // 이 줄은 전통적인 주입 기법으로는 테스트할 수 없다.
    log.info('PASSED');
    return true;
  }
  // 이 줄은 전통적인 주입 기법으로는 테스트할 수 없다.
  log.info('FAIL');
  return false;
};

``` 

이 코드는 종료점이 2개이다.

- `return boolean;`
- `log.info`

**가짜 객체를 주입하는 방법**

- 표준: 매개변수 추가
- 함수형: 커링, 고차함수 변환
- 모듈형: 모듈 의존성 추상화
- 객체지향형: 인터페이스 주입

```js
describe('calculate1 - with jest', () => {
  beforeEach(() => {
    jest.useFakeTimers();
  });

  beforeEach(() => {
    jest.clearAllTimers();
  });

  test('fake timeout with callback', () => {
    Samples.calculate1(1, 2, (result) => {
      expect(result).toBe(3);
    });
    jest.advanceTimersToNextTimer();
  });
});
```

```js
describe('monkey patching', () => {
  let originalTimeOut;
  beforeEach(() => (originalTimeOut = setTimeout));
  afterEach(() => (setTimeout = originalTimeOut));

  test('calculate1', () => {
    setTimeout = (callback, ms) => callback();
    Samples.calculate1(1, 2, (result) => {
      expect(result).toBe(3);
    });
  });
});
```

```js
describe('calculate with intervals', () => {
  beforeEach(() => jest.clearAllTimers());
  beforeEach(() => jest.useFakeTimers());

  test('calculate, incr input/output, calculates correctly', () => {
    let xInput = 1;
    let yInput = 2;
    const inputFn = () => ({ x: xInput++, y: yInput++ }); // 콜백 수를 검증하기 위해 변수를 증가

    const results = [];
    Samples.calculate2(inputFn, (result) => results.push(result));

    jest.advanceTimersToNextTimer(); // setInterval을 한 번 호출
    jest.advanceTimersToNextTimer(); // setInterval을 두 번 호출

    expect(results[0]).toBe(3);
    expect(results[1]).toBe(5);
  });
});
```

### DOM

```js
const fs = require('fs');
const path = require('path');
require('./index-helper.js');

const loadHtml = (fileRelativePath) => {
  const filePath = path.join(__dirname, 'index.html');
  const innerHTML = fs.readFileSync(filePath);
  document.documentElement.innerHTML = innerHTML;
};

const loadHtmlAndGetUIElements = () => {
  loadHtml('index.html');
  const button = document.getElementById('myButton');
  const resultDiv = document.getElementById('myResult');
  return { window, button, resultDiv };
};

describe('index helper', () => {
  test('vanilla button click triggers change in result div', () => {
    const { window, button, resultDiv } = loadHtmlAndGetUIElements();
    window.dispatchEvent(new Event('load'));

    button.click();

    expect(resultDiv.innerText).toBe('Clicked!');
  });
});
```

```js
const { fireEvent, findByText, getByText } = require('@testing-library/dom'); // 사용할 라이브러리 API를 가져오기

const loadHtml = (fileRelativePath) => {
  // 라이브러리 API는 대부분 문서 요소를 기반으로 작업을 처리한다
  const filePath = path.join(__dirname, 'index.html');
  const innerHTML = fs.readFileSync(filePath);
  document.documentElement.innerHTML = innerHTML;
  return document.documentElement;
};

const loadHtmlAndGetUIElements = () => {
  const docElem = loadHtml('index.html');
  const button = getByText(docElem, 'Click Me!', { exact: false });
  return { window, docElem, button };
};

describe('index helper', () => {
  test('dom test lib button click triggers change in page', () => {
    const { window, docElem, button } = loadHtmlAndGetUIElements();
    fireEvent.load(window); // 라이브러리의 fireEvent API를 사용하여 이벤트 디스패치를 간소화함

    fireEvent.click(button);

    // true가 될 때까지 기다리거나 1초 내에 타임아웃
    expect(findByText(docElem, 'Clicked', { exact: false })).toBeTruthy();
  });
});
```

```js
describe('monkey patching', () => {
  let originalTimeOut;
  beforeEach(() => (originalTimeOut = setTimeout));
  afterEach(() => (setTimeout = originalTimeOut));

  test('calculate1', () => {
    setTimeout = (callback, ms) => callback();
    Samples.calculate1(1, 2, (result) => {
      expect(result).toBe(3);
    });
  });
});
```

```js
describe('calculate1 - with jest', () => {
  beforeEach(() => {
    jest.useFakeTimers();
  });

  beforeEach(() => {
    jest.clearAllTimers();
  });

  test('fake timeout with callback', () => {
    Samples.calculate1(1, 2, (result) => {
      expect(result).toBe(3);
    });
    jest.advanceTimersToNextTimer();
  });
});
```

```js
describe('calculate with intervals', () => {
  beforeEach(() => jest.clearAllTimers());
  beforeEach(() => jest.useFakeTimers());

  test('calculate, incr input/output, calculates correctly', () => {
    let xInput = 1;
    let yInput = 2;
    const inputFn = () => ({ x: xInput++, y: yInput++ }); // 콜백 수를 검증하기 위해 변수를 증가

    const results = [];
    Samples.calculate2(inputFn, (result) => results.push(result));

    jest.advanceTimersToNextTimer(); // setInterval을 한 번 호출
    jest.advanceTimersToNextTimer(); // setInterval을 두 번 호출

    expect(results[0]).toBe(3);
    expect(results[1]).toBe(5);
  });
});
```