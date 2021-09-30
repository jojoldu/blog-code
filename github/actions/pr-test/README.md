# Github Pull Request시 Jest & Docker Test Code 수행하기

테스트 코드를 작성과 함께 항상 해주어야하는 기반 작업중 하나가 Pull Request와 Develop & Master 브랜치 Push시에 전체 테스트 코드가 수행되도록 테스트 자동화 환경을 구축 하는 것입니다.  
  


## 1. Docker & Jest 테스트 환경 구축

![1](./images/1.png)

```yaml
name: pull request

on:
  push:
    branches:
      - master
      - feature/**
  pull_request:
    branches:
      - master
      - develop

jobs:
  docker:
    timeout-minutes: 10
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v1

      - name: Start containers
        run: docker-compose -f "docker-compose.yml" up -d --build

      - name: Install node
        uses: actions/setup-node@v1
        with:
          node-version: 16.x

      - name: Install Yarn
        run: npm install yarn

      - name: Install dependencies
        run: yarn install

      - name: Run tests
        run: yarn test

      - name: Stop containers
        if: always()
        run: docker-compose -f "docker-compose.yml" down
```

(일부러 테스트가 실패하도록 한 뒤,) Pull Request를 보내보면, 아래와 같이 테스트가 실패했음을 Github Pull Request 페이지에서 확인할 수 있습니다.

![step1-test-fail](./images/step1-test-fail.png)


## 2. 테스트 커버리지

테스트 커버리지의 레포트가 필요한 경우 단순 Jest만 가지고는 상세하게 표기가 어렵습니다.  
이런 점에서 Junit (JVM 계열의 단위 테스트 도구) 을 호환하는 `junit.xml` 형식은 레포트를 표현하기에 최적화 되어있습니다.  
  
그래서 대부분의 상세 커버리지와 깨진 테스트 표기를 지원하는 Github Action 플러그인들은 **JUnit을 기반**으로 하는데요.  

* [test-reporter](https://github.com/marketplace/actions/test-reporter)
* [Jest Github Action Reporter](https://github.com/marketplace/actions/jest-github-action-reporter)

이때 필요한 패키지가 [jest-junit](https://www.npmjs.com/package/jest-junit) 입니다.  
  
jest-junit을 통해 테스트 결과를 `junit.xml` 형식으로 추출한 뒤, 이를 레포트로 표현하는 방식입니다.  
  
### 
```bash
yarn add --dev jest-junit
```

그리고 package.json에 아래와 같이 `jest-junit` 을 사용하도록 합니다.

```json
  "scripts": {
    ...
    "test:report": "jest --ci --reporters=default --reporters=jest-junit",
    ...
  },
```


```yaml
name: pull request

on:
  push:
    branches:
      - develop
      - master
  pull_request:
    branches:
      - master
      - develop

jobs:
  docker:
    timeout-minutes: 10
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v2

      - name: Start containers
        run: docker-compose -f "docker-compose.yml" up -d --build

      - name: Install node
        uses: actions/setup-node@v2
        with:
          node-version: '16'
          cache: 'npm'

      - name: Install Yarn
        run: npm install yarn

      - name: Install dependencies
        run: yarn install

      - name: Run tests
        run: yarn test:report
      - name: Test Report
        uses: dorny/test-reporter@v1
        if: success() || failure()    # run this step even if previous step failed
        with:
          name: test-results
          path: junit.xml
          fail-on-error: 'false'
          reporter: jest-junit        # Format of test results
          token: ${{ secrets.GITHUB_TOKEN }}

      - name: Stop containers
        if: always()
        run: docker-compose -f "docker-compose.yml" down

```

* `secrets.GITHUB_TOKEN` 
  * Github Action이 내장하고 있는 비밀키입니다.
  * 별도로 Secret 페이지에서 등록할 필요가 없으니 코드 그대로 사용하시면 됩니다.

![result1](./images/result1.png)

![result2](./images/result2.png)

![result3](./images/result3.png)