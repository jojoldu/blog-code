# Claude Code 활용도 대시보드를 직접 만들어보자

> Claude Code Plugin + Hooks + BigQuery + React 대시보드를 하루 만에 구축하는 과정을 정리했습니다.

## 들어가며

최근 Dinii 엔지니어링 팀이 "전 엔지니어가 Claude Code를 100% 활용한다"는 목표를 세우고, 활용도를 가시화하는 대시보드를 만든 사례가 화제가 되었습니다. 핵심 인사이트는 명확했습니다. "**보이지 않으면 개선할 수 없다**"는 것이죠.

이 글에서는 그 사례를 참고하여, Claude Code의 Hooks와 Plugin 시스템을 활용해 팀의 Claude Code 활용도를 추적하는 대시보드를 **직접 구축하는 과정**을 단계별로 정리합니다.

## 전체 아키텍처

구축할 시스템의 전체 구조는 다음과 같습니다.

```
┌─────────────────────────────────────────────────────┐
│  개발자 로컬 환경                                      │
│                                                       │
│  Claude Code ──→ Hook (PostToolUse / Stop)            │
│                    │                                   │
│                    ▼                                   │
│              데이터 수집 스크립트                         │
│              (transcript JSONL 파싱)                    │
└────────────────────┬────────────────────────────────┘
                     │ HTTP POST
                     ▼
          ┌─────────────────────┐
          │  API Gateway (AWS)   │
          │  + Go Lambda          │
          │  (고속 수집 레이어)      │
          └──────────┬──────────┘
                     │
                     ▼
┌────────────────────────────────────────────┐
│  BigQuery (GCP) — 통합 데이터 허브            │
│                                              │
│  claude_usage.sessions   ← Claude Code 활용  │
│  github.pull_requests    ← GitHub 이벤트      │
│  jira.issues             ← Jira 이슈          │
│  deploy.logs             ← 배포 로그           │
│  incident.logs           ← 장애 로그           │
└────────────────────┬─────────────────────────┘
                     │
                     ▼
          ┌─────────────────────┐
          │  대시보드 (React)     │
          │  + Recharts           │
          │  + Looker Studio      │
          └─────────────────────┘
```

수집은 AWS(Go Lambda)에서 고속으로 처리하고, 저장과 분석은 BigQuery로 통일합니다. Jira, GitHub, 배포/장애 로그가 이미 BigQuery에 있으므로, Claude Code 활용 데이터도 같은 곳에 두면 교차 분석이 가능해집니다.

## Step 1. Claude Code Transcript 구조 이해하기

대시보드를 만들기 전에, Claude Code가 어떤 데이터를 남기는지 이해해야 합니다.

Claude Code는 세션 중 모든 상호작용을 JSONL(JSON Lines) 형식으로 `~/.claude/projects/` 경로에 기록합니다. 각 라인이 하나의 이벤트를 나타내며, 스킬 사용, 서브에이전트 호출, MCP 도구 사용 등을 모두 추적할 수 있습니다.

### Transcript 예시 — 스킬 사용

```json
{
  "sessionId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "cwd": "/Users/username/Desktop/your-project",
  "message": {
    "model": "claude-sonnet-4-6",
    "role": "assistant",
    "content": [
      {
        "type": "tool_use",
        "name": "Skill",
        "input": {
          "skill": "frontend-design"
        }
      }
    ]
  },
  "timestamp": "2026-02-04T11:22:07.768Z"
}
```

### Transcript 예시 — 서브에이전트 사용

```json
{
  "sessionId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "message": {
    "model": "claude-sonnet-4-6",
    "role": "assistant",
    "content": [
      {
        "type": "tool_use",
        "name": "Agent",
        "input": {
          "prompt": "테스트 코드를 작성해줘",
          "subagent_type": "test-runner"
        }
      }
    ]
  },
  "timestamp": "2026-02-04T11:30:00.000Z"
}
```

### 추적할 이벤트 분류

| 카테고리 | 식별 기준 | 예시 |
|---------|----------|------|
| 스킬 (Skill) | `name === "Skill"` | `input.skill: "frontend-design"` |
| 서브에이전트 (Agent) | `name === "Agent"` | `input.subagent_type: "test-runner"` |
| MCP 도구 | `name.startsWith("mcp__")` | `mcp__github__create_pr` |
| 슬래시 커맨드 | `type === "slash_command"` | `/commit`, `/review` |
| 일반 도구 | 그 외 `tool_use` | `Bash`, `Edit`, `Write`, `Read` |

## Step 2. 데이터 수집용 Hook 스크립트 작성

Claude Code의 Hooks 시스템을 이용하면, 특정 이벤트가 발생할 때마다 자동으로 스크립트를 실행할 수 있습니다. 여기서는 **Stop** 이벤트(Claude의 응답이 완료될 때)에 후크를 걸어, 해당 세션의 transcript를 파싱하고 수집 서버로 전송합니다.

### 2-1. 수집 스크립트 작성

`~/.claude/hooks/collect-usage.sh`:

```bash
#!/bin/bash
# Claude Code 사용 데이터를 수집하여 서버로 전송하는 Hook 스크립트

# stdin에서 hook 입력 데이터 읽기
INPUT=$(cat)

# 환경 변수에서 필요한 정보 추출
TRANSCRIPT_PATH=$(echo "$INPUT" | jq -r '.transcript_path // empty')
SESSION_ID=$(echo "$INPUT" | jq -r '.session_id // empty')
CWD=$(echo "$INPUT" | jq -r '.cwd // empty')

# transcript 경로가 없으면 종료
if [ -z "$TRANSCRIPT_PATH" ] || [ ! -f "$TRANSCRIPT_PATH" ]; then
  exit 0
fi

# 특정 디렉토리에서만 수집 (선택사항: 회사 프로젝트만 필터링)
# if [[ "$CWD" != *"your-company"* ]]; then
#   exit 0
# fi

# Git 사용자 정보 추출
GIT_USER=$(git config user.email 2>/dev/null || echo "unknown")

# Transcript에서 도구 사용 이벤트 추출
TOOL_EVENTS=$(cat "$TRANSCRIPT_PATH" | jq -c '
  select(.message.role == "assistant")
  | select(.message.content)
  | .message.content[]
  | select(.type == "tool_use")
  | {
      tool_name: .name,
      tool_input_skill: (.input.skill // null),
      tool_input_subagent: (.input.subagent_type // null),
      tool_input_prompt: (if .name == "Agent" then (.input.prompt | .[0:100]) else null end)
    }
' 2>/dev/null)

# 모델 정보 추출
MODEL=$(cat "$TRANSCRIPT_PATH" | jq -r '
  select(.message.model) | .message.model
' 2>/dev/null | sort -u | head -1)

# 토큰 사용량 추출 (usage 필드가 있는 경우)
TOKEN_USAGE=$(cat "$TRANSCRIPT_PATH" | jq -c '
  select(.message.usage)
  | .message.usage
' 2>/dev/null | jq -s '{
  total_input: (map(.input_tokens // 0) | add),
  total_output: (map(.output_tokens // 0) | add)
}')

# 이벤트별 카운트 집계
SKILL_COUNT=$(echo "$TOOL_EVENTS" | jq -s '[.[] | select(.tool_name == "Skill")] | length')
AGENT_COUNT=$(echo "$TOOL_EVENTS" | jq -s '[.[] | select(.tool_name == "Agent")] | length')
MCP_COUNT=$(echo "$TOOL_EVENTS" | jq -s '[.[] | select(.tool_name | startswith("mcp__"))] | length')
TOTAL_TOOL_COUNT=$(echo "$TOOL_EVENTS" | jq -s 'length')

# 스킬 상세 목록
SKILLS_USED=$(echo "$TOOL_EVENTS" | jq -s '
  [.[] | select(.tool_name == "Skill") | .tool_input_skill]
  | group_by(.) | map({name: .[0], count: length})
')

# 서브에이전트 상세 목록
AGENTS_USED=$(echo "$TOOL_EVENTS" | jq -s '
  [.[] | select(.tool_name == "Agent") | .tool_input_subagent]
  | group_by(.) | map({name: .[0], count: length})
')

# MCP 도구 상세 목록
MCP_USED=$(echo "$TOOL_EVENTS" | jq -s '
  [.[] | select(.tool_name | startswith("mcp__")) | .tool_name]
  | group_by(.) | map({name: .[0], count: length})
')

# 수집 데이터 구성
PAYLOAD=$(jq -n \
  --arg session_id "$SESSION_ID" \
  --arg user "$GIT_USER" \
  --arg cwd "$CWD" \
  --arg model "$MODEL" \
  --arg timestamp "$(date -u +%Y-%m-%dT%H:%M:%SZ)" \
  --argjson skill_count "$SKILL_COUNT" \
  --argjson agent_count "$AGENT_COUNT" \
  --argjson mcp_count "$MCP_COUNT" \
  --argjson total_tool_count "$TOTAL_TOOL_COUNT" \
  --argjson skills_used "$SKILLS_USED" \
  --argjson agents_used "$AGENTS_USED" \
  --argjson mcp_used "$MCP_USED" \
  --argjson token_usage "$TOKEN_USAGE" \
  '{
    session_id: $session_id,
    user: $user,
    workspace: $cwd,
    model: $model,
    timestamp: $timestamp,
    skill_count: $skill_count,
    agent_count: $agent_count,
    mcp_count: $mcp_count,
    total_tool_count: $total_tool_count,
    skills_used: $skills_used,
    agents_used: $agents_used,
    mcp_used: $mcp_used,
    token_usage: $token_usage
  }')

# 수집 서버로 전송 (비동기)
curl -s -X POST \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer ${CLAUDE_USAGE_API_KEY}" \
  -d "$PAYLOAD" \
  "${CLAUDE_USAGE_API_URL:-http://localhost:3000}/api/usage" &

exit 0
```

### 2-2. 스크립트 실행 권한 부여

```bash
chmod +x ~/.claude/hooks/collect-usage.sh
```

## Step 3. Hook 설정 등록

이제 Claude Code 설정에 Hook을 등록합니다. 사용자 스코프(`~/.claude/settings.json`)에 등록하면 모든 프로젝트에서 동작합니다.

### 3-1. 사용자 설정에 Hook 추가

`~/.claude/settings.json`:

```json
{
  "hooks": {
    "Stop": [
      {
        "hooks": [
          {
            "type": "command",
            "command": "~/.claude/hooks/collect-usage.sh",
            "timeout": 10,
            "async": true
          }
        ]
      }
    ]
  }
}
```

핵심 포인트가 몇 가지 있습니다.

`"async": true`로 설정하면 Hook이 백그라운드에서 실행되어 Claude Code의 응답 속도에 영향을 주지 않습니다. `Stop` 이벤트를 사용하는 이유는, 하나의 대화 턴이 완료된 시점에 해당 세션의 전체 도구 사용 내역을 한 번에 파싱할 수 있기 때문입니다.

### 3-2. HTTP Hook으로 직접 전송하는 방법 (대안)

스크립트 대신 HTTP Hook을 사용하면 더 간결하게 구성할 수 있습니다.

```json
{
  "hooks": {
    "PostToolUse": [
      {
        "matcher": "Skill|Agent|mcp__*",
        "hooks": [
          {
            "type": "http",
            "url": "https://your-api.example.com/api/tool-usage",
            "timeout": 5,
            "headers": {
              "Authorization": "Bearer $CLAUDE_USAGE_API_KEY"
            },
            "allowedEnvVars": ["CLAUDE_USAGE_API_KEY"]
          }
        ]
      }
    ]
  }
}
```

이 방식은 개별 도구 사용마다 실시간으로 데이터를 전송하므로, 더 세밀한 추적이 가능합니다. `PostToolUse` 이벤트는 도구가 성공적으로 실행된 후 발생하며, `matcher`로 Skill, Agent, MCP 도구만 필터링합니다.

## Step 4. Plugin으로 패키징하여 팀에 배포

Hook 스크립트를 Claude Code Plugin으로 패키징하면, 팀원들이 두 줄의 명령어만으로 설치할 수 있습니다.

### 4-1. Plugin 디렉토리 구조

```
claude-usage-tracker/
├── plugin.json
├── hooks/
│   └── hooks.json
├── scripts/
│   └── collect-usage.sh
└── README.md
```

### 4-2. plugin.json

```json
{
  "name": "claude-usage-tracker",
  "version": "1.0.0",
  "description": "팀의 Claude Code 활용도를 추적하는 플러그인",
  "author": "your-team"
}
```

### 4-3. hooks/hooks.json

```json
{
  "hooks": {
    "Stop": [
      {
        "hooks": [
          {
            "type": "command",
            "command": "\"$CLAUDE_PLUGIN_DIR\"/../scripts/collect-usage.sh",
            "timeout": 10,
            "async": true
          }
        ]
      }
    ]
  }
}
```

### 4-4. GitHub Private 리포지토리로 배포

Plugin을 GitHub Private 리포지토리에 올리면, 조직 내부에서만 접근 가능하게 제한할 수 있습니다.

```bash
# 리포지토리 생성 및 Push
git init
git add .
git commit -m "feat: Claude Code usage tracker plugin"
git remote add origin git@github.com:your-org/claude-usage-tracker.git
git push -u origin main
```

### 4-5. 팀원 설치 방법

팀원에게 안내할 설치 명령어는 단 두 줄입니다.

```bash
/plugin marketplace add https://github.com/your-org/claude-usage-tracker.git
/plugin install claude-usage-tracker
```

Dinii 팀의 사례에서도 이 방식으로 팀 전원(100%)이 플러그인을 설치했다고 합니다. 설정 파일 편집이나 별도 스크립트 실행이 필요 없어, 도입 장벽이 매우 낮습니다.

### 왜 사용자 스코프에 설치하는가?

모노레포 구조에서는 개발자마다 Claude Code를 실행하는 디렉토리가 다를 수 있습니다.

```
monorepo/
├── packages/
│   ├── product-a/    ← 여기서 실행하는 개발자
│   ├── product-b/    ← 여기서 실행하는 개발자
│   └── product-c/
```

프로젝트 레벨에 Hook을 배치하면 하위 디렉토리에서 실행하는 개발자에게 동작하지 않습니다. 사용자 스코프에 설치하면 어떤 디렉토리에서든 계측이 가능합니다.

## Step 5. BigQuery 테이블 설계

수집된 데이터를 저장할 BigQuery 테이블을 설계합니다.

### 5-1. 메인 테이블: `claude_usage.sessions`

```sql
CREATE TABLE IF NOT EXISTS `your-project.claude_usage.sessions` (
  session_id STRING NOT NULL,
  user_email STRING NOT NULL,
  workspace STRING,
  model STRING,
  timestamp TIMESTAMP NOT NULL,

  -- 카테고리별 사용 횟수
  skill_count INT64 DEFAULT 0,
  agent_count INT64 DEFAULT 0,
  mcp_count INT64 DEFAULT 0,
  total_tool_count INT64 DEFAULT 0,

  -- 상세 사용 내역 (JSON 배열)
  skills_used JSON,
  agents_used JSON,
  mcp_used JSON,

  -- 토큰 사용량
  input_tokens INT64 DEFAULT 0,
  output_tokens INT64 DEFAULT 0,

  -- 메타데이터
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
)
PARTITION BY DATE(timestamp)
CLUSTER BY user_email, model;
```

### 5-2. 비용 계산용 뷰: `claude_usage.cost_view`

```sql
CREATE OR REPLACE VIEW `your-project.claude_usage.cost_view` AS
SELECT
  user_email,
  DATE(timestamp) AS usage_date,
  model,
  SUM(input_tokens) AS total_input_tokens,
  SUM(output_tokens) AS total_output_tokens,
  -- 모델별 비용 계산 (2026년 3월 기준 예시)
  CASE model
    WHEN 'claude-opus-4-6' THEN
      SUM(input_tokens) * 15.0 / 1000000 + SUM(output_tokens) * 75.0 / 1000000
    WHEN 'claude-sonnet-4-6' THEN
      SUM(input_tokens) * 3.0 / 1000000 + SUM(output_tokens) * 15.0 / 1000000
    WHEN 'claude-haiku-4-5-20251001' THEN
      SUM(input_tokens) * 0.80 / 1000000 + SUM(output_tokens) * 4.0 / 1000000
    ELSE 0
  END AS estimated_cost_usd
FROM `your-project.claude_usage.sessions`
GROUP BY user_email, usage_date, model;
```

### 5-3. 수집 API — Go Lambda + API Gateway

수집 레이어는 Go Lambda로 구현합니다. Go는 콜드 스타트가 50~100ms 수준으로 거의 없고, JSON 파싱 후 BigQuery에 넣는 단순 작업이라 실행 시간이 5~10ms 정도면 충분합니다. DB 커넥션 풀 관리도 필요 없고, 상시 서버를 운영할 필요도 없어서 이런 이벤트 기반 수집에 딱 맞습니다.

```go
// main.go
package main

import (
	"context"
	"encoding/json"
	"fmt"
	"os"
	"time"

	"cloud.google.com/go/bigquery"
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-lambda-go/lambda"
)

// BigQuery 테이블 스키마에 매핑되는 구조체
type UsageRecord struct {
	SessionID      string          `bigquery:"session_id"`
	UserEmail      string          `bigquery:"user_email"`
	Workspace      string          `bigquery:"workspace"`
	Model          string          `bigquery:"model"`
	Timestamp      time.Time       `bigquery:"timestamp"`
	SkillCount     int64           `bigquery:"skill_count"`
	AgentCount     int64           `bigquery:"agent_count"`
	MCPCount       int64           `bigquery:"mcp_count"`
	TotalToolCount int64           `bigquery:"total_tool_count"`
	SkillsUsed     string          `bigquery:"skills_used"`
	AgentsUsed     string          `bigquery:"agents_used"`
	MCPUsed        string          `bigquery:"mcp_used"`
	InputTokens    int64           `bigquery:"input_tokens"`
	OutputTokens   int64           `bigquery:"output_tokens"`
}

// Hook에서 전송하는 요청 본문
type UsageRequest struct {
	SessionID      string            `json:"session_id"`
	User           string            `json:"user"`
	Workspace      string            `json:"workspace"`
	Model          string            `json:"model"`
	Timestamp      string            `json:"timestamp"`
	SkillCount     int64             `json:"skill_count"`
	AgentCount     int64             `json:"agent_count"`
	MCPCount       int64             `json:"mcp_count"`
	TotalToolCount int64             `json:"total_tool_count"`
	SkillsUsed     json.RawMessage   `json:"skills_used"`
	AgentsUsed     json.RawMessage   `json:"agents_used"`
	MCPUsed        json.RawMessage   `json:"mcp_used"`
	TokenUsage     struct {
		TotalInput  int64 `json:"total_input"`
		TotalOutput int64 `json:"total_output"`
	} `json:"token_usage"`
}

// BigQuery 클라이언트를 Lambda 컨테이너 수명에 맞춰 재사용
var bqClient *bigquery.Client

func init() {
	ctx := context.Background()
	projectID := os.Getenv("GCP_PROJECT_ID")
	var err error
	bqClient, err = bigquery.NewClient(ctx, projectID)
	if err != nil {
		panic(fmt.Sprintf("BigQuery client init failed: %v", err))
	}
}

func handler(ctx context.Context, req events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {
	// API Key 검증
	apiKey := req.Headers["x-api-key"]
	if apiKey != os.Getenv("EXPECTED_API_KEY") {
		return events.APIGatewayProxyResponse{StatusCode: 401}, nil
	}

	// 요청 본문 파싱
	var input UsageRequest
	if err := json.Unmarshal([]byte(req.Body), &input); err != nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 400,
			Body:       `{"error":"invalid json"}`,
		}, nil
	}

	// 타임스탬프 파싱
	ts, err := time.Parse(time.RFC3339, input.Timestamp)
	if err != nil {
		ts = time.Now().UTC()
	}

	// BigQuery에 삽입
	record := &UsageRecord{
		SessionID:      input.SessionID,
		UserEmail:      input.User,
		Workspace:      input.Workspace,
		Model:          input.Model,
		Timestamp:      ts,
		SkillCount:     input.SkillCount,
		AgentCount:     input.AgentCount,
		MCPCount:       input.MCPCount,
		TotalToolCount: input.TotalToolCount,
		SkillsUsed:     string(input.SkillsUsed),
		AgentsUsed:     string(input.AgentsUsed),
		MCPUsed:        string(input.MCPUsed),
		InputTokens:    input.TokenUsage.TotalInput,
		OutputTokens:   input.TokenUsage.TotalOutput,
	}

	inserter := bqClient.Dataset("claude_usage").Table("sessions").Inserter()
	if err := inserter.Put(ctx, record); err != nil {
		return events.APIGatewayProxyResponse{
			StatusCode: 500,
			Body:       fmt.Sprintf(`{"error":"%s"}`, err.Error()),
		}, nil
	}

	return events.APIGatewayProxyResponse{
		StatusCode: 200,
		Body:       `{"success":true}`,
	}, nil
}

func main() {
	lambda.Start(handler)
}
```

#### Go Lambda 배포

```bash
# 빌드 (Lambda용 linux/amd64)
GOOS=linux GOARCH=amd64 go build -o bootstrap main.go

# zip 패키징
zip function.zip bootstrap

# Lambda 배포 (provided.al2023 런타임)
aws lambda create-function \
  --function-name claude-usage-collector \
  --runtime provided.al2023 \
  --handler bootstrap \
  --zip-file fileb://function.zip \
  --role arn:aws:iam::YOUR_ACCOUNT:role/lambda-bigquery-role \
  --environment "Variables={GCP_PROJECT_ID=your-project,EXPECTED_API_KEY=your-key}" \
  --timeout 10 \
  --memory-size 128
```

#### GCP 서비스 계정 인증

Lambda에서 BigQuery에 접근하려면 GCP 서비스 계정 키가 필요합니다. 서비스 계정 JSON 키를 Lambda 환경변수 `GOOGLE_APPLICATION_CREDENTIALS`에 경로를 지정하거나, AWS Secrets Manager에 저장한 뒤 Lambda에서 읽어오는 방식을 사용합니다.

```bash
# Secrets Manager에 GCP 키 저장
aws secretsmanager create-secret \
  --name gcp-bigquery-sa-key \
  --secret-string file://service-account-key.json
```

#### API Gateway 연결

```bash
# REST API 생성 후 Lambda 연동
aws apigateway create-rest-api --name claude-usage-api
# POST /usage 엔드포인트에 Lambda를 통합하고, API Key 인증 설정
```

Hook 설정에서 URL만 API Gateway 엔드포인트로 지정하면 됩니다:

```json
{
  "type": "http",
  "url": "https://abc123.execute-api.ap-northeast-2.amazonaws.com/prod/usage",
  "timeout": 5,
  "headers": {
    "x-api-key": "$CLAUDE_USAGE_API_KEY"
  },
  "allowedEnvVars": ["CLAUDE_USAGE_API_KEY"]
}
```

## Step 6. 대시보드 구축 — Claude Code에게 맡기기

여기가 가장 재미있는 부분입니다. 대시보드의 프론트엔드는 **Claude Code 자체를 이용해서** 만듭니다. BigQuery 스키마만 확실히 잡아두면, UI 구현은 Claude Code에게 프롬프트 한 번으로 위임할 수 있습니다.

### 6-1. Claude Code에게 대시보드 생성 요청

```
BigQuery의 claude_usage.sessions 테이블 데이터를 시각화하는
React 대시보드를 만들어줘.

포함할 차트:
1. 유저별 랭킹 (스킬+서브에이전트 사용 비율 기준)
2. 일별 활성 사용자 수 추이
3. 스킬/서브에이전트/MCP 카테고리별 사용 비율 파이차트
4. 모델별 토큰 사용량 & 예상 비용
5. 워크스페이스(리포지토리)별 사용 현황

기술 스택: React + Recharts + TailwindCSS
API 엔드포인트: /api/dashboard/* 형태로 구성
```

### 6-2. 대시보드에서 보고 싶은 핵심 지표

Dinii의 사례에서 특히 효과적이었던 지표들을 참고하면 다음과 같습니다.

**유저별 랭킹 — "양"과 "질"의 구분**

단순 대화 횟수가 아니라, 스킬과 서브에이전트 사용 비율을 함께 보여줘야 합니다. 대화량이 많아도 기본 도구만 사용하는 경우와, 대화량 대비 스킬 활용도가 높은 경우는 완전히 다른 패턴입니다.

```sql
-- "활용 품질" 점수 계산 예시
SELECT
  user_email,
  COUNT(*) AS total_sessions,
  SUM(skill_count + agent_count) AS advanced_usage,
  ROUND(
    SAFE_DIVIDE(
      SUM(skill_count + agent_count),
      SUM(total_tool_count)
    ) * 100, 1
  ) AS advanced_usage_ratio
FROM `your-project.claude_usage.sessions`
WHERE timestamp >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 7 DAY)
GROUP BY user_email
ORDER BY advanced_usage_ratio DESC;
```

**스킬 속인화(屬人化) 감지**

같은 기능의 스킬을 여러 사람이 각자 만들어 쓰고 있다면, 통합할 여지가 있습니다.

```sql
-- 유사 스킬 사용 패턴 분석
SELECT
  skill_name,
  COUNT(DISTINCT user_email) AS unique_users,
  COUNT(*) AS total_uses
FROM `your-project.claude_usage.sessions`,
  UNNEST(JSON_QUERY_ARRAY(skills_used)) AS skill
CROSS JOIN UNNEST([JSON_VALUE(skill, '$.name')]) AS skill_name
WHERE skill_name IS NOT NULL
GROUP BY skill_name
ORDER BY unique_users DESC;
```

### 6-3. 대시보드 API 엔드포인트 예시

```javascript
// routes/dashboard.js
const { BigQuery } = require('@google-cloud/bigquery');
const bigquery = new BigQuery();

// 유저별 랭킹
router.get('/api/dashboard/ranking', async (req, res) => {
  const [rows] = await bigquery.query({
    query: `
      SELECT
        user_email,
        COUNT(*) AS total_sessions,
        SUM(skill_count) AS skills,
        SUM(agent_count) AS agents,
        SUM(mcp_count) AS mcp_tools,
        ROUND(SAFE_DIVIDE(
          SUM(skill_count + agent_count),
          SUM(total_tool_count)
        ) * 100, 1) AS quality_score
      FROM \`your-project.claude_usage.sessions\`
      WHERE timestamp >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 30 DAY)
      GROUP BY user_email
      ORDER BY quality_score DESC
      LIMIT 20
    `
  });
  res.json(rows);
});

// 일별 활성 사용자 추이
router.get('/api/dashboard/daily-active', async (req, res) => {
  const [rows] = await bigquery.query({
    query: `
      SELECT
        DATE(timestamp) AS date,
        COUNT(DISTINCT user_email) AS active_users,
        COUNT(*) AS total_sessions
      FROM \`your-project.claude_usage.sessions\`
      WHERE timestamp >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 30 DAY)
      GROUP BY date
      ORDER BY date
    `
  });
  res.json(rows);
});

// 카테고리별 사용 비율
router.get('/api/dashboard/category-ratio', async (req, res) => {
  const [rows] = await bigquery.query({
    query: `
      SELECT
        SUM(skill_count) AS skills,
        SUM(agent_count) AS agents,
        SUM(mcp_count) AS mcp_tools,
        SUM(total_tool_count) - SUM(skill_count) - SUM(agent_count) - SUM(mcp_count) AS basic_tools
      FROM \`your-project.claude_usage.sessions\`
      WHERE timestamp >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 30 DAY)
    `
  });
  res.json(rows[0]);
});
```

## Step 7. 공식 Analytics 대시보드와의 차이점

Anthropic은 2026년에 공식 Claude Code Analytics 대시보드를 출시했습니다. `claude.ai/analytics/claude-code`에서 접근할 수 있으며, 수락된 코드 라인, 제안 수락률, 일일 활성 사용자, PR 기여도 등을 추적합니다.

그렇다면 직접 만들 필요가 있을까요? 목적이 다릅니다.

| 항목 | 공식 Analytics | 커스텀 대시보드 |
|------|-------------|--------------|
| 추적 대상 | 코드 수락률, PR 기여도 | 스킬/서브에이전트/MCP 사용 패턴 |
| 목적 | ROI 측정, 채택 현황 | 활용 "품질" 분석, 팀 학습 촉진 |
| 커스터마이징 | 제한적 | 자유로움 |
| 설치 | 별도 설정 없음 | Plugin 설치 필요 |
| GitHub 연동 | 지원 (PR 속성 분석) | 직접 구현 필요 |

공식 대시보드는 "Claude Code가 얼마나 코드에 기여했는가"를 보여주고, 커스텀 대시보드는 "팀원들이 Claude Code를 **어떻게** 활용하고 있는가"를 보여줍니다. 둘을 함께 사용하면 양적 기여도와 질적 활용도를 모두 파악할 수 있습니다.

## Step 8. OpenTelemetry 기반 모니터링과의 비교

Claude Code는 OpenTelemetry도 지원합니다. Datadog, Grafana, SigNoz 등 기존 관측성 스택과 연동할 수 있습니다. 그러나 OpenTelemetry로 수집할 수 있는 데이터는 "Claude Code를 사용했다" 수준까지입니다. 어떤 스킬을, 어떤 서브에이전트를 사용했는지까지는 추적하지 못합니다.

따라서 목적에 따라 접근 방식을 선택하면 됩니다.

| 접근 방식 | 장점 | 한계 |
|----------|------|------|
| Transcript 파싱 (이 글의 방식) | 스킬/에이전트 단위 상세 추적 | 직접 파싱 로직 구현 필요 |
| OpenTelemetry | 기존 모니터링 인프라 활용 | 세부 기능 사용 내역 추적 불가 |
| 공식 Analytics | 설정 없이 바로 사용 | 커스터마이징 제한 |

## Step 9. BigQuery 통합 데이터 허브 — 교차 분석의 힘

Claude Code 활용 데이터만으로도 유용하지만, 진짜 가치는 다른 데이터와 합칠 때 나옵니다. Jira, GitHub, 배포 로그, 장애 로그가 이미 BigQuery에 있다면, 지금까지 불가능했던 질문에 답할 수 있습니다.

### 9-1. 교차 분석 쿼리 예시

**Claude Code 활용도가 높은 개발자가 PR 머지 속도도 빠른가?**

```sql
SELECT
  u.user_email,
  u.quality_score,
  ROUND(AVG(pr.hours_to_merge), 1) AS avg_hours_to_merge,
  COUNT(pr.pr_id) AS total_prs
FROM (
  SELECT
    user_email,
    ROUND(SAFE_DIVIDE(
      SUM(skill_count + agent_count),
      SUM(total_tool_count)
    ) * 100, 1) AS quality_score
  FROM `your-project.claude_usage.sessions`
  WHERE timestamp >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 30 DAY)
  GROUP BY user_email
) u
JOIN `your-project.github.pull_requests` pr
  ON u.user_email = pr.author_email
WHERE pr.merged_at >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 30 DAY)
GROUP BY u.user_email, u.quality_score
ORDER BY u.quality_score DESC;
```

**스킬 활용 비율과 장애 발생률 사이에 상관관계가 있는가?**

```sql
SELECT
  u.user_email,
  u.advanced_usage_ratio,
  COUNT(i.incident_id) AS incidents_caused,
  COUNT(d.deploy_id) AS total_deploys
FROM (
  SELECT
    user_email,
    ROUND(SAFE_DIVIDE(
      SUM(skill_count + agent_count),
      SUM(total_tool_count)
    ) * 100, 1) AS advanced_usage_ratio
  FROM `your-project.claude_usage.sessions`
  WHERE timestamp >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 90 DAY)
  GROUP BY user_email
) u
LEFT JOIN `your-project.deploy.logs` d
  ON u.user_email = d.deployer_email
  AND d.deployed_at >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 90 DAY)
LEFT JOIN `your-project.incident.logs` i
  ON d.deploy_id = i.related_deploy_id
GROUP BY u.user_email, u.advanced_usage_ratio
ORDER BY u.advanced_usage_ratio DESC;
```

**Jira 티켓 처리 속도와 Claude Code 사용 패턴의 관계**

```sql
SELECT
  u.user_email,
  u.avg_daily_sessions,
  ROUND(AVG(
    TIMESTAMP_DIFF(j.resolved_at, j.created_at, HOUR)
  ), 1) AS avg_resolution_hours,
  COUNT(j.issue_key) AS tickets_resolved
FROM (
  SELECT
    user_email,
    COUNT(*) / COUNT(DISTINCT DATE(timestamp)) AS avg_daily_sessions
  FROM `your-project.claude_usage.sessions`
  WHERE timestamp >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 30 DAY)
  GROUP BY user_email
) u
JOIN `your-project.jira.issues` j
  ON u.user_email = j.assignee_email
WHERE j.resolved_at >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 30 DAY)
  AND j.issue_type = 'Story'
GROUP BY u.user_email, u.avg_daily_sessions
ORDER BY u.avg_daily_sessions DESC;
```

### 9-2. 통합 대시보드 뷰

자주 사용하는 교차 분석 결과를 뷰로 만들어두면 대시보드에서 바로 활용할 수 있습니다.

```sql
CREATE OR REPLACE VIEW `your-project.claude_usage.developer_productivity` AS
SELECT
  s.user_email,
  -- Claude Code 활용 지표
  COUNT(DISTINCT s.session_id) AS total_sessions,
  SUM(s.skill_count + s.agent_count) AS advanced_tool_uses,
  ROUND(SAFE_DIVIDE(
    SUM(s.skill_count + s.agent_count),
    SUM(s.total_tool_count)
  ) * 100, 1) AS quality_score,
  -- GitHub 생산성 지표
  COUNT(DISTINCT pr.pr_id) AS prs_merged,
  ROUND(AVG(pr.hours_to_merge), 1) AS avg_pr_merge_hours,
  -- Jira 처리 지표
  COUNT(DISTINCT j.issue_key) AS tickets_resolved,
  ROUND(AVG(TIMESTAMP_DIFF(j.resolved_at, j.created_at, HOUR)), 1) AS avg_ticket_hours,
  -- 안정성 지표
  COUNT(DISTINCT i.incident_id) AS incidents
FROM `your-project.claude_usage.sessions` s
LEFT JOIN `your-project.github.pull_requests` pr
  ON s.user_email = pr.author_email
  AND pr.merged_at >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 30 DAY)
LEFT JOIN `your-project.jira.issues` j
  ON s.user_email = j.assignee_email
  AND j.resolved_at >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 30 DAY)
LEFT JOIN `your-project.incident.logs` i
  ON s.user_email = i.responsible_email
  AND i.created_at >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 30 DAY)
WHERE s.timestamp >= TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 30 DAY)
GROUP BY s.user_email;
```

이 뷰 하나로 "Claude Code를 잘 활용하는 개발자가 실제로 더 생산적인가?"에 대한 데이터 기반 답을 얻을 수 있습니다. 단순히 AI 도구 활용을 독려하는 것보다, 이런 숫자가 훨씬 강력한 설득력을 갖습니다.

## 운영 팁

### 지속적 개선이 핵심

Dinii 팀은 대시보드를 주 5~6회 페이스로 개선했다고 합니다. BigQuery 스키마만 잘 잡아두면, 프론트엔드 변경은 Claude Code에게 프롬프트 한 번으로 빠르게 반영할 수 있습니다.

```
대시보드에 "이번 주 새로 사용된 스킬 TOP 5" 차트를 추가해줘.
기존 /api/dashboard/ 패턴을 따르고, Recharts BarChart를 사용해줘.
```

이런 식으로 "**떠오를 때 바로 만드는**" 사이클을 돌릴 수 있는 게 Claude Code로 대시보드를 운영하는 가장 큰 장점입니다.

### 프라이버시 고려

업무 외 프로젝트나 개인 작업이 수집되지 않도록, 디렉토리 필터링을 반드시 적용하세요. 앞서 수집 스크립트에 주석 처리한 부분을 활성화하면 됩니다.

```bash
# 회사 프로젝트 디렉토리에서만 수집
if [[ "$CWD" != *"your-company"* ]]; then
  exit 0
fi
```

### 데이터 활용 — 숫자가 행동을 바꾼다

대시보드를 도입한 후 Dinii 팀에서는 서브에이전트 사용량이 1주일 만에 약 1.5배 증가했다고 합니다. 다른 사람의 활용 패턴이 보이면 "나도 한번 써봐야겠다"는 자연스러운 동기 부여가 됩니다. 랭킹 상위 사용자의 활용법이 데이터로 보이기 때문에, "효율이 올라간다"는 말에 설득력이 더해집니다.

## 마무리

정리하면 전체 구축 과정은 다음과 같습니다.

1. Claude Code Transcript의 JSONL 구조를 이해한다
2. Stop/PostToolUse Hook으로 데이터를 수집하는 스크립트를 작성한다
3. Plugin으로 패키징하여 GitHub를 통해 팀에 배포한다
4. Go Lambda + API Gateway로 고속 수집 레이어를 구성한다
5. BigQuery에 데이터를 적재하고, Jira/GitHub/배포/장애 로그와 교차 분석한다
6. 대시보드 프론트엔드는 Claude Code에게 맡긴다
7. 공식 Analytics와 병행하여 양적/질적 지표를 모두 확보한다

"보이지 않으면 개선할 수 없다"는 말처럼, AI 도구의 도입은 시작일 뿐입니다. 계측하고, 가시화하고, 팀 전체로 학습을 확산하는 것까지 이어가야 진짜 효과를 볼 수 있습니다.

대시보드 하나로 팀의 AI 활용 문화가 달라질 수 있습니다. 오늘 한번 만들어보세요.

---

## 참고 자료

- [Dinii — 「全エンジニアが Claude Code を 100% 活用する」を目指してダッシュボードを作った](https://zenn.dev/dinii/articles/28c8fcd041837d)
- [Claude Code 공식 문서 — Hooks Reference](https://code.claude.com/docs/en/hooks)
- [Claude Code 공식 문서 — 팀 사용량을 분석으로 추적하기](https://code.claude.com/docs/ko/analytics)
- [팀을 위한 Claude Code 모니터링 방법 4가지 — InfoGrab](https://insight.infograb.net/blog/2026/01/07/claudecode-monitoring/)
- [Claude Code Hooks Guide 2026 — Serenities AI](https://serenitiesai.com/articles/claude-code-hooks-guide-2026)
- [How to Build Claude Code Plugins — DataCamp](https://www.datacamp.com/tutorial/how-to-build-claude-code-plugins)
- [simonw/claude-code-transcripts — GitHub](https://github.com/simonw/claude-code-transcripts)
