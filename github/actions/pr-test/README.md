# Github Pull Request시 Test Code 수행하기 (feat. NestJS)

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


## 테스트 커버리지

```bash
yarn add --dev jest-junit
```

```bash
ACTION_TEST_COVERAGE
```

##
