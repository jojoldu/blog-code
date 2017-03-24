# equasl & hashcode 사용하기 
안녕하세요? 이번 시간엔 **equasl & hashcode**를 어떤 곳에서 사용할 수 있는지를 확인해보려고 합니다. 모든 코드는 [Github](https://github.com/jojoldu/blog-code/tree/master/business-logic)에 있기 때문에 함께 보시면 더 이해하기 쉬우실 것 같습니다.  
(공부한 내용을 정리하는 [Github](https://github.com/jojoldu/blog-code)와 세미나+책 후기를 정리하는 [Github](https://github.com/jojoldu/review), 이 모든 내용을 담고 있는 [블로그](http://jojoldu.tistory.com/)가 있습니다. )<br/>

java를 사용하면서 equals와 hashcode를 언제 override하면 좋을지 고민하시는 분들을 몇번 뵙게 되었습니다.  


여기서 둘의 관계를 정의하는데 약간의 기준을 말씀드리자면, entity는 dto에 대해 전혀 알지 못하고, dto만이 entity에 대해 알고 있어야만 합니다.  
이는 dto의 경우 조회 혹은 출력화면의 변경에 따라 언제든지 변경될 수 있지만, entity의 경우 실질적으로 변경될 일이 거의 없습니다.  
자주 변경되는 dto를 기준으로 두면 테이블과 직접 관계를 맺고 있는 entity가 계속해서 변경되어야 하므로, **무조건 이란 생각으로** dto가 entity에 의존하도록 구성하는것이 좋습니다.  
 