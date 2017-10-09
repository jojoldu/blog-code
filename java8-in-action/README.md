# Java8 연습

안녕하세요? 이번 시간엔 java8에 새로 추가된 기능을 다시 정리해볼 예정입니다.  
모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/java8-in-action)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>



## 1. 스트림 API

기존에는 한번에 한 항목만을 처리했지만, 스트림 API를 통해 일련의 스트림으로 만들어 처리할 수 있게 되었습니다.


## 3. 병렬성과 공유 가변 데이터

코드의 병렬성을 쉽게 얻을 수 있게 되었습니다.  
단, 코드 작성 방법에 많은 변화가 필요합니다.  
**공유된 가변 데이터를 사용하면 안됩니다**.  
변경되지 않으며, 공유되지 않는 데이터만을 메소드에서 사용해야 합니다.  

## 4. 디폴트메소드
