# 에이전틱 코딩 시대, IaC와 GitOps가 필수인 이유

에이전틱 코딩(Agentic Coding)이 대세가 되면서 개발 속도가 눈에 띄게 빨라졌다.
Cursor, Windsurf, Claude Code 같은 AI 코딩 에이전트가 코드베이스를 분석하고, 수십 개의 파일을 동시에 수정하고, 터미널 명령까지 실행하는 수준이 되었다.

속도가 빨라진 것 자체는 좋은 일이다.
다만, 속도가 빨라질수록 한 가지 문제가 커진다.

> AI 에이전트가 만든 변경이 잘못되었을 때, 안전하게 되돌릴 수 있는가?

소프트웨어 코드는 이미 Git으로 관리하고 있으니 `git revert` 하면 된다.
그런데 인프라는?

많은 회사에서 여전히 AWS 콘솔에 접속해 클릭으로 리소스를 만든다.
변경 이력은 담당자의 머릿속에만 존재하고, "어제 누가 Security Group 바꿨어?"라는 질문에 아무도 답하지 못한다.

에이전틱 코딩 시대에 이런 환경은 문제가 된다.
AI 에이전트에게 "트래픽 증가에 대응해줘"라고 했더니, 콘솔에서 Security Group을 열고, 인스턴스 타입을 변경하고, 로드밸런서를 추가했다고 하자.
다음 날 장애가 발생했을 때:

- 어떤 변경이 장애를 일으켰는지 알 수 없다
- "어제 상태"로 즉시 되돌릴 방법이 없다
- AI가 실제로 무엇을 변경했는지 확인할 수 없다

바로 여기서 IaC(Infrastructure as Code)가 필요하다.
인프라를 코드로 정의하면, 소프트웨어 개발에서 누리던 Git의 모든 이점을 인프라에서도 그대로 얻을 수 있다.

이 글에서는 **Pulumi + AWS + GitHub Actions** 조합으로 실제 동작하는 IaC 프로젝트를 처음부터 끝까지 만들어본다.
직접 따라하면서 IaC와 GitOps가 왜 필요한지 체감할 수 있도록 구성했다.

> 이 글의 전체 예제 코드는 [GitHub](https://github.com/jojoldu/iac-gitops-pulumi)에서 확인할 수 있다.

---

## 1. 왜 IaC인가

IaC를 쓰면 뭐가 좋은지, 표로만 보면 잘 와닿지 않는다.
직접 겪어봐야 느끼는 차이를 먼저 정리했다.

### 수동 관리의 문제

콘솔 클릭으로 인프라를 관리하면 초기에는 빠르다.
EC2 하나 만드는데 코드를 작성할 필요가 없으니 당연하다.

그런데 시간이 지나면 문제가 쌓인다.

- 프로덕션과 스테이징 환경이 왜 다른지 아무도 모른다
- 장애가 나면 "누가 뭘 바꿨는지" 추적이 안된다
- 새 환경을 만들려면 콘솔에서 하나씩 다시 클릭해야 한다
- 인수인계가 불가능하다 (담당자가 퇴사하면 히스토리가 사라진다)

### IaC로 바뀌는 것

인프라를 코드로 관리하면 이런 것들이 가능해진다.

```bash
# 어제 누가 Security Group을 바꿨는지 확인
git log --oneline components/webServer.ts

# 장애 발생 시 어제 상태로 되돌리기
git revert HEAD
pulumi up --yes

# 새 환경(staging)을 만들기
pulumi stack init staging
pulumi up --yes
```

모든 변경이 코드로 남고, 코드는 Git에 기록되고, Git은 되돌릴 수 있다.
이것이 IaC의 핵심 가치다.

### 멱등성

IaC에서 가장 중요한 개념이 멱등성(Idempotency)이다.

쉘 스크립트로 인프라를 만들면 이런 문제가 생긴다.

```bash
# 실행할 때마다 새 인스턴스가 생성된다
aws ec2 run-instances --image-id ami-xxx --instance-type t3.micro

# 3번 실행하면? EC2가 3대 생긴다
```

Pulumi는 다르다.

```typescript
// "webServer라는 EC2가 존재해야 한다"
const server = new aws.ec2.Instance("webServer", {
    instanceType: "t3.micro",
    ami: "ami-xxx",
});

// 몇 번 실행하든 EC2는 1대다. 없으면 생성하고, 있으면 유지한다.
```

에이전틱 코딩에서 멱등성이 특히 중요한 이유가 있다.
AI 에이전트는 같은 작업을 재시도하거나, 여러 경로에서 동일한 리소스를 참조할 수 있다.
멱등성이 보장되지 않으면 AI의 재시도가 곧 장애의 원인이 된다.

---

## 2. 왜 Pulumi인가

IaC 도구로 Terraform이 가장 유명하다.
Terraform도 훌륭한 도구이고, 이미 잘 쓰고 있다면 그것도 좋은 선택이다.

다만 새로 IaC를 도입하는 팀이 에이전틱 코딩을 적극 활용하고 싶다면, Pulumi가 더 유리한 출발점이라고 생각한다.

이유는 간단하다.
Pulumi는 TypeScript, Python 같은 범용 프로그래밍 언어를 사용한다.

Terraform의 HCL은 전용 DSL이다.
조건문이나 반복문을 쓰려면 HCL만의 문법을 배워야 한다.

```hcl
# Terraform: 조건부 생성이 필요하면?
resource "aws_instance" "web" {
  count = var.enable_web ? 1 : 0
  
  # 반복이 필요하면? dynamic block을 써야 한다
  dynamic "ingress" {
    for_each = var.ports
    content {
      from_port = ingress.value
    }
  }
}
```

Pulumi는 그냥 if문과 for문을 쓰면 된다.

```typescript
// Pulumi: 그냥 TypeScript다
if (config.requireBoolean("enableWeb")) {
    const server = new aws.ec2.Instance("web", {
        ami: "ami-xxx",
        instanceType: "t3.micro",
    });
}

for (const port of [80, 443]) {
    // ...
}
```

AI 에이전트 관점에서도 마찬가지다.
LLM의 학습 데이터에 TypeScript와 Python 코드가 HCL보다 훨씬 많다.
그만큼 AI 에이전트가 생성하는 Pulumi 코드의 정확도가 더 높을 수밖에 없다.

---

## 3. 실습: 처음부터 끝까지 따라하기

여기서부터 실습이다.
실제로 따라하면서 Pulumi + AWS + GitHub Actions 환경을 만들어본다.

> **전제 조건**
> - Node.js 20+ 설치
> - AWS CLI 설정 완료 (`aws configure`)
> - [Pulumi CLI 설치](https://www.pulumi.com/docs/install/)
> - GitHub 저장소 생성

### 3-1. 프로젝트 생성

```bash
mkdir my-infrastructure && cd my-infrastructure
pulumi new aws-typescript
```

프로젝트 이름, 설명, 스택 이름을 물으면 다음과 같이 입력한다.

```
project name: my-infrastructure
project description: 우리 서비스의 AWS 인프라
stack name: dev
aws:region: ap-northeast-2
```

생성된 프로젝트 구조는 다음과 같다.

```
my-infrastructure/
├── index.ts           # 메인 인프라 정의
├── Pulumi.yaml        # 프로젝트 설정
├── Pulumi.dev.yaml    # dev 스택 설정
├── package.json
└── tsconfig.json
```

### 3-2. S3 버킷 하나 만들어보기

먼저 가장 간단한 것부터 시작한다.
S3 버킷 하나를 만들어보자.

`index.ts`를 아래와 같이 수정한다.

```typescript
import * as pulumi from "@pulumi/pulumi";
import * as aws from "@pulumi/aws";

const config = new pulumi.Config();
const environment = pulumi.getStack(); // dev, staging, prod 등 스택 이름

// S3 버킷
const bucket = new aws.s3.BucketV2("app-data", {
    bucket: `my-app-data-${environment}`,
    tags: {
        Environment: environment,
        ManagedBy: "pulumi",
    },
});

// 버킷 버저닝 활성화
const versioning = new aws.s3.BucketVersioningV2("app-data-versioning", {
    bucket: bucket.id,
    versioningConfiguration: {
        status: "Enabled",
    },
});

// 퍼블릭 접근 차단
const publicAccessBlock = new aws.s3.BucketPublicAccessBlock("app-data-public-access", {
    bucket: bucket.id,
    blockPublicAcls: true,
    blockPublicPolicy: true,
    ignorePublicAcls: true,
    restrictPublicBuckets: true,
});

export const bucketName = bucket.bucket;
```

변경사항을 미리 확인한다.

```bash
pulumi preview
```

출력 결과:

```
Previewing update (dev):

     Type                               Name                     Plan
 +   pulumi:pulumi:Stack                my-infrastructure-dev    create
 +   ├─ aws:s3:BucketV2                 app-data                 create
 +   ├─ aws:s3:BucketVersioningV2       app-data-versioning      create
 +   └─ aws:s3:BucketPublicAccessBlock  app-data-public-access   create

Resources:
    + 4 to create
```

총 4개의 리소스(Stack 포함)가 생성될 것이라고 미리 알려준다.
실제로 적용하기 전에 무엇이 바뀌는지 확인할 수 있다는 것이 핵심이다.

문제가 없으면 적용한다.

```bash
pulumi up --yes
```

Git에 커밋한다.

```bash
git init
git add .
git commit -m "S3 버킷 추가"
```

### 3-3. EC2 웹 서버 추가하기

이번엔 현업에서 흔히 볼 수 있는 구성을 만들어본다.
VPC, Security Group, EC2를 추가한다.

`index.ts`에 아래 코드를 추가한다.

```typescript
// VPC
const vpc = new aws.ec2.Vpc("main-vpc", {
    cidrBlock: "10.0.0.0/16",
    enableDnsHostnames: true,
    enableDnsSupport: true,
    tags: {
        Name: `main-vpc-${environment}`,
        Environment: environment,
        ManagedBy: "pulumi",
    },
});

// 퍼블릭 서브넷
const publicSubnet = new aws.ec2.Subnet("public-subnet-1", {
    vpcId: vpc.id,
    cidrBlock: "10.0.1.0/24",
    availabilityZone: "ap-northeast-2a",
    mapPublicIpOnLaunch: true,
    tags: {
        Name: `public-subnet-1-${environment}`,
        Type: "public",
    },
});

// 인터넷 게이트웨이
const igw = new aws.ec2.InternetGateway("main-igw", {
    vpcId: vpc.id,
    tags: { Name: `main-igw-${environment}` },
});

// 라우트 테이블
const publicRt = new aws.ec2.RouteTable("public-rt", {
    vpcId: vpc.id,
    routes: [{
        cidrBlock: "0.0.0.0/0",
        gatewayId: igw.id,
    }],
    tags: { Name: `public-rt-${environment}` },
});

new aws.ec2.RouteTableAssociation("public-rta-1", {
    subnetId: publicSubnet.id,
    routeTableId: publicRt.id,
});

// Security Group
const webSg = new aws.ec2.SecurityGroup("web-sg", {
    vpcId: vpc.id,
    description: "Allow HTTP and HTTPS",
    ingress: [
        { protocol: "tcp", fromPort: 80, toPort: 80, cidrBlocks: ["0.0.0.0/0"], description: "HTTP" },
        { protocol: "tcp", fromPort: 443, toPort: 443, cidrBlocks: ["0.0.0.0/0"], description: "HTTPS" },
    ],
    egress: [
        { protocol: "-1", fromPort: 0, toPort: 0, cidrBlocks: ["0.0.0.0/0"], description: "Allow all outbound" },
    ],
    tags: { Name: `web-sg-${environment}`, ManagedBy: "pulumi" },
});

// EC2 인스턴스
const ami = aws.ec2.getAmiOutput({
    mostRecent: true,
    owners: ["amazon"],
    filters: [{ name: "name", values: ["al2023-ami-2023*-x86_64"] }],
});

const instanceType = config.get("instanceType") || "t3.micro";

const webServer = new aws.ec2.Instance("web-server", {
    ami: ami.id,
    instanceType: instanceType,
    subnetId: publicSubnet.id,
    vpcSecurityGroupIds: [webSg.id],
    tags: {
        Name: `web-server-${environment}`,
        Environment: environment,
        ManagedBy: "pulumi",
    },
});

export const vpcId = vpc.id;
export const webServerPublicIp = webServer.publicIp;
```

적용 전에 미리 확인한다.

```bash
pulumi preview
```

```
Previewing update (dev):

     Type                              Name              Plan
     pulumi:pulumi:Stack               my-infrastructure-dev
 +   ├─ aws:ec2:Vpc                    main-vpc          create
 +   ├─ aws:ec2:Subnet                 public-subnet-1   create
 +   ├─ aws:ec2:InternetGateway        main-igw          create
 +   ├─ aws:ec2:RouteTable             public-rt         create
 +   ├─ aws:ec2:RouteTableAssociation  public-rta-1      create
 +   ├─ aws:ec2:SecurityGroup          web-sg            create
 +   └─ aws:ec2:Instance               web-server        create

Resources:
    + 7 to create
    4 unchanged
```

총 7개 리소스가 새로 생기고, 기존 S3 관련 4개는 변경 없다.

적용하고 커밋한다.

```bash
pulumi up --yes
git add .
git commit -m "VPC, Security Group, EC2 웹 서버 추가"
```

### 3-4. 환경별 설정 분리

현업에서는 dev, staging, prod 환경의 설정이 다르다.
EC2 인스턴스 타입이 dev에서는 `t3.micro`지만, prod에서는 `t3.large`를 쓰는 식이다.

Pulumi는 스택별로 설정 파일을 분리할 수 있다.

`Pulumi.dev.yaml`에 설정을 추가한다.

```yaml
config:
  aws:region: ap-northeast-2
  my-infrastructure:instanceType: t3.micro
```

prod 스택을 만들어보자.

```bash
pulumi stack init prod
```

`Pulumi.prod.yaml`이 생성된다. 아래 내용을 넣는다.

```yaml
config:
  aws:region: ap-northeast-2
  my-infrastructure:instanceType: t3.large
```

코드는 동일하고, 환경별 설정만 다르다.
`pulumi up`을 실행하면 각 스택의 설정에 맞는 인프라가 만들어진다.

```bash
# dev 환경 적용 (t3.micro)
pulumi stack select dev
pulumi up --yes

# prod 환경 적용 (t3.large)
pulumi stack select prod
pulumi up --yes
```

같은 코드로 여러 환경을 관리할 수 있다.
콘솔에서 수동으로 하면 환경마다 하나씩 클릭해야 하고, 실수가 생겨도 알 수 없다.

커밋한다.

```bash
git add .
git commit -m "환경별 설정 분리 (dev: t3.micro, prod: t3.large)"
```

### 3-5. 현업 시나리오: SSH 포트를 열었다가 되돌리기

현업에서 흔히 겪는 상황을 재현해보자.

> 디버깅을 위해 SSH(22번 포트)를 급하게 열었는데, 그대로 두고 잊어버렸다.

IaC 없이는 이런 변경이 기록되지 않는다.
IaC에서는 이 과정이 어떻게 되는지 따라해보자.

먼저 브랜치를 만든다.

```bash
git checkout -b feature/open-ssh
```

Security Group의 `ingress`에 SSH 규칙을 추가한다.

```typescript
ingress: [
    { protocol: "tcp", fromPort: 80, toPort: 80, cidrBlocks: ["0.0.0.0/0"], description: "HTTP" },
    { protocol: "tcp", fromPort: 443, toPort: 443, cidrBlocks: ["0.0.0.0/0"], description: "HTTPS" },
    // 디버깅용 SSH 추가
    { protocol: "tcp", fromPort: 22, toPort: 22, cidrBlocks: ["0.0.0.0/0"], description: "SSH" },
],
```

미리보기로 변경사항을 확인한다.

```bash
pulumi preview
```

```
Previewing update (dev):

     Type                         Name     Plan       Info
     pulumi:pulumi:Stack          my-infrastructure-dev
 ~   └─ aws:ec2:SecurityGroup     web-sg   update     [diff: ~ingress]

Resources:
    ~ 1 to update
    10 unchanged
```

Security Group 하나만 변경된다는 것을 알 수 있다.

적용하고 커밋한다.

```bash
pulumi up --yes
git add .
git commit -m "디버깅용 SSH 포트 오픈"
git push origin feature/open-ssh
```

여기까지가 "SSH를 열었다" 이다.

디버깅이 끝났다.
이제 되돌리면 된다.
IaC + Git이 없었다면 "어떤 Security Group에서 SSH를 열었더라?" 부터 찾아야 한다.
Git에서는 간단하다.

```bash
git checkout main
git merge feature/open-ssh
git revert HEAD
pulumi up --yes
```

끝이다. Security Group이 원래 상태로 돌아간다.
어떤 변경이 있었고, 누가 되돌렸는지도 Git 히스토리에 남는다.

```bash
git log --oneline
# abc1234 Revert "디버깅용 SSH 포트 오픈"
# def5678 디버깅용 SSH 포트 오픈
# ghi9012 VPC, Security Group, EC2 웹 서버 추가
# jkl3456 S3 버킷 추가
```

---

## 4. GitOps: Git을 중심에 둔 운영

IaC로 인프라를 코드화했다면, 다음 단계는 GitOps다.

GitOps라고 하면 쿠버네티스(K8s)와 ArgoCD를 떠올리는 경우가 많다.
하지만 GitOps의 본질은 특정 도구가 아니라 원칙이다.

[OpenGitOps](https://opengitops.dev/)에서 정의한 4가지 원칙이 있다.

| 원칙 | 의미 | 이 글에서의 구현 |
|------|------|-----------------|
| 선언적 | "어떻게"가 아니라 "무엇을"을 정의 | Pulumi TypeScript 코드 |
| 버전 관리 | 모든 변경을 기록하고 불변으로 관리 | Git + PR 리뷰 |
| 자동 Pull | Git 변경을 감지해서 자동으로 적용 | GitHub Actions push 트리거 |
| 지속적 조정 | 실제 상태가 달라지면 자동으로 맞춤 | Drift Detection 스케줄 |

핵심은 **main 브랜치의 코드 = 인프라의 "있어야 할 상태"** 라는 것이다.

- main에 머지되면 → CI/CD가 자동으로 `pulumi up` 실행
- 누가 콘솔에서 수동 변경하면 → 스케줄 CI가 감지해서 알림

K8s 없이도, ArgoCD 없이도, GitHub Actions만으로 이 원칙을 충분히 실천할 수 있다.

---

## 5. GitHub Actions로 GitOps 구현하기

실습에서 만든 프로젝트에 CI/CD 파이프라인을 추가한다.
3개의 워크플로우를 만든다.

### 5-1. PR Preview

PR이 생성되면 `pulumi preview`를 실행하고, 결과를 PR 코멘트로 남긴다.
리뷰어가 "이 PR이 머지되면 인프라에 어떤 변화가 생기는지"를 코드 diff와 함께 볼 수 있다.

`.github/workflows/preview.yml`을 생성한다.

```yaml
name: Infrastructure Preview

on:
  pull_request:
    branches: [main]

jobs:
  preview:
    runs-on: ubuntu-latest
    permissions:
      pull-requests: write
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: '20'
      - run: npm ci

      - uses: aws-actions/configure-aws-credentials@v4
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ap-northeast-2

      - uses: pulumi/actions@v5
        with:
          command: preview
          stack-name: dev
          comment-on-pr: true
          comment-on-pr-number: ${{ github.event.pull_request.number }}
        env:
          PULUMI_ACCESS_TOKEN: ${{ secrets.PULUMI_ACCESS_TOKEN }}
```

이 워크플로우가 동작하면 PR에 아래와 같은 코멘트가 자동으로 달린다.

```
Pulumi Preview

  + aws:rds:Instance       user-db       create
  ~ aws:ec2:SecurityGroup  web-sg        update [diff: +ingress]
  
  Resources: 1 to create, 1 to update, 10 unchanged
```

코드 리뷰어 입장에서 이게 있고 없고의 차이가 크다.
코드 diff만으로는 "실제로 뭐가 바뀌는지" 파악하기 어려운 경우가 있는데, `pulumi preview` 결과가 함께 있으면 바로 확인할 수 있다.

### 5-2. Deploy

main 브랜치에 머지되면 자동으로 `pulumi up`을 실행한다.

`.github/workflows/deploy.yml`을 생성한다.

```yaml
name: Infrastructure Deploy

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: '20'
      - run: npm ci

      - uses: aws-actions/configure-aws-credentials@v4
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ap-northeast-2

      - uses: pulumi/actions@v5
        with:
          command: up
          stack-name: dev
        env:
          PULUMI_ACCESS_TOKEN: ${{ secrets.PULUMI_ACCESS_TOKEN }}
```

이제부터 직접 `pulumi up`을 실행할 필요가 없다.
PR을 만들고, 리뷰 받고, 머지하면 인프라가 자동으로 반영된다.

이것이 GitOps의 "자동 Pull" 원칙이다.

### 5-3. Drift Detection

GitOps의 4번째 원칙 "지속적 조정"을 구현하는 워크플로우다.

누군가 AWS 콘솔에서 직접 리소스를 수정하면, Git의 코드와 실제 인프라가 달라진다.
이것을 Drift라고 부른다.

이 워크플로우는 매일 아침 9시에 실행되어 Drift를 감지한다.

`.github/workflows/drift-detection.yml`을 생성한다.

```yaml
name: Drift Detection

on:
  schedule:
    - cron: '0 0 * * 1-5'  # UTC 0시 = KST 9시, 평일만
  workflow_dispatch:        # 수동 실행도 가능

jobs:
  detect:
    runs-on: ubuntu-latest
    permissions:
      issues: write
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: '20'
      - run: npm ci

      - uses: aws-actions/configure-aws-credentials@v4
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ap-northeast-2

      - uses: pulumi/actions@v5
        id: drift
        continue-on-error: true
        with:
          command: preview
          stack-name: dev
          expect-no-changes: true
        env:
          PULUMI_ACCESS_TOKEN: ${{ secrets.PULUMI_ACCESS_TOKEN }}

      - name: Drift 감지 시 GitHub Issue 생성
        if: steps.drift.outcome == 'failure'
        uses: actions/github-script@v7
        with:
          script: |
            await github.rest.issues.create({
              owner: context.repo.owner,
              repo: context.repo.repo,
              title: '⚠️ 인프라 Drift 감지',
              body: [
                'Git 코드와 실제 AWS 상태가 다릅니다.',
                '누군가 콘솔에서 직접 변경한 것 같습니다.',
                '',
                '확인 후 코드를 업데이트하거나 `pulumi up`으로 원래 상태로 복구해주세요.',
                '',
                `[워크플로우 실행 결과](${context.serverUrl}/${context.repo.owner}/${context.repo.repo}/actions/runs/${context.runId})`
              ].join('\n'),
              labels: ['infrastructure', 'drift']
            })
```

Drift가 감지되면 GitHub Issue가 자동으로 생성된다.

커밋한다.

```bash
mkdir -p .github/workflows
git add .github/
git commit -m "CI/CD 파이프라인 추가 (Preview, Deploy, Drift Detection)"
```

이 3개의 워크플로우를 조합하면 GitOps의 4가지 원칙을 모두 구현할 수 있다.

| GitOps 원칙 | 구현 |
|-------------|------|
| 선언적 | Pulumi TypeScript 코드 |
| 버전 관리 | Git + PR 리뷰 |
| 자동 Pull | `deploy.yml` — main push 시 자동 `pulumi up` |
| 지속적 조정 | `drift-detection.yml` — 평일 9시 Drift 체크 |

---

## 6. 현업 시나리오: AI 에이전트와 함께 인프라 변경하기

여기서부터는 에이전틱 코딩 환경에서 IaC + GitOps가 어떻게 동작하는지 구체적인 시나리오로 살펴본다.

### 시나리오 1: 트래픽 증가 대응

```
👤 "현재 웹서버가 단일 EC2인데, 트래픽이 증가하고 있어. 
    Auto Scaling Group으로 변경해줘."
```

AI 에이전트는 기존 `index.ts`를 분석하고, 아래와 같은 코드를 작성한다.

```typescript
// 기존 단일 EC2 인스턴스를 제거하고 아래로 교체

// 서브넷 하나 더 추가 (ASG는 최소 2개 AZ 필요)
const publicSubnet2 = new aws.ec2.Subnet("public-subnet-2", {
    vpcId: vpc.id,
    cidrBlock: "10.0.2.0/24",
    availabilityZone: "ap-northeast-2c",
    mapPublicIpOnLaunch: true,
    tags: { Name: `public-subnet-2-${environment}`, Type: "public" },
});

new aws.ec2.RouteTableAssociation("public-rta-2", {
    subnetId: publicSubnet2.id,
    routeTableId: publicRt.id,
});

// Launch Template
const launchTemplate = new aws.ec2.LaunchTemplate("web-lt", {
    imageId: ami.id,
    instanceType: instanceType,
    vpcSecurityGroupIds: [webSg.id],
    tags: { Name: `web-lt-${environment}`, ManagedBy: "pulumi" },
});

// ALB
const alb = new aws.lb.LoadBalancer("web-alb", {
    internal: false,
    loadBalancerType: "application",
    securityGroups: [webSg.id],
    subnets: [publicSubnet.id, publicSubnet2.id],
    tags: { Name: `web-alb-${environment}`, ManagedBy: "pulumi" },
});

// Target Group
const targetGroup = new aws.lb.TargetGroup("web-tg", {
    port: 80,
    protocol: "HTTP",
    vpcId: vpc.id,
    healthCheck: {
        path: "/health",
        healthyThreshold: 2,
        unhealthyThreshold: 3,
    },
    tags: { Name: `web-tg-${environment}` },
});

// Listener
new aws.lb.Listener("web-listener", {
    loadBalancerArn: alb.arn,
    port: 80,
    defaultActions: [{
        type: "forward",
        targetGroupArn: targetGroup.arn,
    }],
});

// Auto Scaling Group
const asg = new aws.autoscaling.Group("web-asg", {
    desiredCapacity: 2,
    maxSize: 10,
    minSize: 2,
    launchTemplate: {
        id: launchTemplate.id,
        version: "$Latest",
    },
    vpcZoneIdentifiers: [publicSubnet.id, publicSubnet2.id],
    targetGroupArns: [targetGroup.arn],
});

export const albDnsName = alb.dnsName;
```

AI 에이전트가 이 코드로 PR을 만들면, CI가 `pulumi preview`를 실행한다.

```
Pulumi Preview

  + aws:ec2:Subnet             public-subnet-2  create
  + aws:ec2:RouteTableAssociation public-rta-2  create
  + aws:ec2:LaunchTemplate     web-lt           create
  + aws:lb:LoadBalancer        web-alb          create
  + aws:lb:TargetGroup         web-tg           create
  + aws:lb:Listener            web-listener     create
  + aws:autoscaling:Group      web-asg          create
  - aws:ec2:Instance           web-server       delete

  Resources: 7 to create, 1 to delete, 10 unchanged
```

PR 리뷰에서 확인할 수 있는 것들:

- 기존 EC2(`web-server`)가 삭제되고 ASG가 생성된다
- 코드 diff를 통해 Health Check 설정이 적절한지 확인할 수 있다
- 문제가 있으면 머지하지 않으면 된다

머지 후 문제가 생기면? `git revert` → 자동 `pulumi up` → 원래 단일 EC2 상태로 복구.

### 시나리오 2: 보안 점검

```
👤 "현재 인프라 코드에서 보안 취약점 있는지 점검해줘."
```

AI 에이전트가 코드를 분석한 결과:

```
분석 결과 2가지 보안 이슈를 발견했습니다.

1. web-sg: SSH(22번 포트)가 0.0.0.0/0으로 열려 있음
   → 회사 VPN IP 대역(10.0.0.0/8)으로 제한

2. app-data 버킷: 서버 사이드 암호화가 설정되지 않음
   → SSE-KMS 암호화 추가
```

AI 에이전트가 수정한 코드의 diff:

```diff
 // Security Group
-{ protocol: "tcp", fromPort: 22, toPort: 22, cidrBlocks: ["0.0.0.0/0"], description: "SSH" },
+{ protocol: "tcp", fromPort: 22, toPort: 22, cidrBlocks: ["10.0.0.0/8"], description: "SSH - VPN only" },
```

```typescript
// S3 버킷 암호화 추가
const encryption = new aws.s3.BucketServerSideEncryptionConfigurationV2("app-data-encryption", {
    bucket: bucket.id,
    rules: [{
        applyServerSideEncryptionByDefault: {
            sseAlgorithm: "aws:kms",
        },
    }],
});
```

보안 변경은 특히 코드 리뷰가 중요하다.
콘솔에서 수동으로 바꾸면 "정확히 뭐가 바뀌었는지" 확인하기 어렵다.
IaC에서는 diff가 곧 변경 내역이기 때문에 리뷰가 명확하다.

### 시나리오 3: 장애 복구

금요일 오후 5시, AI 에이전트가 제안한 변경이 머지된 후 서비스 장애가 발생했다.

```bash
# 1. 최근 커밋 확인
git log --oneline -5
# a1b2c3d Auto Scaling Group으로 변경  ← 이 커밋이 원인
# e4f5g6h S3 버킷 암호화 추가
# ...

# 2. 해당 커밋을 되돌리기
git revert a1b2c3d

# 3. main에 푸시 → CI/CD가 자동으로 이전 상태로 복구
git push origin main

# 총 소요 시간: 2~3분
```

수동 관리였다면?
담당자가 콘솔에 접속해서 ALB를 삭제하고, ASG를 삭제하고, Launch Template을 삭제하고, EC2를 다시 만들어야 한다.
금요일 밤이 사라진다.

---

## 7. 리소스 정리

실습이 끝나면 AWS 리소스를 정리해야 한다.
요금이 나가니 반드시 정리하자.

```bash
# dev 스택 리소스 삭제
pulumi stack select dev
pulumi destroy --yes

# prod 스택 리소스 삭제 (만들었다면)
pulumi stack select prod
pulumi destroy --yes
```

`pulumi destroy`도 멱등하다.
이미 삭제된 리소스가 있으면 건너뛰고, 남아있는 것만 삭제한다.

---

## 8. 그 다음 단계: K8s + ArgoCD + Helm

이 글에서 구현한 GitOps는 솔직히 말하면 "간이 버전"이다.
GitHub Actions가 `pulumi up`을 실행하는 방식은 GitOps의 원칙을 따르긴 하지만, 한계가 있다.

좀 더 본격적인 GitOps를 하려면 쿠버네티스(K8s) + ArgoCD + Helm Chart 조합이 필요하다.

왜 그런지 정리해보면 이렇다.

### GitHub Actions 방식의 한계

이 글에서 구현한 방식은 Push 기반이다.
CI가 코드를 감지해서 인프라에 "밀어넣는(Push)" 구조다.

이 구조에서는 몇 가지 문제가 생긴다.

- **CI가 인프라에 대한 강력한 권한을 가진다.** GitHub Actions에 AWS 키를 넣어야 하고, 그 키가 유출되면 인프라 전체가 위험해진다.
- **실시간 조정이 안된다.** Drift Detection을 cron으로 돌리고 있지만, 하루에 한 번 체크하는 것과 실시간으로 감지하는 것은 다르다. 그 사이에 누군가 콘솔에서 변경하면 최대 24시간 동안 모른다.
- **애플리케이션 배포와 인프라 배포가 분리되지 않는다.** 실제 서비스에서는 앱 배포(컨테이너 이미지 교체)와 인프라 변경(VPC, DB 등)의 주기와 리스크가 완전히 다르다.

### ArgoCD가 해결하는 것

ArgoCD는 Pull 기반이다.
클러스터 안에서 에이전트가 돌면서 Git 저장소를 지속적으로 감시한다.

```
GitHub Actions (Push 기반)
  CI → 인프라에 명령을 보낸다

ArgoCD (Pull 기반)  
  클러스터 안의 에이전트 ← Git 저장소를 계속 감시한다
```

차이가 크다.

- **CI에 인프라 권한이 필요 없다.** ArgoCD가 클러스터 안에서 동작하기 때문에, 외부에서 접근 키를 관리할 필요가 없다.
- **실시간 조정이 된다.** 3분마다 Git과 실제 상태를 비교하고, 차이가 있으면 자동으로 원래 상태로 맞춘다. 누가 `kubectl`로 직접 수정해도 ArgoCD가 되돌린다.
- **Self-Healing이 가능하다.** 파드가 죽으면 다시 살리고, 설정이 바뀌면 원래대로 돌린다. GitOps에서 말하는 "지속적 조정"이 이것이다.

### Helm Chart가 필요한 이유

Pulumi에서 `Pulumi.dev.yaml`과 `Pulumi.prod.yaml`로 환경별 설정을 분리한 것처럼, Helm Chart도 같은 역할을 한다.
다만 Helm은 K8s 생태계의 사실상 표준 패키지 매니저라서, 오픈소스 차트를 가져다 쓸 수 있다는 차이가 있다.

```yaml
# values-dev.yaml
replicaCount: 2
resources:
  limits:
    memory: 512Mi

# values-prod.yaml
replicaCount: 10
resources:
  limits:
    memory: 2Gi
```

애플리케이션의 K8s 매니페스트를 Helm Chart로 패키징하고, 환경별 values 파일로 설정을 분리하고, ArgoCD가 Git의 Chart를 감시하면서 자동으로 클러스터에 반영한다.
여기까지 갖춰지면 GitOps의 4가지 원칙을 빠짐없이 충족하게 된다.

### 이 글에서 다루지 않은 이유

솔직히 K8s + ArgoCD + Helm까지 한 번에 다루면 시작 자체가 너무 어려워진다.

K8s 클러스터를 띄워야 하고, ArgoCD를 설치해야 하고, Helm Chart 문법을 배워야 하고, 그 위에서 GitOps를 구현해야 한다.
인프라를 코드로 관리하는 것 자체가 처음인 팀에게 이 모든 것을 한꺼번에 요구하면, "그냥 콘솔이 낫겠다"는 결론에 도달할 가능성이 높다.

그래서 이 글에서는 진입 장벽을 최대한 낮추려고 했다.
Pulumi + GitHub Actions만으로도 GitOps의 핵심 원칙은 충분히 체험할 수 있다.
여기서 한 발짝 나아가고 싶다면, 그때 K8s + ArgoCD + Helm을 도입하면 된다.

순서를 정리하면 이렇다.

1. **(이 글)** Pulumi + GitHub Actions → IaC와 GitOps의 기본 체험
2. **(다음 단계)** 컨테이너화 + K8s 도입 → 애플리케이션을 컨테이너로 배포
3. **(최종)** ArgoCD + Helm Chart → 완전한 Pull 기반 GitOps 구현

한 번에 3번으로 가려고 하면 대부분 중간에 포기한다.
1번부터 시작해서 익숙해진 다음에 올라가는 것을 권장한다.

---

## 마치며

이 글에서 다룬 내용을 정리하면 다음과 같다.

1. S3 버킷 하나 만드는 것부터 시작했다
2. VPC, Security Group, EC2를 추가했다
3. 환경별 설정을 분리했다
4. SSH 포트를 열었다가 되돌려봤다
5. GitHub Actions로 PR Preview, 자동 배포, Drift Detection을 구현했다

이 모든 과정에서 일관된 패턴이 하나 있다.

> 코드를 수정하고 → Git에 커밋하고 → PR을 만들고 → 리뷰 후 머지하면 → 인프라가 자동으로 반영된다.

에이전틱 코딩 시대에 이 패턴은 더 중요해진다.
AI 에이전트가 하루에 수십 번 인프라 변경을 제안할 수 있는 환경에서, 모든 변경이 Git에 기록되고, 코드 리뷰를 거쳐 적용되고, 문제가 생기면 `git revert` 한 번으로 복구되는 환경이 없으면 문제가 생긴다.

콘솔 클릭이 익숙해서 IaC가 번거로워 보일 수 있다.
처음에는 나도 그랬다.
하지만 한번 구축해놓으면 AI 에이전트가 인프라 코드를 작성하고, CI/CD가 자동으로 검증하고, 문제가 생기면 한 줄로 복구되는 환경이 갖춰진다.
그 환경에 익숙해지고 나면 다시 콘솔 클릭으로 돌아가기 어렵다.

완전한 GitOps(K8s + ArgoCD + Helm)까지 가려면 갈 길이 남았지만, 그 여정의 첫 발은 이미 뗐다.
다음 글에서는 이 인프라 위에 컨테이너 기반 배포를 도입하는 과정을 다뤄볼 예정이다.
