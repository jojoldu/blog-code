# AI 시대의 인프라 생존 전략: 왜 IaC와 Git이 필수인가

## 들어가며: AI 시대, 왜 Git이 더 중요해졌는가

AI가 코드를 짜고, 데이터 분석 모델을 돌리고, 인프라 구성을 제안하는 시대다. AI는 혁신적인 속도를 제공하지만, 동시에 **'불확실성'**이라는 숙제도 안겨주었다.

AI가 제안한 결과물이 의도와 다를 때, 혹은 예상치 못한 오류를 발생시켰을 때 우리에게 가장 필요한 것은 무엇일까? 바로 **"언제든 안전했던 과거의 상태로 되돌릴 수 있는 능력"**이다.

소프트웨어 개발에서 Git은 이미 단순한 도구를 넘어 '실수로부터의 자유'를 보장하는 필수 인프라가 되었다. AI 시대의 폭발적인 변화 속에서 Git의 버전 관리는 우리가 믿고 의지할 수 있는 유일한 검증된 타임머신이다.

Git이 제공하는 핵심 가치는 다음과 같다.

- **버전 관리**: 모든 변경 이력이 기록되어 언제든 과거로 돌아갈 수 있음
- **멱등성**: 같은 커밋을 체크아웃하면 항상 같은 상태가 보장됨
- **실험의 자유**: 브랜치를 만들어 마음껏 시도하고, 실패하면 버리면 됨
- **협업과 리뷰**: PR을 통해 변경사항을 검토하고 승인할 수 있음

AI와 함께 일할 때 이 가치들은 더욱 빛을 발한다. AI가 만든 코드가 잘못되었다면? `git revert` 한 번이면 된다. AI의 제안이 마음에 들지 않는다면? 브랜치를 버리면 그만이다.

## 그렇다면 인프라는?

소프트웨어 코드는 Git으로 관리하면서, 정작 그 코드가 돌아가는 **인프라는 왜 Git으로 관리하지 않는 걸까?**

많은 회사에서 여전히 AWS 콘솔에 접속해 클릭으로 리소스를 만든다. 변경 이력은 담당자의 머릿속에만 존재하고, "어제 누가 Security Group 바꿨어?"라는 질문에 아무도 답하지 못한다. 프로덕션과 스테이징 환경이 왜 다른지도 모른다.

AI 시대에 이런 환경은 치명적이다. AI가 콘솔에서 수동으로 설정을 바꾼다면, 어떤 변경이 장애를 일으켰는지 알 수 없다. 장애 발생 시 "어제 상태"로 즉시 되돌릴 방법도 없다.

바로 여기서 **IaC(Infrastructure as Code)** 가 필요하다. 인프라를 코드로 정의하면, 소프트웨어 개발에서 누리던 Git의 모든 이점을 인프라에서도 그대로 얻을 수 있다.

> 코드에서 Git이 필수이듯, 인프라에서도 Git은 필수다. IaC는 그것을 가능하게 한다.

---

## 1. GitOps: Git을 중심에 둔 운영 패러다임

IaC로 인프라를 코드화했다면, 그 다음 단계는 **GitOps**다. GitOps는 Git 저장소를 **단일 진실 공급원(Single Source of Truth)**으로 삼아 인프라와 애플리케이션을 운영하는 방법론이다.

### OpenGitOps 표준 원칙 (v1.0.0)

[OpenGitOps](https://opengitops.dev/)는 CNCF(Cloud Native Computing Foundation) 산하 GitOps Working Group에서 관리하는 오픈소스 표준이다. GitOps를 도입하려는 조직에게 구조화된 표준과 모범 사례를 제공한다.

OpenGitOps에서 정의한 GitOps의 4가지 핵심 원칙은 다음과 같다.

| 원칙 | 설명 |
|------|------|
| **1. Declarative (선언적)** | GitOps로 관리되는 시스템은 원하는 상태를 선언적으로 표현해야 한다. "어떻게"가 아니라 "무엇"을 정의한다. |
| **2. Versioned and Immutable (버전 관리 및 불변성)** | 원하는 상태는 불변성을 보장하고, 버전 관리되며, 완전한 버전 히스토리를 유지하는 방식으로 저장된다. |
| **3. Pulled Automatically (자동 Pull)** | 소프트웨어 에이전트가 소스(Git)에서 원하는 상태 선언을 자동으로 Pull 한다. |
| **4. Continuously Reconciled (지속적 조정)** | 소프트웨어 에이전트가 지속적으로 실제 시스템 상태를 관찰하고, 원하는 상태와 다르면 자동으로 맞춘다. |

### 왜 이 원칙들이 중요한가?


원칙 1: Declarative (선언적)
> "EC2 인스턴스를 생성하라" (X) - 명령형
> "t3.large EC2 인스턴스가 존재해야 한다" (O) - 선언형

원칙 2: Versioned and Immutable (버전 관리 및 불변성)
> 모든 변경은 Git 커밋으로 기록 -> 누가, 언제, 무엇을 변경했는지 추적 가능
> 한번 커밋된 상태는 변경 불가 -> 신뢰할 수 있는 히스토리

원칙 3: Pulled Automatically (자동 Pull)
> 개발자가 pulumi up 실행 (X) - Push 방식
> ArgoCD/Flux가 Git 변경 감지 후 자동 적용 (O) - Pull 방식

원칙 4: Continuously Reconciled (지속적 조정)
> 누군가 콘솔에서 수동 변경 -> 에이전트가 감지 -> Git 상태로 자동 복구


### GitOps가 AI 시대에 중요한 이유

OpenGitOps의 4가지 원칙은 AI 시대에 더욱 빛을 발한다.


[기존 방식 - 원칙 위반]
AI 제안 -> 콘솔에서 수동 적용 (Pull 아님) -> 이력 없음 (버전 관리 안됨) -> 문제 발생 -> 복구 불가

[GitOps 방식 - 원칙 준수]
AI 제안 -> PR 생성 (선언적) -> 코드 리뷰 -> 머지 (버전 관리) -> 자동 적용 (Pull) -> 문제 발생 -> git revert -> 자동 복구 (지속적 조정)


GitOps 환경에서는 AI가 아무리 많은 변경을 제안해도, 모든 것이 Git 히스토리에 남는다.  
문제가 생기면 해당 커밋만 되돌리면 인프라가 자동으로 이전 상태로 복구된다.

### IaC vs GitOps: 무엇이 다른가?

| 구분 | IaC | GitOps |
|------|-----|--------|
| **정의** | 인프라를 코드로 작성 | Git을 중심으로 인프라 운영 |
| **초점** | 코드화 (선언적 정의) | 운영 프로세스 (자동화된 조정) |
| **적용 방식** | Push (개발자가 명령 실행) | Pull (에이전트가 Git 감시 후 자동 적용) |
| **상태 관리** | 코드로 상태 정의 | 코드와 실제 상태의 지속적 동기화 |
| **관계** | GitOps의 기반 | IaC를 활용한 운영 방법론 |

IaC는 인프라를 코드로 만드는 "도구"이고, GitOps는 그 코드를 Git 중심으로 운영하는 "방법론"이다.  
OpenGitOps 원칙에 따르면, IaC만으로는 충분하지 않다.  
선언적으로 정의된 코드(IaC)를 버전 관리하고(Git), 자동으로 Pull하여 지속적으로 조정하는(GitOps Agent) 전체 시스템이 필요하다.

AI 시대에는 둘 다 필수다.

---

## 2. AI 시대, 인프라의 멱등성과 원복이 중요한 이유

AI는 확률적으로 최선의 답을 내놓지만, 그것이 항상 우리 환경에 완벽하다는 보장은 없다.  
이때 인프라가 IaC 기반의 GitOps 환경으로 구축되어 있지 않다면 다음과 같은 위기에 직면한다.

- **추적 불가**: AI가 콘솔에서 수동으로 설정을 바꾼다면, 어떤 변경이 장애를 일으켰는지 알 수 없다.
- **원복 불가**: 장애 발생 시 "어제 상태"로 즉시 되돌릴 방법이 없다.
- **멱등성 상실**: 같은 명령을 내려도 실행할 때마다 결과가 달라진다면 AI에게 자동화를 맡길 수 없다.

### 멱등성(Idempotency)이란?

> 연산을 여러 번 적용하더라도 결과가 달라지지 않는 성질을 의미한다.  
> 즉, 현재 상태가 어떻든 코드가 정의한 '최종 상태'로 항상 동일하게 맞춰주는 능력이다.

멱등성이 없는 경우 (쉘 스크립트)
```
aws ec2 run-instances ...  # 실행할 때마다 새 인스턴스 생성
```

멱등성이 있는 경우 (IaC)

```
"webServer라는 EC2가 있어야 해"  # 없으면 생성, 있으면 유지
```


## 3. 수동 관리 vs IaC 비교

| 특징 | 수동 관리 (Click-ops) | IaC (Pulumi + Git) |
|------|----------------------|-------------------|
| **속도** | 초기엔 빠르나 규모 커지면 한계 | 항상 일정하고 빠름 |
| **안정성** | 사람의 실수에 매우 취약함 | 코드 리뷰 및 사전 테스트 가능 |
| **버전 관리** | 불가능 (이력 확인 어려움) | Git을 통한 완벽한 히스토리 관리 |
| **원복** | 불가능 (수작업 복구 필요) | git revert로 즉시 복구 |
| **AI 연동** | 불가능 | 매우 용이 (Code Generation) |

---

## 4. 왜 Pulumi인가?

Terraform도 훌륭하지만, **Pulumi**는 TypeScript, Python 같은 실제 프로그래밍 언어를 사용한다. 이는 LLM(대규모 언어 모델)이 가장 잘 이해하고 생성할 수 있는 형태라는 점에서 AI 시대에 강력한 강점을 가진다.

| 특징 | 설명 |
|------|------|
| **익숙한 언어** | TypeScript, Python, Go, C# 등 지원 |
| **타입 안전성** | IDE 자동완성, 컴파일 타임 오류 검출 |
| **재사용성** | 함수, 클래스, 모듈로 추상화 가능 |
| **테스트 가능** | 유닛 테스트, 통합 테스트 작성 가능 |
| **AI 친화적** | LLM이 잘 이해하는 범용 언어 사용 |

---

## 5. 실전 예제: AWS + Pulumi + Git

### 프로젝트 구조

```
my-infrastructure/
├── index.ts           # 메인 인프라 정의
├── components/
│   ├── vpc.ts         # VPC 컴포넌트
│   ├── webServer.ts   # EC2 컴포넌트
│   └── storage.ts     # S3 컴포넌트
├── Pulumi.yaml        # 프로젝트 설정
├── Pulumi.dev.yaml    # 개발 환경 설정
├── Pulumi.prod.yaml   # 프로덕션 환경 설정
└── package.json
```

### Step 1: VPC 구성 (components/vpc.ts)

```typescript
import * as pulumi from "@pulumi/pulumi";
import * as aws from "@pulumi/aws";

export interface VpcArgs {
    cidrBlock: string;
    environment: string;
}

export class Vpc extends pulumi.ComponentResource {
    public readonly vpc: aws.ec2.Vpc;
    public readonly publicSubnets: aws.ec2.Subnet[];
    public readonly privateSubnets: aws.ec2.Subnet[];

    constructor(name: string, args: VpcArgs, opts?: pulumi.ComponentResourceOptions) {
        super("custom:network:Vpc", name, {}, opts);

        this.vpc = new aws.ec2.Vpc(`${name}-vpc`, {
            cidrBlock: args.cidrBlock,
            enableDnsHostnames: true,
            enableDnsSupport: true,
            tags: {
                Name: `${name}-vpc`,
                Environment: args.environment,
                ManagedBy: "pulumi",
            },
        }, { parent: this });

        const azs = aws.getAvailabilityZones({ state: "available" });

        this.publicSubnets = [];
        for (let i = 0; i < 2; i++) {
            const subnet = new aws.ec2.Subnet(`${name}-public-${i}`, {
                vpcId: this.vpc.id,
                cidrBlock: `10.0.${i}.0/24`,
                availabilityZone: azs.then(az => az.names[i]),
                mapPublicIpOnLaunch: true,
                tags: { Name: `${name}-public-${i}`, Type: "public" },
            }, { parent: this });
            this.publicSubnets.push(subnet);
        }

        this.privateSubnets = [];
        for (let i = 0; i < 2; i++) {
            const subnet = new aws.ec2.Subnet(`${name}-private-${i}`, {
                vpcId: this.vpc.id,
                cidrBlock: `10.0.${i + 10}.0/24`,
                availabilityZone: azs.then(az => az.names[i]),
                tags: { Name: `${name}-private-${i}`, Type: "private" },
            }, { parent: this });
            this.privateSubnets.push(subnet);
        }

        this.registerOutputs({
            vpcId: this.vpc.id,
            publicSubnetIds: this.publicSubnets.map(s => s.id),
            privateSubnetIds: this.privateSubnets.map(s => s.id),
        });
    }
}
```

### Step 2: 웹 서버 구성 (components/webServer.ts)

```typescript
import * as pulumi from "@pulumi/pulumi";
import * as aws from "@pulumi/aws";

export interface WebServerArgs {
    vpcId: pulumi.Input<string>;
    subnetId: pulumi.Input<string>;
    instanceType: string;
    environment: string;
}

export class WebServer extends pulumi.ComponentResource {
    public readonly instance: aws.ec2.Instance;
    public readonly securityGroup: aws.ec2.SecurityGroup;
    public readonly publicIp: pulumi.Output<string>;

    constructor(name: string, args: WebServerArgs, opts?: pulumi.ComponentResourceOptions) {
        super("custom:compute:WebServer", name, {}, opts);

        this.securityGroup = new aws.ec2.SecurityGroup(`${name}-sg`, {
            vpcId: args.vpcId,
            description: "Security group for web server",
            ingress: [
                { protocol: "tcp", fromPort: 80, toPort: 80, cidrBlocks: ["0.0.0.0/0"] },
                { protocol: "tcp", fromPort: 443, toPort: 443, cidrBlocks: ["0.0.0.0/0"] },
                { protocol: "tcp", fromPort: 22, toPort: 22, cidrBlocks: ["0.0.0.0/0"] },
            ],
            egress: [
                { protocol: "-1", fromPort: 0, toPort: 0, cidrBlocks: ["0.0.0.0/0"] },
            ],
            tags: { Name: `${name}-sg`, ManagedBy: "pulumi" },
        }, { parent: this });

        const ami = aws.ec2.getAmi({
            mostRecent: true,
            owners: ["amazon"],
            filters: [{ name: "name", values: ["amzn2-ami-hvm-*-x86_64-gp2"] }],
        });

        this.instance = new aws.ec2.Instance(`${name}-instance`, {
            ami: ami.then(a => a.id),
            instanceType: args.instanceType,
            subnetId: args.subnetId,
            vpcSecurityGroupIds: [this.securityGroup.id],
            tags: { Name: name, Environment: args.environment, ManagedBy: "pulumi" },
        }, { parent: this });

        this.publicIp = this.instance.publicIp;
        this.registerOutputs({ instanceId: this.instance.id, publicIp: this.publicIp });
    }
}
```

### Step 3: S3 스토리지 구성 (components/storage.ts)

```typescript
import * as pulumi from "@pulumi/pulumi";
import * as aws from "@pulumi/aws";

export interface StorageArgs {
    bucketName: string;
    environment: string;
}

export class Storage extends pulumi.ComponentResource {
    public readonly bucket: aws.s3.BucketV2;
    public readonly bucketId: pulumi.Output<string>;

    constructor(name: string, args: StorageArgs, opts?: pulumi.ComponentResourceOptions) {
        super("custom:storage:Storage", name, {}, opts);

        this.bucket = new aws.s3.BucketV2(`${name}-bucket`, {
            bucket: args.bucketName,
            tags: { Environment: args.environment, ManagedBy: "pulumi", VersionControl: "Git" },
        }, { parent: this });

        const bucketVersioning = new aws.s3.BucketVersioningV2(`${name}-versioning`, {
            bucket: this.bucket.id,
            versioningConfiguration: { status: "Enabled" },
        }, { parent: this });

        this.bucketId = this.bucket.id;
        this.registerOutputs({ bucketId: this.bucketId });
    }
}
```

### Step 4: 메인 인프라 정의 (index.ts)

```typescript
import * as pulumi from "@pulumi/pulumi";
import { Vpc } from "./components/vpc";
import { WebServer } from "./components/webServer";
import { Storage } from "./components/storage";

const config = new pulumi.Config();
const environment = config.require("environment");
const instanceType = config.get("instanceType") || "t3.micro";

const network = new Vpc("main", { cidrBlock: "10.0.0.0/16", environment });

const webServer = new WebServer("web", {
    vpcId: network.vpc.id,
    subnetId: network.publicSubnets[0].id,
    instanceType,
    environment,
});

const storage = new Storage("data", {
    bucketName: `my-app-storage-${environment}`,
    environment,
});

export const vpcId = network.vpc.id;
export const webServerPublicIp = webServer.publicIp;
export const webServerUrl = pulumi.interpolate`http://${webServer.publicIp}`;
export const storageBucketId = storage.bucketId;
```

### Step 5: 환경별 설정 파일

**Pulumi.dev.yaml**
```yaml
config:
  aws:region: ap-northeast-2
  my-infrastructure:environment: dev
  my-infrastructure:instanceType: t3.micro
```

**Pulumi.prod.yaml**
```yaml
config:
  aws:region: ap-northeast-2
  my-infrastructure:environment: prod
  my-infrastructure:instanceType: t3.large
```

---

## 6. Git 워크플로우: 인프라 변경의 정석

### 브랜치 전략

```
main (production)
  └── develop
        ├── feature/add-rds
        ├── feature/update-security-group
        └── hotfix/fix-vpc-routing
```

### 변경 프로세스

```bash
# 1. 기능 브랜치 생성
git checkout -b feature/add-rds

# 2. 인프라 코드 수정 후 미리보기
pulumi preview

# 3. 코드 커밋
git add .
git commit -m "feat: Add RDS instance for user data"

# 4. PR 생성 및 코드 리뷰
git push origin feature/add-rds

# 5. 리뷰 완료 후 머지 & 적용
git checkout main
git merge feature/add-rds
pulumi up --yes
```

### 롤백이 필요할 때

```bash
# 방법 1: Git revert 후 재적용
git revert HEAD
pulumi up --yes

# 방법 2: 특정 커밋으로 되돌리기
git checkout <previous-commit-hash> -- .
pulumi up --yes

# 방법 3: Pulumi 히스토리에서 직접 복원
pulumi stack history
pulumi stack export --version <version> > backup.json
```

---

## 7. CI/CD 파이프라인 (GitHub Actions)

```yaml
name: Infrastructure CI/CD

on:
  pull_request:
    branches: [main]
  push:
    branches: [main]

jobs:
  preview:
    if: github.event_name == 'pull_request'
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
          command: preview
          stack-name: dev
        env:
          PULUMI_ACCESS_TOKEN: ${{ secrets.PULUMI_ACCESS_TOKEN }}

  deploy:
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
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
          stack-name: prod
        env:
          PULUMI_ACCESS_TOKEN: ${{ secrets.PULUMI_ACCESS_TOKEN }}
```

---

## 8. AI와 함께하는 IaC 워크플로우

IaC 환경이 갖춰지면, AI를 활용한 인프라 관리가 가능해진다.

```
Human: "현재 웹서버 트래픽이 증가하고 있어. 오토스케일링 그룹으로 변경해줘"

AI: "현재 index.ts를 분석한 결과, 단일 EC2 인스턴스로 구성되어 있다.
     Auto Scaling Group + ALB 구성으로 변경하는 코드를 작성할게.
     
     [코드 생성]
     
     pulumi preview로 변경사항을 먼저 확인해봐."
```

---

## 마치며

AI 시대의 인프라 관리는 더 이상 선택이 아니다.

- **버전 관리**: 모든 변경 이력 추적
- **멱등성**: 예측 가능한 결과
- **원복 가능성**: 실패해도 빠른 복구
- **협업**: 코드 리뷰를 통한 품질 관리
- **자동화**: CI/CD 파이프라인 통합
- **GitOps**: Git을 단일 진실 공급원으로 삼는 운영

콘솔 클릭에서 벗어나, 코드로 인프라를 관리하라. 그래야만 AI와 함께 안전하게 인프라를 발전시킬 수 있다.
