# iterm2 에 한글 자소 분리 문제

처음 iterm2를 세팅하게 되면 다음과 같이 **한글의 자소가 분리**되는 현상을 볼 수 있습니다.  

![1](./images/1.png)

iterm2의 유니코드 세팅이 `none`으로 되어있기 때문인데요.  
이를 설정해보겠습니다.    
  
iterm2 의 `Preferences` (단축키: `command+,`) 으로 이동합니다.  

![2](./images/2.png)

그리고 차례로 `Profiles` -> `Text` -> `Unicode normalization form` 으로 이동합니다.

(정리하면 `Preferences` -> `Profiles` -> `Text` -> `Unicode normalization form` 순)

![3](./images/3.png)

여기서 `NFC`나 `HFS+` 중 하나를 선택하시면 됩니다.  
(둘 중 한글 자소가 제대로 합쳐지는 것이면 됩니다.)  
  
그럼 아래와 같이 한글 자소가 분리되지 않고 잘 합쳐진 형태로 보이게 됩니다.

![4](./images/4.png)


## 참고

* [KLDP](https://kldp.org/comment/634981#comment-634981)