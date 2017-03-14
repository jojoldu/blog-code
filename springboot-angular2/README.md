#springboot & angular2 프로젝트
안녕하세요? 이번 시간엔 springboot-angular2 예제를 진행해보려고 합니다. 모든 코드는 [Github]()에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>

### package.json
* systemjs : amd와 같은 모듈 로더, ```import``` 구문을 지원한다.
* @angular/platform-browser-dynamic
  * angular2 bootstrap을 위한 모듈
* @angular/platform-browser-dynamic을 위해 필요한 모듈
  * @angular/compiler@2.4.9 
  * @angular/platform-browser@2.4.9
  * @angular/common@2.4.9
  
### 타입스크립트 연습하기

* tsconfig.json 생성하기
  * ```target```을 es5로 할 경우 절대 경로로 의존성 등록을 해야하는 이슈가 있다. ([참고](http://stackoverflow.com/questions/33332394/angular-2-typescript-cant-find-names))

* package.json에 타입스크립트 컴파일러 단축키 지정

```
{

  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1",
    "tsc": "./node_modules/typescript/bin/tsc",
    "tsc:w": "./node_modules/typescript/bin/tsc -w"
  },

}
```

* ```tsc``` : 컴파일러 실행
* ```tsc:w``` : 컴파일러 watch로 실행

* ```src/main/resources/static/js``` 에 타입스크립트 파일 생성 및 코드 작성
* watch로 컴파일된 ```src/main/resources/static/js/``` js 파일 실행시키기

```
node xxx.js
```