# 다른 언어 배우기

약 7년이 넘는 시간 동안 Java (Groovy) / Spring 을 학습하고 개발해왔다.  
최근 2년은 Node.js 환경에서 JS / TS 로 학습하고 개발하고 있다.  
  
Node.js 진영에서 NestJS와 TypeORM이 활발하게 사용되면서 

* TS 에서는 logger를 DI로 항상 처리한다
	* 도메인 객체에서 부수효과가 있는 로거를 자연스럽게 사용하지 않게 된다
	* 순수 객체를 만들기 위해 자연스럽게 노력하고, 부수효과 계층 (서비스, 컨트롤러, 레파지토리) 에 로거를 집중 시킨다
* async/await 가 지원되니 async/await가 있는 함수는 부수효과를 일으키는 함수임을 인지할 수 있어, 자연스럽게 부수효과 함수와 순수함수를 격리시키게 된다.

대표적인 테스트 프레임워크인 Jest가 [모듈 모킹 (Module Mocking)](https://www.daleseo.com/jest-mock-modules/) 기능을 너무 잘 지원해서 그런지... 테스트 하기가 너무 쉽다.  


의존성 주입이 불가능하고 항상 모듈 시스템을 후킹할 수 있는 외부도구에 의존하게되는것 같다.
이건 장점이라고 느낄 사람도 있지만, 예전 글에서도 언급했지만 **테스트 하기가 어려울정도로 의존성과 인터페이스 설계가 망가진 상태에서도** 테스트가 쉬운것은 의존성 스멜 (Dependency Smell) 을 탈취제로 숨기는 잘못된 방식이 될 확률이 높다. 


* [@SpyBean @MockBean 의도적으로 사용하지 않기](https://jojoldu.tistory.com/320)

보통 좋은 디자인의 코드는 테스트 하기 쉽다.  
테스트 하기 쉬운 코드가 좋은 코드가 된다고 100% 확신할 수 없지만,  
테스트 하기 어려운 코드는 나쁜 코드였다.  
그래서 테스트 하기 좋은 코드인지 아닌지를 통해 코드 스멜 (Code Smell) 을 파악할 수 있다.  
  
근데 Jest라는 **모듈까지 통째로 Mocking이 가능한 테스트 환경**으로 인해서 
**테스트하기 좋은 코드, 모듈**에 대한 


반면 
JVM, Spring 진영에서는 최대한 외부 라이브러리는 추상화 시킨다

* Spring Data XXX, Spring Cloud XXX 는 하위 구현체를 모두 추상화 시킨다
* 언제든 라이브러리가 교체 되어도 내 코드가 영향을 받지 않도록 한다.
* 외부 API 도 특히나 그렇다


당시 사용하던 Redis Client인 Jedis의 성능이 너무 좋지 못해 Lettuce로 Client를 교체해서 성능 테스트를 진행했었다.  
이때 내가 교체하면서 변경한 코드라곤 **빌드 도구에서 의존성만 교체해준것뿐**이다.  
실제 프로젝트 코드 어느것도 수정하지 않고 다른 클라이언트로 교체했다.  

* [Jedis 보다 Lettuce 를 쓰자](https://jojoldu.tistory.com/418)

Node.js 환경에서는 외부 라이브러리를 Wrapping 해서 사용하고 있다.  
Http Client인 [Got](https://www.npmjs.com/package/got) 를 그대로 쓰지 않고, 인터페이스 (`HttpClient`) 와 구현체 (`GotHttpClient`) 로 만들어서 **언제든 


아마도 이제 다시 JVM 환경으로 돌아가더라도 Logger는 DI를 해서 사용할 것 같고, Logger가 침투하는 영역 역시 부수효과 (Side Effect) 메소드 내에서만 사용할 것 같다.  
