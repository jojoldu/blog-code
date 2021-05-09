# M1 맥북 개발 환경 세팅

애플의 M1 맥북이 나오면서 많은 개발자분들이 굉장한 성능 차이를 체감하게 되시는데요.  
저 역시도 최근에 맥미니를 구매해서 사용중인데, 비슷한 가격대의 맥북 프로에 비해서 훨씬 더 쾌적하게 개발을 하고 있습니다.  

![intro](./images/intro.png)

다만 기존 맥북의 설치앱들을 그대로 마이그레이션 하다보니 **Intel 버전의 앱들도 그대로 설치**되어 M1 성능을 체감못하는 경우도 있습니다.  
그래서 M1에 맞게 기존  


> 저같은 경우 **회사 업무에서는 Intel 맥북** (2019 맥북 프로) 를 사용하지만, 집에서는 M1 맥미니를 사용중입니다.  
> 아직까지 VPN, 보안 프로그램, 패키지등 개발에 필요한 여러 환경에서 100% M1 맥북을 지원한다는 보장이 없기 때문인데요.  
> 그래서 본인의 유일한 개발장비를 M1로 교체하기보다는, 회사 업무에서는 기존의 Intel 버전을 사용하시고 개인 개발을 할때만 M1을 사용하면서 천천히 다른 소프트웨어들이 다 M1을 지원하는걸 기다리는걸 추천드립니다.  

## 1. Silicon 버전 체크

현재 실행중인 프로그램이 M1에 적합한 앱인지 확인을 해봐야하는데요.  
가장 쉬운 방법은 설치된 응용 프로그램 전체를 한번에 보는 것입니다.

![app1](./images/app1.png)

![app2](./images/app2.png)

![app3](./images/app3.png)

* Apple Silicon: M1에 최적화 / Intel에서는 작동 X
* Universal: M1 / Intel 모두 작동
* Intel: M1 최적화가 안되어있어 로제타2를 통해 실행 (자동 실행)

또 다른 방법으로는 활성 상태 보기에서 직접 원하는 앱들만 바로 볼 수도 있습니다.

![process](./images/process.png)


![silicon](./images/silicon.png)

![universal](./images/universal.png)

![intel](./images/intel.png)

## 2. JetBrains IDE

JetBrains의 모든 IDE들이 M1 맥북을 지원하게 되었습니다.  
Intel 버전을 사용할 경우 M1에서는 기존 맥북 보다도 오히려 더 성능이 떨어지는 느낌이라서 무조건 재설치가 필요한데요.  

![toolbox](./images/toolbox.png)

## 3. VS Code

기본 설치 페이지에서는 Intel만 나와서 직접 다운로드 페이지로 이동하여 **Apple Silicon**을 

[Download](https://code.visualstudio.com/Download)

## 4. Docker

```bash
docker.errors.DockerException: Error while fetching server API version: ('Connection aborted.', FileNotFoundError(2, 'No such file or directory'))
[5608] Failed to execute script docker-compose

```

[](https://docs.docker.com/docker-for-mac/apple-silicon/)

![docker](./images/docker1.png)

## 5. Chrome

[](https://www.google.com/intl/ko/chrome/)

![chrome](./images/chrome.png)

## 6. iTerm2

저처럼 기존 Intel 맥북을 그대로 마이그레이션해서 M1 환경을 구축했다면 homebrew

![iterm](./images/iterm.png)

## 7. Homebrew

Homebrew의 경우 개발자분들마다 intel 버전 그대로 가져가야할지, Silicon 버전을 설치할지 분분한데요.  
저 같은 경우에도 아직까지는 Homebrew는 Intel 버전을 쓰는게 낫겠다는 판단하에 Intel 버전으로 설치해서 사용중입니다.

![homebrew](./images/homebrew.png)

> Homebrew Silicon 버전으로 설치할 경우 몇몇 언어들의 하위버전이 설치안되는 이슈도 있다고 하니 마음편하게 Intel을 설치했습니다.

* [Intel 버전 설치](https://www.44bits.io/ko/post/setup-apple-silicon-m1-for-developers#homebrew-%EC%84%A4%EC%B9%98)
* [Silicon 버전 설치](https://awesometic.tistory.com/272)
