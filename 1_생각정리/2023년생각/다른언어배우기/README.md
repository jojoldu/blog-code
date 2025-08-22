# 다른 언어로 성장하기

작년 4월까지 JVM 언어 / Spring 을 사용하면서 커리어를 쌓다가, 최근 이직을 하면서 Node.js (JS / TS) 에서 개발경험을 쌓고 있다.  
  
처음 Node.js를 만났을때는 2016년이였는데, 당시엔 "Node.js와 Express를 활용한 블로그 API 2시간 만에 만들기" 같은 류의 컨텐츠가 유행이였다.  

Express가 주력으로 선택될때의 Node.js 코드 스타일과 아키텍처, 컨벤션등은 기존의 프레임워크와는 꽤나 많이 달랐다.  
그래서 당시 스프링 프레임워크가 주력이였던 내 입장에서는 여기에서 추구하는 아키텍처나 패턴은 무엇일까? 궁금하기도 했다.  
그리고 이후에 어떤 방향성으로 발전할까도 궁금했다.  
  
시간이 흘러 최근에 Node.js를 접할때는 NestJS와 TypeORM이 대세인것처럼 보였다.  
이력서, 채용공고, 블로그, 페이스북, 트위터 등에서 이 프레임워크들에 대한 이야기가 주로 다뤄졌기 때문이다.  
TS, NestJS와 TypeORM을 보면서 기본적인 프레임워크 설계나 컨셉이 C# 과 .Net, EF 혹은 Java 와 Spring, JPA 등과 유사한 점이 많아졌다는 생각이 들었다.  

TS, NestJS, TypeORM 등으로 아키텍처나 코드스타일, 지향하는 방향등이 비슷한 부분이 많아졌지만 그럼에도 JVM 진영과 Node.js 진영간에는 서로 다른 지향점을 가지고 있는 부분들도 많았다.  
그리고 그로 인해 배우는 점이 많았다.  

## Node.js, TS에서 배우기

대표적인 다른 예는 로거 (Logger) 의 사용법이다.  
  
JVM에서 로거는 [SLF4J](https://www.slf4j.org/) 기반의 `static Logger`를 주로 사용한다.  

* [백기선님의 스프링 부트와 로깅](https://www.slideshare.net/whiteship/ss-47273947)

반면에 Node.js/TS 환경에서는 [Logger를 의존성 주입 (DI)](https://stackoverflow.com/a/9915056/264697) 으로 주로 다룬다.  

```ts
@Injectable()
export class AbcService {
  constructor(
	...
    private readonly logger: Logger,
  ) {}
  ...
}
```

이렇게 로거를 DI로 다루고나니 얻게 되는 장점이 몇가지 있었다.

* 도메인 객체에서 부수효과가 있는 로거를 자연스럽게 사용하지 않게 된다.
	* 로거 역시 원칙적으로는 외부 의존성을 가진 부수효과 함수이다.
	* 이를 무분별하게 사용하다가, DI로만 로거를 사용할 수 있도록 제한하니 자연스럽게 외부 의존성을 배제한 도메인 객체를 만들기 위해 노력하게 된다.
	* 그리고 부수효과 계층 (서비스, 컨트롤러, 레파지토리) 에 로거가 집중되게 된다.
* 로거에 대한 단위테스트가 가능해졌다.

특히 종종 로거에 대해 단위테스트가 필요할때 답답한 점이 있었는데, 이 점이 해소되었다.  
다음과 같이 `StubLogger`를 사용해서 단위 테스트를 구현해보니 **실제 해당 로직이 수행되면서 발생하는 로깅 내용이 내가 의도한대로 잘 남겨지고 있는지도 검증**할 수 있었다.

```ts
export class StubLogger extends Logger {

  message: string;
  err?: Error;

  override debug(message: string, error?: Error): void {
    this.message = message;
    this.err = error;
  }

  override error(message: string, error?: Error): void {
    this.message = message;
    this.err = error;
  }

  override warn(message: string, error?: Error): void {
    this.message = message;
    this.err = error;
  }

  override info(message: string, error?: Error): void {
    this.message = message;
    this.err = error;
  }
}
```

또 하나는 `async/await` 로 인해 자연스럽게 **해당 함수/메소드가 부수효과 (Side Effect) 함수임을 인지**할 수 있어 순수 함수와 부수 효과 함수를 더 신경써서 격리시키게 된다.  
  
반면에 주의해야할 점들도 배운게 많다.  
대표적인 테스트 프레임워크인 [Jest](https://jestjs.io/) 를 보면 정말 막강한 기능들을 많이 가지고 있는데, 오히려 그 막강한 기능 때문에 **좋은 설계와 디자인 보다는 나쁜 설계와 디자인으로 빠지기 쉽다**고 느껴졌다.  
  
대표적인 사례가 [모듈 모킹 (Module Mocking)](https://www.daleseo.com/jest-mock-modules/) 이다.  
  
예를 들어 다음과 같이 `emailService`를 `import` 해서 사용하고 있는 `userService`가 있다고 해보자.

```ts
import { send } from "./emailService";

export function signup(user) {
  send(user.email, message);
}
```

이 코드에 대한 단위 테스트를 작성하기 위해서는 결국 `emailService`를 모킹해야만 한다.  
  
근데 **외부에서 주입 받은것도 아니고 그냥 import를 시킨** 이 send 함수를 어떻게 모킹할 수 있을까?  
  
Jest에서는 이게 정말 **쉽게 가능하다**.

```ts
import { signup } from "./userService";
import { send } from "./emailService";

jest.mock("./emailService"); // 모듈을 통째로 모킹한다.

beforeEach(() => {
  send.mockClear();
});

test("회원가입을 하면 이메일이 발송된다", () => {
  const user = {
  	email: "test@email.com",
  	phone: "012-345-6789",
  };

  signup(user);

  expect(sendEmail).toBeCalledTimes(1);
});
```


Jest가 기능을 너무 잘 지원해서 **원래는 테스트하기가 어려웠어야할 의존성 디자인 안에서도** 테스트 하기가 너무 쉽다.  
  
**모듈까지 모킹이 되기 때문에** 의존성 디자인의 중요성을 알기가 너무 어렵다.  
의존성 주입보다는 항상 모듈 시스템을 후킹할 수 있는 외부 도구에 자꾸 의존하게 개발자를 유도한다고 느껴진다.  

Jest가 의도한 것은 아니지만, Jest를 처음 테스트 도구로 접한 사람이라고 하면 당연히 **모든 영역을 모킹으로 해결할 수 있기 때문에** 의존성 디자인에 대해 신경쓸 이유가 없어진다.  
  
예전 글에서도 이처럼 망가진 의존성에 대한 냄새를 숨기는 방법에 대해서는 우려 된다는 식의 이야기를 쓴적이 있다. 

* [@SpyBean @MockBean 의도적으로 사용하지 않기](https://jojoldu.tistory.com/320)

테스트 하기가 어려울 정도로 의존성과 인터페이스 설계가 망가진 상태에서도 테스트가 쉬운것은 **의존성 스멜 (Dependency Smell) 을 탈취제로 숨기는 것** 처럼 느껴진다. 

보통 좋은 디자인의 코드는 테스트 하기 쉽다.  
테스트 하기 쉬운 코드가 좋은 코드가 된다고 100% 확신할 수 없지만,  
테스트 하기 어려운 코드는 나쁜 코드일 확률이 높다.    
그래서 테스트 하기 좋은 코드인지 아닌지를 통해 코드 스멜 (Code Smell) 을 파악할 수 있다.  

아무리 이상하게 디자인을 해도 테스트 하기가 쉽다면, 이건 도구를 좀 제한할 필요가 있다.  
  
너무 기능이 막강해서 쉽게 원하는 것을 얻게 하기 보다는, 최소한의 제한을 두고 잘못된 방향으로 가지 못하도록 강제성을 두는게 좋을 수도 있겠다는 생각을 했다.
  
## JVM 생태계에서 배우기

이번엔 JVM에서 주로 쓰는 것들이다.  
  
JVM, Spring에서는 **추상화, 표준 인터페이스** 등을 정말 많이 신경을 쓴다.  
대표적인 예가 바로 JPA와 Spring Data 시리즈들이다.  
  
이들은 하위 구현체가 교체되어도 **내 코드가 영향을 받지 않도록** 보호해준다.  
  
실제 사례로 이야기해보자면, 과거에 Redis 성능 테스트를 하던 중에 Spring Data Redis Client의 기본 구현체인 [Jedis](https://github.com/redis/jedis)의 성능이 너무 좋지 못했다.  
그래서 다른 Redis Client 구현체인 [Lettuce](https://lettuce.io/)로 Client를 교체해서 성능 테스트를 진행했었다.  
  
* [Jedis 보다 Lettuce 를 쓰자](https://jojoldu.tistory.com/418)
  
이때 Redis Client를 교체하면서 변경한 코드는 **빌드 도구에서 의존성만 교체해준 것뿐**이다.  
실제 **프로젝트 코드는 단 1줄도 수정할 필요가 없었다**.  
즉, 외부 의존성 교체가 내 코드에 영향을 끼치지 않았던 것이다.  
  
만약 NestJS에서 TypeORM을 쓰다가 Prisma 등으로 교체해야한다고 가정해보자.  
이때 내 코드는 얼마나 많은 변경이 필요할까?  
  
추상화 계층이 없으면 외부 의존성이 교체될때마다 내 프로젝트 코드는 쉽게 영향을 받는다.  
반면, 추상화 계층이 잘 설계 되어 있으면 외부 라이브러리 교체시에 기존 코드를 전혀 수정할 필요가 없다.  
    
지금 우리팀은 HttpClient 역시 추상화 계층을 두고 `interface HttpClient` 와 실제 구현체인 `class GotHttpClient` ([Got](https://www.npmjs.com/package/got)) 를 만들어서 사용하고 있다.  
이후에 다른 HttpClient (ex: `Axios`) 등으로 교체된다고 해서, 기존 프로젝트 코드들이 변경될 것은 전혀 없도록 구성된 것이다.

## 마무리

한쪽 생태계만이 모든 면에서 우수할 수는 없다.  
각 생태계마다 그렇게 된 사유가 있고, 히스토리가 있다.  
그리고 다른 생태계에서 배울만한 점들은 충분히 많다.  

Node.js 개발자니깐 Node.js 개발 자료만 찾아보는 것 보다는,
JVM 개발자니깐 JVM 아키텍처와 패턴만이 최고라고 하기 보다는,  
서로가 더 잘하는 부분에 대해서는 충분히 열린 마음으로 받아들이는게 중요할 것 같다.

> 참고로 최근에 가장 재밌게 본 2권의 책 [단위 테스트](https://product.kyobobook.co.kr/detail/S000001805070), [함수형 코딩](https://product.kyobobook.co.kr/detail/S000001952246) 은 각각 `C#` 과 `JavaScript`로 되어있었다.
