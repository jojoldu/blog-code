# Claude Code 완전 설정 가이드 2026

Claude Code를 설치하고 대충 쓰고 있지 않은가?

2026년의 Claude Code는 Hooks, Skills, MCP, Rules, Auto Memory 등 기능이 폭발적으로 늘었다. 하지만 이걸 전부 제대로 설정해서 쓰고 있는 개발자는 거의 없다.

이 글에서는 Claude Code의 7개 설정 레이어를 한국어로 정리한다. 각 레이어가 무엇이고, 왜 필요하며, 실무에서 어떻게 설정해야 하는지를 모두 다룬다.

## 7개 설정 레이어 한눈에 보기

Claude Code의 설정은 7개 레이어로 구성된다.

| 레이어 | 역할 |
|---|---|
| 1. CLAUDE.md | Claude에게 전달하는 프로젝트 지시서 (팀 공유 / 개인용) |
| 2. Auto Memory | Claude가 자동으로 학습하는 메모 |
| 3. .claude/rules/ | 모듈형 규칙 파일 |
| 4. settings.json | 권한 · 허용 도구 · 환경 설정 |
| 5. Hooks | 라이프사이클 이벤트 자동화 (17개 이벤트) |
| 6. Skills | 커스텀 슬래시 커맨드 |
| 7. MCP | 외부 도구 연동 (GitHub, DB, Sentry 등) |

이 전부를 올바르게 설정해야 비로소 '실무 레벨'이라 할 수 있다.

## 디렉토리 구조 전체

먼저 완성형 디렉토리 구조를 보자.

```
your-project/
├── CLAUDE.md                    # 프로젝트 지시서 (git 관리)
├── CLAUDE.local.md              # 개인용 지시서 (gitignore)
├── .mcp.json                    # 팀 공유 MCP 설정 (git 관리)
├── .claude/
│   ├── settings.json            # 팀 공유 설정 (git 관리)
│   ├── settings.local.json      # 개인 설정 (gitignore)
│   ├── rules/                   # 모듈형 규칙
│   │   ├── code-style.md
│   │   ├── testing.md
│   │   ├── security.md
│   │   └── frontend/
│   │       ├── react.md
│   │       └── styles.md
│   ├── skills/                  # 커스텀 스킬
│   │   ├── deploy/
│   │   │   └── SKILL.md
│   │   ├── review-pr/
│   │   │   └── SKILL.md
│   │   └── fix-issue/
│   │       └── SKILL.md
│   ├── agents/                  # 커스텀 서브에이전트
│   │   └── security-reviewer/
│   │       └── AGENT.md
│   └── hooks/                   # Hook 스크립트
│       ├── lint-on-save.sh
│       ├── block-secrets.sh
│       └── notify-slack.sh
│
├── ~/.claude/                   # 유저 글로벌 설정
│   ├── CLAUDE.md                # 전체 프로젝트 공통 개인 지시
│   ├── settings.json            # 글로벌 설정
│   ├── settings.local.json      # 로컬 전용
│   ├── rules/                   # 개인 규칙
│   │   └── preferences.md
│   └── skills/                  # 개인 스킬
│       └── explain-code/
│           └── SKILL.md
```

핵심 포인트

- **git으로 관리하는 것**: `CLAUDE.md`, `.claude/settings.json`, `.mcp.json`, `.claude/rules/`
- **gitignore에 넣을 것**: `CLAUDE.local.md`, `.claude/settings.local.json`

팀원 전체가 공유해야 하는 설정과 개인 설정을 명확히 분리하는 것이 첫 번째 원칙이다.

## 1. CLAUDE.md — Claude에게 전달하는 지시서

CLAUDE.md는 Claude의 뇌에 설치하는 프로젝트의 DNA다.

### 메모리 계층 (우선순위순)

| 우선순위 | 이름 | 경로 | 용도 |
|---|---|---|---|
| 최고 | 관리 정책 | /Library/Application Support/ClaudeCode/CLAUDE.md | 조직 전체 규칙 |
| 높음 | 프로젝트 | ./CLAUDE.md | 팀 공유 지시 |
| 높음 | 규칙 | .claude/rules/*.md | 모듈형 지시 |
| 중간 | 유저 | ~/.claude/CLAUDE.md | 개인 글로벌 설정 |
| 낮음 | 로컬 | ./CLAUDE.local.md | 개인 프로젝트 고유 설정 |
| 자동 | Auto Memory | ~/.claude/projects/\<project\>/memory/ | Claude 자동 학습 메모 |

### CLAUDE.md의 황금률: "사람만 아는 것"을 적어라

**가장 흔한 실수**: 소스 코드에서 읽어낼 수 있는 정보를 CLAUDE.md에 적는 것이다.

Claude는 `package.json`을 읽으면 빌드 명령어를 안다. `tsconfig.json`을 읽으면 TypeScript 설정을 안다. 디렉토리를 탐색하면 아키텍처를 안다. `.eslintrc`를 읽으면 코딩 규약을 안다.

이런 걸 CLAUDE.md에 적으면 토큰 낭비다.

**CLAUDE.md에는 Claude가 소스 코드를 읽어도 절대 모르는 정보만 적어야 한다.**

| 적어야 할 것 (사람만 아는 것) | 적지 말아야 할 것 (코드에서 읽을 수 있는 것) |
|---|---|
| 왜 이 아키텍처를 선택했는지 | packages/ 하위 디렉토리 목록 |
| 과거 인시던트에서 배운 금지 사항 | `pnpm build`로 빌드할 수 있다는 것 |
| 비즈니스 제약과 우선순위 | TypeScript를 쓴다는 것 |
| 배포 환경 정보 | ESLint 규칙 |
| 팀 간 약속 · 워크플로 | 의존 패키지 목록 |
| "이 코드는 건드리지 마" 같은 암묵지 | 테스트 프레임워크가 무엇인지 |

### 실무용 CLAUDE.md 템플릿

```markdown
# 프로젝트 의사결정

## 왜 이 구성인가
- 모노레포 채택 이유: 프론트·백엔드 간 타입 공유로 버그를 주 3건→0건으로 줄임
- Hono를 선택한 이유: Cloudflare Workers에서 콜드스타트 50ms 이하
- PlanetScale을 쓰는 이유: 프로덕션 DB 스키마 변경을 브랜치로 안전하게 관리

## 절대 해서는 안 되는 것
- `packages/shared/src/legacy/`는 구 API 호환 레이어. 리팩토링하고 싶겠지만 절대 건드리지 마 (고객사 3곳이 의존 중, 2026년 Q3 폐기 예정)
- Stripe webhook handler는 멱등성을 깨뜨리면 이중 과금 발생. 과거에 $12,000 사고가 있었음
- `users` 테이블의 `email` 컬럼에 UNIQUE 제약이 없음. 역사적 이유로 중복 존재. 앱 레이어에서 검증 중

## 배포와 환경
- staging: Cloudflare Workers (`wrangler deploy --env staging`)
- production: 프로덕션 배포는 GitHub Actions만 사용. 수동 배포 금지
- DB migration: `pnpm db:migrate`는 반드시 staging에서 먼저 실행 후 확인

## 팀 워크플로
- PR은 반드시 1인 이상 리뷰를 거칠 것
- `feat/` 브랜치는 squash merge, `fix/` 브랜치는 일반 merge
- 금요일 15시 이후 프로덕션 배포 금지 (주말 대응 방지)

## 비즈니스 컨텍스트
- 엔터프라이즈 고객은 응답 200ms 이내 SLA
- GDPR 대응 필수. 사용자 데이터는 반드시 논리 삭제 (물리 삭제 금지)
- 월간 리포트 생성은 매월 1일 심야 cron 실행. 이 날은 DB 부하가 높음
```

이 템플릿의 포인트:

- `pnpm install`이나 `pnpm test`는 적지 않았다 → Claude는 `package.json`을 읽으면 안다
- 디렉토리 구조는 적지 않았다 → Claude는 `ls`하면 안다
- 대신 **"왜"**, **"하지 마"**, **"과거 사고"** 를 적었다 → 이건 코드에 없다

### @import로 외부 파일 참조

CLAUDE.md에서 다른 파일을 임포트할 수 있다.

```markdown
프로젝트 개요는 @README.md 를 참조.
사용 가능한 명령어는 @package.json 을 확인.
Git 운영 규칙은 @docs/git-workflow.md 을 따를 것.

# 개인 설정
- @~/.claude/my-project-instructions.md
```

import 주의사항:

- 상대 경로는 CLAUDE.md가 있는 디렉토리 기준으로 해석된다
- 최대 5단계 재귀 import가 가능하다
- 코드블록 내의 `@`는 import로 해석되지 않는다
- 처음 사용 시 import 허용 다이얼로그가 표시된다

### .claude/rules/ — 조건부 규칙

특정 파일 패턴에 대해서만 적용되는 규칙을 작성할 수 있다.

```markdown
# .claude/rules/api-rules.md
---
paths:
  - "packages/backend/src/routes/**/*.ts"
---

# API 개발 규칙

- 모든 엔드포인트에 zod 검증을 넣을 것
- 에러 응답은 RFC 7807 형식으로 할 것
- 응답에는 페이지네이션 정보를 포함할 것
- Rate Limit 헤더를 반환할 것
```

```markdown
# .claude/rules/react-rules.md
---
paths:
  - "packages/frontend/src/**/*.{tsx,ts}"
---

# React 개발 규칙

- Server Components를 기본으로 사용할 것
- "use client"는 최소화할 것
- Suspense로 로딩 상태를 관리할 것
- key prop에 index를 사용하지 말 것
```

이렇게 하면 해당 경로의 파일을 다룰 때만 규칙이 활성화된다. CLAUDE.md에 모든 규칙을 몰아넣는 것보다 훨씬 효율적이다.

## 2. settings.json — 권한과 설정

### 설정 파일 우선순위

```
관리 정책 (최고)
  ↓
CLI 플래그 (--disallowedTools 등)
  ↓
.claude/settings.local.json (프로젝트 로컬)
  ↓
.claude/settings.json (프로젝트 공유)
  ↓
~/.claude/settings.local.json (유저 로컬)
  ↓
~/.claude/settings.json (유저 공유 · 최저)
```

위에서 아래로 우선순위가 높다. 즉, 프로젝트 레벨 설정이 유저 레벨 설정을 덮어쓴다.

### 실무용 .claude/settings.json (팀 공유)

```json
{
  "permissions": {
    "defaultMode": "default",
    "allow": [
      "Read",
      "Glob",
      "Grep",
      "Bash(pnpm run *)",
      "Bash(pnpm test *)",
      "Bash(pnpm build *)",
      "Bash(pnpm lint *)",
      "Bash(pnpm typecheck)",
      "Bash(git status)",
      "Bash(git diff *)",
      "Bash(git log *)",
      "Bash(git branch *)",
      "Bash(git checkout *)",
      "Bash(git add *)",
      "Bash(git commit *)",
      "Bash(gh pr *)",
      "Bash(gh issue *)",
      "Bash(npx prisma *)",
      "Bash(* --version)",
      "Bash(* --help)",
      "Edit(/packages/**)",
      "Write(/packages/**)"
    ],
    "deny": [
      "Bash(git push --force *)",
      "Bash(git reset --hard *)",
      "Bash(rm -rf *)",
      "Bash(curl *)",
      "Bash(wget *)",
      "Read(.env*)",
      "Read(**/*.pem)",
      "Read(**/*secret*)",
      "Edit(.env*)",
      "Edit(/.github/workflows/**)"
    ]
  },
  "env": {
    "MAX_MCP_OUTPUT_TOKENS": "50000"
  }
}
```

설계 원칙은 **최소 권한**이다. `deny`를 먼저 설계하고, 필요한 것만 `allow`에 추가한다.

### 개인용 .claude/settings.local.json

```json
{
  "permissions": {
    "allow": [
      "Bash(docker *)",
      "Bash(docker-compose *)"
    ]
  },
  "hooks": {
    "PostToolUse": [
      {
        "matcher": "Edit|Write",
        "hooks": [
          {
            "type": "command",
            "command": "\"$CLAUDE_PROJECT_DIR\"/.claude/hooks/lint-on-save.sh"
          }
        ]
      }
    ]
  }
}
```

개인용 설정은 git에 올리지 않으므로 각자의 환경에 맞게 자유롭게 설정할 수 있다.

### 커밋·PR 귀속 표시(attribution) 제어

Claude Code는 기본적으로 커밋 메시지에 `Co-Authored-By: Claude`를 추가한다. 불필요하다면 `attribution` 설정으로 없앨 수 있다.

```json
{
  "attribution": {
    "commit": "",
    "pr": ""
  }
}
```

| 설정 | 기본값 | 설명 |
|---|---|---|
| attribution.commit | Co-Authored-By 트레일러 포함 | 커밋 메시지에 대한 귀속 표시 |
| attribution.pr | 푸터에 Claude Code 표기 | PR 설명에 대한 귀속 표시 |

빈 문자열 `""`로 완전히 숨길 수 있다. 커스텀 메시지도 가능하다.

```json
{
  "attribution": {
    "commit": "Generated with Claude Code"
  }
}
```

`~/.claude/settings.json`에 넣으면 전체 프로젝트에서 적용된다.

### 권한 패턴 상세 레퍼런스

| 타입 | 예시 | 설명 |
|---|---|---|
| Bash | `Bash(npm run *)` | npm run으로 시작하는 모든 명령어 |
| Bash | `Bash(git commit *)` | git commit 허용 |
| Read | `Read(.env*)` | .env 파일 읽기 |
| Read | `Read(~/.ssh/*)` | 홈의 .ssh 하위 |
| Read | `Read(//etc/passwd)` | 절대 경로 (//로 시작) |
| Edit | `Edit(/src/**)` | 프로젝트 루트 상대 경로 |
| Edit | `Edit(**/*.test.ts)` | 전체 테스트 파일 |
| WebFetch | `WebFetch(domain:github.com)` | 도메인 지정 |
| MCP | `mcp__github__*` | GitHub MCP 전체 도구 |
| Task | `Task(Explore)` | Explore 서브에이전트 |
| Skill | `Skill(deploy *)` | deploy 스킬 |

**중요**: `*` 앞에 스페이스가 있는지에 따라 동작이 달라진다.

- `Bash(ls *)` → `ls -la`에 매칭, `lsof`에는 매칭 안 됨 (단어 경계 있음)
- `Bash(ls*)` → `ls -la`에도 `lsof`에도 매칭 (단어 경계 없음)

## 3. Hooks — 17개 라이프사이클 이벤트

Hooks는 CLAUDE.md의 "부탁"과 다르다. **확실하게 실행된다.**

CLAUDE.md에 "rm -rf를 쓰지 마"라고 적어도 Claude가 무시할 수 있다. 하지만 Hook으로 막으면 물리적으로 실행이 차단된다.

### 전체 이벤트 목록

| 이벤트 | 시점 | PreToolUse처럼 차단 가능 |
|---|---|---|
| SessionStart | 세션 시작·재개 | - |
| UserPromptSubmit | 프롬프트 전송 직후 | - |
| PreToolUse | 도구 실행 전 | Yes |
| PermissionRequest | 권한 다이얼로그 표시 시 | - |
| PostToolUse | 도구 성공 후 | - |
| PostToolUseFailure | 도구 실패 후 | - |
| Notification | 알림 전송 시 | - |
| SubagentStart | 서브에이전트 시작 시 | - |
| SubagentStop | 서브에이전트 종료 시 | - |
| Stop | Claude 응답 완료 시 | - |
| TeammateIdle | 팀메이트가 idle 상태 시 | - |
| TaskCompleted | 태스크 완료 시 | - |
| ConfigChange | 설정 파일 변경 시 | - |
| WorktreeCreate | worktree 생성 시 | - |
| WorktreeRemove | worktree 삭제 시 | - |
| PreCompact | 컨텍스트 압축 전 | - |
| SessionEnd | 세션 종료 시 | - |

### 핸들러의 4가지 유형

```
command  → 셸 스크립트 실행
http     → HTTP POST 요청 전송
prompt   → LLM에게 판단을 맡김 (yes/no)
agent    → 서브에이전트로 검증
```

### 실무 Hooks 설정 예시

```json
{
  "hooks": {
    "PreToolUse": [
      {
        "matcher": "Bash",
        "hooks": [
          {
            "type": "command",
            "command": "\"$CLAUDE_PROJECT_DIR\"/.claude/hooks/block-secrets.sh",
            "statusMessage": "보안 체크 중..."
          }
        ]
      }
    ],
    "PostToolUse": [
      {
        "matcher": "Edit|Write",
        "hooks": [
          {
            "type": "command",
            "command": "\"$CLAUDE_PROJECT_DIR\"/.claude/hooks/lint-on-save.sh",
            "timeout": 30
          }
        ]
      }
    ],
    "Stop": [
      {
        "hooks": [
          {
            "type": "command",
            "command": "\"$CLAUDE_PROJECT_DIR\"/.claude/hooks/notify-slack.sh",
            "async": true
          }
        ]
      }
    ]
  }
}
```

### 실용 Hook 스크립트 모음

**위험 명령어 차단 (PreToolUse)**

```bash
#!/bin/bash
# .claude/hooks/block-secrets.sh
INPUT=$(cat)
COMMAND=$(echo "$INPUT" | jq -r '.tool_input.command // empty')

# 비밀 정보를 포함하는 명령어 차단
if echo "$COMMAND" | grep -qE '(password|secret|token|api.?key)'; then
  jq -n '{
    hookSpecificOutput: {
      hookEventName: "PreToolUse",
      permissionDecision: "deny",
      permissionDecisionReason: "비밀 정보를 포함할 수 있는 명령어가 차단되었습니다"
    }
  }'
  exit 0
fi

# rm -rf 차단
if echo "$COMMAND" | grep -qE 'rm\s+-rf'; then
  jq -n '{
    hookSpecificOutput: {
      hookEventName: "PreToolUse",
      permissionDecision: "deny",
      permissionDecisionReason: "rm -rf는 금지되어 있습니다"
    }
  }'
  exit 0
fi

exit 0
```

**파일 저장 후 자동 린트 (PostToolUse)**

```bash
#!/bin/bash
# .claude/hooks/lint-on-save.sh
INPUT=$(cat)
FILE_PATH=$(echo "$INPUT" | jq -r '.tool_input.file_path // .tool_result.filePath // empty')

if [ -z "$FILE_PATH" ]; then
  exit 0
fi

# TypeScript 파일만 린트
if [[ "$FILE_PATH" == *.ts || "$FILE_PATH" == *.tsx ]]; then
  RESULT=$(npx eslint --fix "$FILE_PATH" 2>&1)
  if [ $? -ne 0 ]; then
    jq -n --arg msg "$RESULT" '{
      hookSpecificOutput: {
        hookEventName: "PostToolUse"
      },
      transcript: ("ESLint 에러:\n" + $msg)
    }'
  fi
fi

exit 0
```

**Slack 알림 (Stop — 비동기)**

```bash
#!/bin/bash
# .claude/hooks/notify-slack.sh
INPUT=$(cat)
STOP_REASON=$(echo "$INPUT" | jq -r '.stop_reason // "unknown"')

if [ "$STOP_REASON" = "end_turn" ]; then
  curl -s -X POST "$SLACK_WEBHOOK_URL" \
    -H 'Content-Type: application/json' \
    -d "{\"text\": \"Claude Code 태스크가 완료되었습니다\"}" \
    > /dev/null 2>&1
fi
```

### Prompt Hook (LLM 판단형)

셸 스크립트 대신 LLM에게 판단을 맡길 수도 있다.

```json
{
  "hooks": {
    "PreToolUse": [
      {
        "matcher": "Bash",
        "hooks": [
          {
            "type": "prompt",
            "prompt": "다음 Bash 명령이 프로덕션 환경에 영향을 줄 가능성이 있는지 판단하세요. 명령어: $ARGUMENTS. 프로덕션 DB 접속, 프로덕션 서버 배포, 환경 변수 변경을 포함하면 no로 응답하세요.",
            "timeout": 15
          }
        ]
      }
    ]
  }
}
```

## 4. Skills — 커스텀 슬래시 커맨드

Skills는 자주 반복하는 워크플로를 `/명령어`로 만들어주는 기능이다.

### Skill 프론트매터 전체 필드

| 필드 | 필수 | 설명 |
|---|---|---|
| name | No | /커맨드명 (생략 시 디렉토리명) |
| description | 권장 | 용도 설명 (Claude 자동 실행 판단 근거) |
| argument-hint | No | 인수 힌트 (예: [issue-number]) |
| disable-model-invocation | No | true로 Claude 자동 실행 비활성화 |
| user-invocable | No | false로 / 메뉴에서 숨김 |
| allowed-tools | No | 이 Skill에서 허용할 도구 |
| model | No | 사용 모델 지정 |
| context | No | fork로 서브에이전트 실행 |
| agent | No | context: fork 시의 에이전트 타입 |
| hooks | No | Skill 고유 Hooks |

### 실용 스킬 예시

**PR 리뷰 스킬**

```markdown
# .claude/skills/review-pr/SKILL.md
---
name: review-pr
description: PR을 리뷰하고 개선 제안을 한다
context: fork
agent: Explore
allowed-tools: Bash(gh *)
argument-hint: "[PR번호]"
disable-model-invocation: true
---

## PR 컨텍스트
- PR diff: !`gh pr diff $ARGUMENTS`
- PR 코멘트: !`gh pr view $ARGUMENTS --comments`
- 변경 파일: !`gh pr diff $ARGUMENTS --name-only`

## 리뷰 절차
1. 변경 의도를 이해한다
2. 코드 품질을 체크한다 (타입 안전성, 에러 핸들링)
3. 보안상 우려가 없는지 확인한다
4. 성능 영향을 평가한다
5. 테스트 커버리지를 확인한다

리뷰 결과를 항목별로 정리하고 중요도 (Critical/Major/Minor)를 부여한다.
```

**Issue 수정 스킬**

```markdown
# .claude/skills/fix-issue/SKILL.md
---
name: fix-issue
description: GitHub Issue를 수정한다
disable-model-invocation: true
argument-hint: "[issue번호]"
---

GitHub issue #$ARGUMENTS 를 수정한다.

1. `gh issue view $ARGUMENTS`로 Issue 내용 확인
2. 관련 코드 특정
3. 수정 구현
4. 테스트 추가
5. `git commit -m "fix: #$ARGUMENTS 수정"`
```

**배포 스킬 (서브에이전트 실행)**

```markdown
# .claude/skills/deploy/SKILL.md
---
name: deploy
description: 스테이징 환경에 배포한다
disable-model-invocation: true
context: fork
allowed-tools: Bash(pnpm *), Bash(git *), Bash(gh *)
---

$ARGUMENTS 를 스테이징에 배포한다:

1. 테스트 스위트 실행
2. 빌드 실행
3. 배포 명령 실행
4. 배포 성공 확인
5. Slack에 알림
```

### 동적 컨텍스트 주입

`` !`명령어` `` 구문으로 셸 명령의 결과를 Skill에 주입할 수 있다.

```markdown
---
name: status
description: 프로젝트 상태 요약
context: fork
agent: Explore
---

## 현재 상태
- 브랜치: !`git branch --show-current`
- 미커밋: !`git status --short`
- 최신 커밋: !`git log --oneline -5`
- 테스트 결과: !`pnpm test --silent 2>&1 | tail -5`
- 타입 체크: !`pnpm typecheck 2>&1 | tail -3`

위 정보를 바탕으로 프로젝트 상태를 요약 보고한다.
```

## 5. MCP — 외부 도구 연동

MCP(Model Context Protocol)를 통해 Claude Code에 외부 도구를 연결할 수 있다.

### MCP 서버 추가

```bash
# HTTP (권장)
claude mcp add --transport http github https://api.githubcopilot.com/mcp/

# SSE (비권장)
claude mcp add --transport sse sentry https://mcp.sentry.dev/sse

# stdio (로컬 실행)
claude mcp add --transport stdio --env AIRTABLE_API_KEY=$KEY airtable \
  -- npx -y airtable-mcp-server
```

### MCP 스코프

| 스코프 | 설정 파일 | 적용 범위 |
|---|---|---|
| local (기본값) | ~/.claude.json | 나만 · 이 프로젝트 |
| project | .mcp.json | 팀 전체 (git 관리) |
| user | ~/.claude.json | 나만 · 전체 프로젝트 |

### 팀 공유 MCP 설정 (.mcp.json)

```json
{
  "mcpServers": {
    "github": {
      "type": "http",
      "url": "https://api.githubcopilot.com/mcp/"
    },
    "sentry": {
      "type": "http",
      "url": "https://mcp.sentry.dev/mcp"
    },
    "database": {
      "type": "stdio",
      "command": "npx",
      "args": ["-y", "@bytebase/dbhub", "--dsn", "${DB_DSN}"],
      "env": {
        "DB_DSN": "${DB_DSN:-postgresql://readonly:pass@localhost:5432/dev}"
      }
    }
  }
}
```

인증 정보는 반드시 환경 변수로 관리한다. `.mcp.json`에 직접 시크릿을 넣지 않는다.

### MCP Tool Search

MCP 도구가 너무 많아서 컨텍스트를 압박하는 경우, Tool Search를 사용할 수 있다.

```bash
# 임계값을 5%로 변경
ENABLE_TOOL_SEARCH=auto:5 claude

# 항상 활성화
ENABLE_TOOL_SEARCH=true claude

# 비활성화
ENABLE_TOOL_SEARCH=false claude
```

## 6. Auto Memory — Claude의 자동 학습

### 구조

```
~/.claude/projects/<project>/memory/
├── MEMORY.md          # 인덱스 (최초 200줄이 로드)
├── debugging.md       # 디버깅 패턴
├── api-conventions.md # API 설계 결정사항
└── ...                # Claude가 자동 생성
```

MEMORY.md는 **200줄까지만** 로드된다. 그 이상의 정보는 토픽별 파일로 분할하고, MEMORY.md에서 링크하면 된다. Claude는 필요에 따라 토픽 파일을 읽어온다.

### 명시적으로 기억시키기

```
> pnpm을 써. npm은 쓰지 마. 이걸 기억해둬
> API 테스트에 로컬 Redis가 필요하다는 걸 메모리에 저장해
```

이렇게 대화 중에 직접 기억을 지시할 수 있다.

### 제어 방법

```json
// .claude/settings.json에서 프로젝트 단위로 비활성화
{ "autoMemoryEnabled": false }
```

```bash
# 환경 변수로 강제 제어 (settings.json보다 우선)
CLAUDE_CODE_DISABLE_AUTO_MEMORY=1 claude  # 강제 OFF
CLAUDE_CODE_DISABLE_AUTO_MEMORY=0 claude  # 강제 ON
```

## 7. 실무 운영 체크리스트

모든 설정이 갖추어졌는지 확인하자.

### 보안

- [ ] `.env*` 파일의 Read/Edit를 deny로 설정
- [ ] `rm -rf`를 PreToolUse Hook으로 차단
- [ ] `git push --force`를 deny로 설정
- [ ] 비밀키 (`*.pem`) 읽기를 deny
- [ ] `curl`/`wget`을 deny하고 WebFetch로 제한
- [ ] CI/CD 워크플로 편집을 deny

### 개발 효율

- [ ] 자주 쓰는 빌드/테스트 명령어를 allow에 설정
- [ ] PostToolUse로 자동 린트 설정
- [ ] PR 리뷰 Skill 생성
- [ ] Issue 수정 Skill 생성
- [ ] MCP로 GitHub 연동 설정

### 팀 표준화

- [ ] `.claude/settings.json`을 git 관리
- [ ] `.mcp.json`을 git 관리
- [ ] `.claude/rules/`로 코딩 규약 관리
- [ ] CLAUDE.md에는 "사람만 아는 것"을 기재
- [ ] `.gitignore`에 이하 추가:

```
CLAUDE.local.md
.claude/settings.local.json
```

## 자주 하는 실수와 대응

### 실수 1: 권한이 너무 넓다

```json
// NG: 위험
{ "permissions": { "allow": ["Bash"] } }

// OK: 필요한 명령만 허용
{
  "permissions": {
    "allow": [
      "Bash(pnpm *)",
      "Bash(git status)",
      "Bash(git diff *)"
    ]
  }
}
```

### 실수 2: CLAUDE.md에 소스 코드에서 읽을 수 있는 정보를 적는다

```markdown
<!-- NG: Claude는 package.json과 디렉토리 구조를 읽으면 안다 -->
# 프로젝트 개요
TypeScript + React + Node.js의 SaaS 앱.
packages/frontend: Next.js 15
packages/backend: Hono

## 명령어
- `pnpm install` - 의존성 설치
- `pnpm build` - 빌드
- `pnpm test` - 테스트 실행
```

```markdown
<!-- OK: 사람 머릿속에만 있는 "왜"와 "하지 마"를 적는다 -->
# 의사결정과 제약
- legacy/는 2026년 Q3 폐기 예정. 건드리지 말 것
- Stripe webhook 멱등성을 깨뜨리면 이중 과금 (과거 $12K 사고)
- 금요일 15시 이후 프로덕션 배포 금지
```

### 실수 3: Hook의 timeout을 설정하지 않는다

```json
// NG: 타임아웃 없음 (기본값 600초)
{
  "type": "command",
  "command": "npm run lint"
}

// OK: 적절한 타임아웃
{
  "type": "command",
  "command": "npm run lint",
  "timeout": 30
}
```

### 실수 4: Skill에서 disable-model-invocation을 빼먹는다

```markdown
# NG: Claude가 임의로 배포 스킬을 실행할 수 있음
---
name: deploy
description: Deploy to production
---

# OK: 수동으로만 실행 가능
---
name: deploy
description: Deploy to production
disable-model-invocation: true
---
```

부작용이 있는 Skill(배포, 데이터 변경 등)에는 반드시 `disable-model-invocation: true`를 설정해야 한다.

## 정리

Claude Code 실무 운영에 필요한 설정을 전부 정리했다.

- **CLAUDE.md** → "사람만 아는 것"을 적는다. 빌드 명령어나 디렉토리 구성이 아니라, 의사결정 이유·금지사항·비즈니스 제약을
- **settings.json** → 권한은 최소 권한 원칙으로. `deny` 우선 설계
- **Hooks** → 17개 이벤트를 활용. 보안 체크와 린트 자동화는 필수
- **Skills** → 자주 쓰는 워크플로를 슬래시 커맨드화. 부작용이 있는 것은 `disable-model-invocation: true`
- **MCP** → 팀 공유는 `.mcp.json`으로 관리. 인증 정보는 환경 변수로
- **Auto Memory** → 200줄 제한을 의식. 토픽별로 분할
- **Attribution** → `attribution.commit`과 `attribution.pr`로 귀속 표시 제어

이것들을 전부 설정해야 비로소 Claude Code는 "장난감"에서 "실무 도구"가 된다.
