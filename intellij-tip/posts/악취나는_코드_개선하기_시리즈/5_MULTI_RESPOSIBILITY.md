[IntelliJ 블로그](https://blog.jetbrains.com/idea/2017/09/code-smells-multi-responsibility-methods/)에 올라온 시리즈물을 번역 & 재정리 하였습니다. 

![0_로고](./images/0_로고.png)


# 5. Code Smells: 다중책임을 가진 메소드

마지막 포스팅에서 ```validateQuery```라는 크고 복잡한 메소드를 소개했습니다.  
이 포스팅의 초점은 메소드에서 나는 변경가능성에 대한 악취를 심플하게 하는것이지만, 실제로는 메소드의 절반을 잘라내는 아주 간단한 변경이였습니다.  
  
이 메소드를 전체적으로 공개 할 때가 왔기 때문에 이를 공개하겠습니다.  
너무나 많은 화면을 차지하는 것에 대해 사과드립니다.  
마지막 리팩토링의 최종 결과를 사용하여 여기서 약간 줄일 수 있습니다.  
그러면 20 줄의 코드가 줄어됩니다.

![5_validatequery](./images/5_validatequery.png)

