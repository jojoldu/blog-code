# Toolbox로 설치한 IntelliJ에서 Touchbar를 F1 ~ F12로 고정시키기

맥북 신형에서부터 터치바가 추가되었습니다.  

![터치바](./images/터치바.png)

각 어플리케이션에 따라 특수키를 지원하는 기능인데요.  
유튜브, 키노트등을 사용할때 유용하게 쓸 수 있었습니다.  
하지만 IntelliJ나 기타 개발 환경에서는 F1 ~ F12 키를 사용하기가 불편했습니다.  
(기존의 F1 ~ F12 자리가 터치바로 활용됩니다.)  
  
F1 ~ F12를 사용할때마다 ```Fn```키를 함께 눌러서 사용했어야 했습니다.  
그러다보니 생산성이 굉장히 떨어지는게 느껴졌습니다.  
  
## 1. Library 디렉토리 즐겨찾기 등록

![finder](./images/finder.png)

![Library](./images/Library.png)



![사이드바추가1](./images/사이드바추가1.png)

![사이드바추가2](./images/사이드바추가2.png)


## 2. Fn키 활성화 등록

시스템 환경 설정 -> 키보드 -> 단축키 -> 기능키로 이동합니다.

![키보드](./images/키보드.png)

여기서 ```+```를 클릭합니다.  

![Intellij선택](./images/Intellij선택.png)

Library -> Application Support -> Jetbrains -> Toolbox -> apps -> IDEA-C (Ultimate라면 IDEA-U 선택) -> ch-0 -> 버전 -> 앱 선택

![등록후](./images/등록후.png)

앱의 바로가기를 등록하시면 안됩니다.  
실제 어플리케이션을 등록하셔야 합니다.

**세팅전**

![세팅전](./images/세팅전.png)

F1 ~ F12가 없는 터치바에서 
  
**세팅후**

![세팅후](./images/세팅후.png)

F1 ~ F12가 활성화된 상태로 변경됩니다.  
  
(폰 화질이 안좋아서 죄송합니다 ㅠ)  
  
IntelliJ