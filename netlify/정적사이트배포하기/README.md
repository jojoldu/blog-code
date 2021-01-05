# Netlify 로 정적 웹 사이트 배포하기

정적 웹 사이트 (js/html/css만 존재하는 형태)로 서비스 제공이 필요할 때 가장 편하게 구축할 수 있는 방법으로 [Netlify](https://www.netlify.com/)가 있습니다.  
  
빌드/배포/호스팅까지 정적 웹 사이트를 제공하기 위한 모든 기능을 쉽고 편하게 제공하다보니 많은 분들이 사용하고 계시는데요.  
  
GithubAction이나 TravisCI와 같이 별도의 CI 서비스를 구축할 필요도 없다보니 한번쯤 익혀놓으시면 정말 요긴하게 쓰일데가 많습니다.  
  
[가격](https://www.netlify.com/pricing/) 역시 **개인이 무료로 쓰기에** 불편함이 없습니다.

![price](./images/price.png)

자 그래서 이번 시간에는 Netlify로 간단한 정적 사이트를 배포하는 방법을 진행해보겠습니다.  

## 1. 가입하기

아직 가입을 안하신 분들이라면 아래 과정을 따라 가입부터 시작해보겠습니다.  
  
[Netlify](https://www.netlify.com/) 사이트를 방문해보시면 아래와 같이 우측 상단에 Signup 버튼이 있어, 이를 선택합니다.

![signup1](./images/signup1.png)

가입 방식이 여러곳인데, 저는 Github 계정만 있어 Github을 선택합니다.

![signup2](./images/signup2.png)

필요한 추가 정보를 등록하면 가입이 완료됩니다.

![signup3](./images/signup3.png)

가입이 완료되면 아래와 같이 퀵가이드 팝업이 뜨는데요.

![signup4](./images/signup4.png)

일단 퀵 가이드가 아닌 일반적인 방식으로 배포를 진행해보겠습니다.

## 2. 배포하기

가입 후 로그인된 페이지를 보시면 아래와 같이 **New site from Git** 버튼이 있습니다.  
이를 클릭합니다.

![deploy1](./images/deploy1.png)

그럼 아래와 같이 온라인 저장소 중 어떤 곳에서 코드를 가져올지 선택하는 화면이 나오는데요.  
제 코드는 Github에 있으니 Github을 선택합니다.

![deploy2](./images/deploy2.png)

그럼 로그인된 Github 계정이 사용할 수 있는 사용자와 그룹이 나오는데, 여기서 본인의 사용자를 선택합니다.

![deploy3](./images/deploy3.png)

특별히 Netlify가 접근하면 안되는 저장소가 있는게 아니기 때문에 접근 권한을 "All repositories" 를 선택하고 Install 버튼을 클릭합니다.

![deploy4](./images/deploy4.png)

그럼 아래와 같이 해당 계정이 보유한 저장소들이 보이는데요.  
Netlify로 배포할 저장소를 검색해서 선택합니다.

![deploy5](./images/deploy5.png)

> 테스트에 사용된 저장소는 [tistory-ppm](https://github.com/jojoldu/tistory-ppm)입니다.  
> 혹시나 배포가 실패해서 디렉토리 구조를 비교해보고 싶으신 분들은 참고해보세요.

그럼 아래와 같이 **배포 환경**을 설정합니다.

![deploy6](./images/deploy6.png)

빌드 커맨드와 정적 웹 사이트로 제공될 디렉토리 위치를 선택하셔야 하는데요.  
저 같은 경우 아래와 같은 구조로 되어 있어 ```public``` 디렉토리를 등록하였습니다.

![dir](./images/dir.png)

설정이 다 끝나셨으면 **Deploy site**를 클릭합니다.  
  
그럼 아래와 같이 배포가 진행되는 것을 볼 수 있는데요.  
  
**Site deploy in progress** 링크를 클릭해보면 배포 현황을 볼 수 있습니다.

![deploy7](./images/deploy7.png)

아래와 같이 현재 빌드중인 링크를 클릭해보면

![deploy8](./images/deploy8.png)

아래와 같이 빌드와 배포 로그를 볼 수 있습니다.

![deploy9](./images/deploy9.png)

정상적으로 성공했다면 아래와 같이 **Complete** 로그를 볼 수 있습니다.

![deploy10](./images/deploy10.png)

여기까지 하셨으면 Netlify로 정적 웹 사이트 배포와 호스팅까지는 끝났습니다.  
  
실제로 아래와 같이 배포가 성공한 경우에는 유니크한 이름의 ```xxx.netlify.app``` 도메인이 붙어서 해당 사이트가 제공되는 것을 볼 수 있습니다.

![domain1](./images/domain1.png)

![domain2](./images/domain2.png)

다만, 이렇게 정체를 알 수 없는 도메인 보다는 그래도 식별가능한 주소이면 좋겠죠?  
  
별도의 DNS 구매를 하려면 돈이 드니 ```xxx.netlify.app```에서 ```xxx```만이라도 원하는 이름으로 변경해보겠습니다.

## 3. prefix 도메인 변경하기

해당 배포 페이지에서 좌측 상단의 **Site overview**를 클릭하시면 아래와 같이 **Site settings**가 있습니다.  
이를 클릭합니다.

![domain3](./images/domain3.png)

그럼 아래와 같이 현재 서비스 되는 사이트의 여러 정보들을 설정할 수 있는데요.  
여기서 **Site name**이 ```xxx.netlify.app```에서 ```xxx```를 나타냅니다.  
하단의 **Change site name**을 클릭합니다.

![domain4](./images/domain4.png)

여기서 원하는 이름으로 변경합니다.

![domain5](./images/domain5.png)

그러면 아래와 같이 지정된 이름으로 netlify 앱의 prefix 도메인이 변경된 것을 확인할 수 있습니다.

![domain6](./images/domain6.png)

## 4. 뱃지 달기

혹시나 TravisCI와 같이 README.md 에 뱃지를 달고 싶으시다면 **Site Details** 하단에 아래와 같이 뱃지 스크립트가 나옵니다.

![badge1](./images/badge1.png)

이를 복사해서 README.md 파일에 붙여넣으시면 뱃지도 정상적으로 확인할 수 있습니다.

![badge2](./images/badge2.png)