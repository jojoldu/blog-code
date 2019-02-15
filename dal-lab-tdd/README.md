# 2019.02.15 달랩 TDD

## 1. Intro

* TDD란?
    * Test Driven Development
    * 테스트 주도 개발
      * 예전 이름은 Unit Test Code First
      * 실질적으론 **Test Fisrt**로 봄
    * Goal (Spec) 을 향해 한걸음씩 나아가는것
    * ex)
      * 카페를 만든다고 하면, 그 안에서 어떤 회원등급들이 있고, 어떤 메뉴들을 팔건지를 **시나리오** 상으로 쭉 작성이 필요
      * 등급과 메뉴가 추가될때마다 기존 기능이 잘 돌아가는지는 미지수
      * 이를 자동테스트로 보장
      * 즉, 시나리오 -> 자동테스트

## 2. 실습

* node 및 jest 설치
    * [아샬님의 JS 테스트 환경 구축](https://github.com/ahastudio/til/blob/master/javascript/20181212-setup-javascript-project.md)
* TDD에서 제일 중요한건 Refactoring
* 실습 코드 참고
* 테스트 코드를 짜면서 안전함을 보장 받을 수 있다.
* TDD에서 가장 중요한것은 **Refactoring**
  