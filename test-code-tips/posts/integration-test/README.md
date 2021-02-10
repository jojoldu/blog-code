# integration-test 환경 구축하기

## 개요

Gradle 빌드 설정에서 단위테스트로 부터 통합테스트를 명시적으로 구분하고 통합테스트 단계를 설정하기 위한 방법을 설명한다.

 

## Overview

기존의 main, test 소스 셋에 추가적인 integration-test 소스 셋을 위한 환경을 구성한다.

integration-test 디렉토리 안에는 통합테스트를 위한 테스트 코드를 작성하고, 통합테스트 만을 위한 리소스를 구성한다. 



 

* 단위 테스트
  * 테스트의 범위를 잘 한정(Isolation) 해서 그것의 행위 및 상태를 테스트하는 것이다. 
  * 빠른 테스트 - 개발자에게 빠르게 피드백을 주는 것 - 이 아주 중요하다. 테스트를 돌리는데 몇 분씩 걸리면 테스트를 안돌리게 되고 결국 쉽게 깨지게 된다.
* 통합 테스트 
  * 외부와 (External Resources)와 연동되는 테스트를 일컫는다. 
  * 단위테스트 보다 시간이 오래걸리고 외부 환경에 보다 민감하다.


### build.gradle

```groovy
// 통합테스트 Task 수행시 사용할 소스 및 리소스 경로 설정
sourceSets {
    integrationTest {
        java.srcDir file('src/integration-test/java')
        resources.srcDir file('src/integration-test/resources')
        compileClasspath = sourceSets.main.output + configurations.testRuntime
        runtimeClasspath = output + compileClasspath
    }
}
  
// 통합테스트를 위한 Task 정의
task integrationTest(type: Test) {
    testClassesDir = sourceSets.integrationTest.output.classesDir
    classpath = sourceSets.integrationTest.runtimeClasspath
}
 
 
// $ gradle check 실행 시 통합테스트가 수행도되록 설정
check.dependsOn integrationTest
```  
 

실행
통합테스트를 실행하려면 `gradle check` 을 실행한다.

(대부분의 코드 수정 작업 시에는 `gradle test` 로 단위 테스트를 중심으로 테스트를 실행한다.)

