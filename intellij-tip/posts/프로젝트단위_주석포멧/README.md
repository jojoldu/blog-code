# 프로젝트별로 다른 주석 자동생성 설정

업무용 노트북을 개인시간에 개인 개발용으로도 사용하다보니 자주 번거롭게 느껴지는 부분이 바로 파일의 주석 생성 부분입니다.  
일반적으로 회사에서 생성되는 파일들은 회사에서 지정한 포맷으로 파일 상단에 주석을 추가하도록 IDE에 세팅하게 됩니다.  
그러다보니 개인 프로젝트를 진행시에는 자동 생성 되는 주석을 지우고 개인 프로젝트용 주석을 복사&붙여넣기 수작업을 파일 생성때마다 매번 해야하는 불편함이 있었습니다.  

[IntelliJ 이슈](https://youtrack.jetbrains.com/issue/IDEA-27486)에서 많은 분들이 프로젝트 별로 코드 템플릿이 가능하도록 기능 지원을 요청했지만 아직 추가되진 않은것 같습니다.

![왜](../images/왜.png)

대안책으로 생각한 방법이 바로, **프로젝트 이름을 기준으로 분기처리**를 하여 주석 포맷을 다르게 출력시키도록 만드는 것입니다.  
IntelliJ는 주석 템플릿을 [Apache Velocity 문법](http://velocity.apache.org/engine/1.7/user-guide.html#if-elseif-else)을 사용하고 있는데요, Velocity가 템플릿 엔진중 하나이기 때문에 **동적 생성**이 가능합니다.  
if문과 정규표현식을 조금만 아신다면 아주 쉽게 해결할 수 있습니다.  
자 그럼 이제 진행을 해보겠습니다.

### 해결책
IntelliJ에서 ```command+shift+a``` 혹은 ```ctrl+shift+a```를 눌러 action 검색창이 열린다면 아래와 같이 ```file template```을 입력합니다.  

![open-template](../images/open-template.png)

**File template**을 선택후 ```includes -> File Header```를 선택하게 되면 기본 양식의 주석이 보이실텐데, 이부분을 수정하시면 됩니다.  
회사의 프로젝트 이름이 대부분 company-xxx, corporation-xxx등으로 이루어졌다고 가정해서 코드를 작성하면 아래와 같습니다.

![edit-template](../images/edit-template.png)

혹시나 코드가 잘 안보이실 분들을 위해 직접 코드를 추가하였습니다.  

```
#set($projectName = ${PROJECT_NAME})
#if($projectName.matches("^(company|corporation)"))
/**
 * Created by dwlee on ${DATE}.
 */
#else
/**
 * Created by jojoldu@gmail.com on ${DATE}
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
#end
```

IntelliJ에서 지원하는 템플릿 변수중 하나인 ```${PROJECT_NAME}```를 이용하여 **현재 활성화된 프로젝트의 이름이 company 혹은 corporation으로 시작하는지** 정규표현식```^(company|corporation)```을 통해 확인한 코드입니다.  
(혹시나 IntelliJ 템플릿 변수들을 좀 더 확인하고 싶으신 분들은 [링크](https://www.jetbrains.com/help/idea/2016.3/creating-and-editing-file-templates.html)를 확인하시면 됩니다.)  
이외에도 회사의 프로젝트에 규칙만 존재한다면 개인 프로젝트와는 완전히 분리된 주석 템플릿을 사용할 수 있으니, 저와 비슷한 고민을 가지신 분들은 꼭 사용해보셨으면 좋겠습니다.  
감사합니다!
