# Utterances 에서 Giscus 로 마이그레이션하기

블로그의 댓글을 [Utterances](https://utteranc.es/) 에서 [Giscus](https://giscus.app/ko) 로 마이그레이션 했다.  

![result](./images/result.jpeg)

기존 댓글을 잘 사용하고 있었지만, 장점이 훨씬 많은 Giscus로 이관하게 되었다.

## 1. Giscus 장점

[Giscus](https://giscus.app/ko) 는 [Utterances](https://utteranc.es/)에 비해 많은 장점들이 있다.

### 대댓글

![adv1](./images/adv1.png)

### 댓글 수, 댓글 정렬, 게시물에 대한 반응

![adv2](./images/adv2.png)

### 다양한 테마

생각보다 많은 테마와 사용자가 직접 생성한 테마를 적용할 수 있다.

![adv3](./images/adv3.png)

블로그에 적용된 테마는 `Github Light High Contrast` 테마이다.  
(High Contrast 테마는 JetBrains IDE에서도 사용하는 애정하는 테마이다.)  

### 그 외 장점

- 지연 로딩 댓글 
- 다국어 (한국어 포함) 
- 봇 자체 호스팅 
- 기타 당등 고급 옵션과 같은 많은 새로운 기능을 추가

가장 중요한건 실제로 계속해서 **관리가 되고 있는 오픈소스라**는 것이다.  
(대조적으로 Utterances는 6개월 동안 업데이트가 없다.)

기존 Utterances는 다음과 같이 **동일 이슈가 중복 생성**되는 버그 등이 존재한다는 점을 생각해보면 옮겨야할 이유가 많았다.

![problem1](./images/problem1.png)

그래서 나처럼 기존 [Utterances](https://utteranc.es/) 에 부족함을 느꼈다면 [Giscus](https://giscus.app/ko)로 마이그레이션을 진행하자.

## 2. Giscus 설치

### Install

https://github.com/apps/giscus

### 설정

https://giscus.app/ko

## 3. 마이그레이션

https://docs.github.com/en/discussions/managing-discussions-for-your-community/moderating-discussions#converting-an-issue-to-a-discussion



> 전체 선택해서 한번에 이관하는 버튼이 있을줄 알았는데.... 없었다...  
스크립트 작성해서 옮길까 했지만, 손으로 해도 30분이면 될것같아서 크롬 단축키 쓰면서 수동 이관 했다.