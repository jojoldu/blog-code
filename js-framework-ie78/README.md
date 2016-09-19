# IE 7,8에서 JS 프레임워크 조합하기
Angular나 React + @ 조합같은 경우 정말 매력적인 JS 프레임워크임은 분명하나 IE 7,8 에서는 사용할 수 없다는 치명적인 단점? 이 있다. <br/>
국내에선 아직까지 IE 7,8의 점유율이 높은 편이라 회사 사정에 따라 IE 7,8을 지원해야하는 경우가 있다. <br/>

![악마의 IE](./images/ie-devil.png)

하지만 우리도 JS Framework를 쓰고싶다! 모던하게 개발해보고 싶다! jquery만 사용하고 싶지 않다! <br/>
이런 분들이 꽤 계실것 같아 이런 환경에서 개발할 수 있는 방법을 소개하려 한다.

## 사용할 JS 프레임워크
* [backbone](http://backbonejs.org/)
  - [backbone.js 사용시 주의사항](http://huns.me/development/1212)

* jquery
  - 1.x 버전을 사용할 예정
  - webpack으로 의존성 교체 (backbone이 jquery 최신버전을 의존하고 있어 이를 교체할 예정)
  
* lodash
  - javascript를 함수형 프로그래밍으로 구현하도록 지원하는 Util 라이브러리
  - 기존에 underscore.js의 업그레이드 버전
  - backbone이 underscore.js를 의존하고 있어 이를 lodash로 교체할 예정
  
* handlebars

* webpack
