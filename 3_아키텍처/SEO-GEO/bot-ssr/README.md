# Bot 전용 SSR 서버

# Bot SSR 아키텍처 설계

## 1. 핵심 컨셉: 관심사의 완전한 분리

```
┌─────────────────────────────────────────────────────┐
│                    CloudFront / Nginx                │
│              (User-Agent 기반 트래픽 분기)              │
└──────────┬──────────────────────┬────────────────────┘
           │                      │
     일반 유저 요청             봇 요청
           │               (Googlebot, 카카오봇,
           ▼                ChatGPT, Slack 등)
┌──────────────────┐          │
│   FE App (Next)  │          ▼
│                  │  ┌──────────────────┐
│ - 유저 경험 최적화  │  │  Go Bot SSR      │
│ - DX 우선         │  │                  │
│ - CSR/SSR 자유선택 │  │ - SEO/GEO 최적화  │
│ - 개인화, 실시간성  │  │ - 경량 HTML 생성   │
│                  │  │ - API → 템플릿     │
└──────────────────┘  └──────────────────┘
```

### 분리를 통해 얻는 것

**FE App (유저 전용)**
- SSR을 SEO 때문에 억지로 유지할 필요 없음
- 페이지 특성에 따라 CSR/SSR/SSG를 순수하게 유저 경험 기준으로 선택
- 리뷰, 커리큘럼 등을 lazy load 해서 체감 속도 향상 가능
- CDN 캐싱을 위해 사용성을 해치는 CSR 패턴을 가져갈 필요 없음
- 유저에게 딱 필요한 데이터만 가공해서 노출

**Go Bot SSR (봇 전용)**
- JSON-LD 구조화 데이터, FAQ Schema, Rating Schema, BreadcrumbList 등을 풍부하게 포함
- 유저 페이지에서는 번들 크기만 키우는 SEO 마크업을 부담 없이 추가
- GEO(Generative Engine Optimization): AI 크롤러에 최적화된 시맨틱 HTML 별도 설계
- SNS 공유 미리보기(카카오톡, 페이스북, 슬랙 등)도 봇 크롤러가 처리하므로 이 서버에서 제공
- 성능에만 집중 (목표: 100ms 이내 응답)

### SNS 공유가 문제없는 이유

SNS에서 URL 미리보기가 생성되는 과정은 다음과 같다.

```
사용자가 카카오톡에 inflearn.com/course/123 공유
    │
    ▼
카카오톡 서버의 크롤러 (User-Agent: kakaotalk-scrap)가 URL 요청
    │
    ▼
CloudFront/Nginx가 봇으로 감지 → Go Bot SSR로 라우팅
    │
    ▼
Bot SSR이 OG 메타 태그 포함된 경량 HTML 응답
    │
    ▼
카카오톡이 og:title, og:description, og:image 파싱 → 미리보기 카드 렌더링
```

SNS 미리보기를 만드는 주체가 **바로 봇**이다. 모든 주요 SNS 크롤러는 JavaScript를 실행하지 않고 초기 HTML의 `<meta>` 태그만 파싱하므로, Bot SSR 서버에서만 메타 태그를 관리하면 된다.

오히려 현재보다 나아지는 점이 있다. 지금은 200-500KB HTML에서 OG 태그를 찾아야 하지만, Bot SSR에서는 20-30KB 경량 HTML 상단에 메타 태그가 바로 있으므로 파싱이 빠르고 안정적이다. 또한 SNS별 최적화 메타 태그(Twitter Card large image, 카카오 커스텀 OG 등)를 FE 앱 번들 부담 없이 자유롭게 추가할 수 있다.

---

## 2. Go Bot SSR 서버 아키텍처

### 기술 스택

| 영역 | 선택 | 이유 |
|---|---|---|
| HTTP 라우터 | `Echo` 또는 `Chi` | Echo는 미들웨어 구조가 깔끔, Chi는 표준 라이브러리 호환성 높음 |
| 템플릿 엔진 | `html/template` (표준) | FE 개발자 진입 장벽 최소화, 순수 HTML 작성 가능 |
| 템플릿 임베딩 | `embed.FS` | 프로덕션: 바이너리 내장, 개발: `fsnotify` 핫 리로드 |
| 캐싱 | in-memory (ristretto) + CDN | 로컬 캐시로 API 호출 최소화, CDN으로 봇 응답 캐싱 |
| 배포 | 단일 Go 바이너리 | Docker 이미지 ~20MB, K8s 또는 Lambda 모두 가능 |

### 봇 감지 전략

Bot SSR이 정상 동작하려면 **모든 봇 트래픽이 정확하게 분기**되어야 한다.
특히 SNS 공유 미리보기가 깨지지 않으려면 각 SNS 크롤러의 User-Agent를 빠짐없이 감지해야 한다.

**주요 봇 User-Agent 목록**

| 분류 | 봇 이름 | User-Agent 키워드 |
|---|---|---|
| 검색엔진 | Googlebot | `Googlebot`, `Googlebot-Image` |
| 검색엔진 | Bingbot | `bingbot` |
| 검색엔진 | Naver | `Yeti`, `Naverbot` |
| SNS | 카카오톡 | `kakaotalk-scrap`, `kakao` |
| SNS | 페이스북 | `facebookexternalhit`, `Facebot` |
| SNS | 트위터/X | `Twitterbot` |
| SNS | 슬랙 | `Slackbot-LinkExpanding` |
| SNS | 디스코드 | `Discordbot` |
| SNS | 텔레그램 | `TelegramBot` |
| SNS | 라인 | `Line` |
| SNS | LinkedIn | `LinkedInBot` |
| AI 크롤러 | ChatGPT | `ChatGPT-User`, `GPTBot` |
| AI 크롤러 | Claude | `ClaudeBot`, `Claude-Web` |
| AI 크롤러 | Perplexity | `PerplexityBot` |

**구현 방식: 외부 봇 DB 활용**

직접 User-Agent 목록을 하드코딩하면 새로운 봇 추가 시마다 배포가 필요하다.
대신 `crawler-user-agents` 같은 오픈소스 봇 DB를 활용하되, SNS 봇은 반드시 포함되도록 커스텀 목록을 병합한다.

```go
// internal/middleware/bot_detect.go

// 1차: 오픈소스 봇 DB (주기적 업데이트)
// 2차: 커스텀 SNS 봇 목록 (반드시 포함 보장)
// 3차: 헤더 기반 추가 감지 (X-Purpose: preview 등)

func IsBotRequest(r *http.Request) bool {
    ua := r.UserAgent()

    // 커스텀 SNS 봇 (절대 누락 불가)
    for _, bot := range snsBots {
        if strings.Contains(strings.ToLower(ua), bot) {
            return true
        }
    }

    // 오픈소스 봇 DB
    if crawlerDB.IsCrawler(ua) {
        return true
    }

    // 헤더 기반 감지 (일부 프록시/프리렌더 서비스)
    if r.Header.Get("X-Purpose") == "preview" {
        return true
    }

    return false
}

var snsBots = []string{
    "kakaotalk-scrap", "kakao",
    "facebookexternalhit", "facebot",
    "twitterbot",
    "slackbot",
    "discordbot",
    "telegrambot",
    "linkedinbot",
    "line",
}
```

**운영 포인트: 봇 감지 모니터링**

봇 감지 미들웨어에서 분기 로그를 남기고, 알 수 없는 User-Agent가 Bot SSR이 아닌 FE로 갔을 때 SNS 미리보기가 깨지는 것을 사전에 감지할 수 있도록 한다.

```
[INFO] bot_detected: ua="kakaotalk-scrap/2.0" path="/course/123" → bot_ssr
[WARN] unknown_ua: ua="NewSNSBot/1.0" path="/course/456" → fe_app
```

`unknown_ua` 경고가 지속되면 해당 봇을 커스텀 목록에 추가하고 배포한다.

### 요청 처리 흐름

```
봇 요청 (GET /courses/123)
    │
    ▼
[1] 캐시 확인 (in-memory, TTL 기반)
    │
    ├─ HIT → 즉시 HTML 응답 (~1ms)
    │
    └─ MISS
        │
        ▼
[2] 인프런 공개 API 호출 (/api/v1/courses/123)
        │
        ▼
[3] API 응답 → Go 구조체 매핑
        │
        ▼
[4] html/template 렌더링
    ├── base.html (공통 레이아웃)
    ├── meta_tags.html (OG, Twitter Card)
    ├── structured_data.html (JSON-LD)
    └── course_detail.html (페이지별 본문)
        │
        ▼
[5] 캐시 저장 + HTML 응답 (~100ms)
```

---

## 3. 프로젝트 구조 및 역할 분리

```
bot-ssr/
│
├── cmd/server/
│   └── main.go                    # [DevOps] 서버 진입점, 설정 로딩
│
├── internal/
│   ├── server/
│   │   └── server.go              # [DevOps] Echo/Chi 서버 설정, 라우팅
│   │
│   ├── handler/                   # [DevOps] 핸들러 (라우트 → 템플릿 연결)
│   │   ├── course.go
│   │   ├── challenge.go
│   │   ├── mentoring.go
│   │   └── clip.go
│   │
│   ├── api/                       # [DevOps + BE] API 클라이언트
│   │   ├── client.go              #   HTTP 클라이언트, 재시도, 타임아웃
│   │   └── types.go               #   API 응답 구조체 정의
│   │
│   ├── middleware/
│   │   ├── bot_detect.go          # [DevOps] User-Agent 봇 감지
│   │   └── cache.go               # [DevOps] 응답 캐싱 미들웨어
│   │
│   └── renderer/
│       └── renderer.go            # [DevOps] 템플릿 로딩, 렌더링 엔진
│
├── templates/                     # ★ [FE] PR로 관리하는 영역
│   ├── layouts/
│   │   └── base.html              #   공통 HTML 껍데기
│   │
│   ├── partials/
│   │   ├── meta_tags.html         #   OG, Twitter Card 메타 태그
│   │   ├── structured_data.html   #   JSON-LD (Course, FAQ, Review 등)
│   │   └── pagination.html        #   페이지네이션 공통
│   │
│   ├── pages/
│   │   ├── course_detail.html     #   강의 상세
│   │   ├── challenge_detail.html  #   챌린지 상세
│   │   ├── mentoring_detail.html  #   멘토링 상세
│   │   └── clip_detail.html       #   클립 상세
│   │
│   └── config/                    #   [FE] 페이지별 메타 매핑 설정
│       ├── course.yaml
│       ├── challenge.yaml
│       ├── mentoring.yaml
│       └── clip.yaml
│
├── preview/                       # ★ [FE] 미리보기 도구
│   ├── server.go                  #   로컬 미리보기 서버
│   └── fixtures/                  #   테스트용 샘플 데이터
│       ├── course_sample.json
│       └── challenge_sample.json
│
├── tests/
│   ├── snapshot/                  #   렌더링 결과 스냅샷 테스트
│   └── seo/                       #   SEO 필수 요소 검증 테스트
│
├── Dockerfile                     # [DevOps]
├── Makefile                       # [DevOps] build, test, preview 명령
└── go.mod
```

### 역할 경계 정리

| 역할 | 담당 영역 | 작업 방식 |
|---|---|---|
| DevOps | `cmd/`, `internal/`, `Dockerfile`, 인프라 | 직접 커밋 |
| FE | `templates/`, `preview/fixtures/` | PR → 리뷰 → 머지 |
| BE | `internal/api/types.go` (API 스펙 변경 시) | PR → 리뷰 → 머지 |

---

## 4. FE 개발자 워크플로우

### 로컬 개발

```bash
# 1. 미리보기 서버 실행 (Go 설치 불필요, 바이너리 제공)
make preview

# 2. 브라우저에서 확인
# http://localhost:3001/preview/course/sample
# http://localhost:3001/preview/challenge/sample

# 3. 템플릿 수정 → 자동 리로드 (fsnotify)
# templates/pages/course_detail.html 수정 → 브라우저 새로고침
```

### 템플릿 작성 예시

```html
<!-- templates/pages/course_detail.html -->
{{template "base" .}}

{{define "meta"}}
  <title>{{.Course.Title}} - 인프런</title>

  <!-- 기본 OG (페이스북, 카카오톡, 슬랙, 디스코드 등) -->
  <meta property="og:title" content="{{.Course.Title}}" />
  <meta property="og:description" content="{{.Course.Description}}" />
  <meta property="og:image" content="{{.Course.ThumbnailURL}}" />
  <meta property="og:image:width" content="1200" />
  <meta property="og:image:height" content="630" />
  <meta property="og:type" content="website" />
  <meta property="og:site_name" content="인프런" />
  <meta property="og:url" content="https://www.inflearn.com/course/{{.Course.Slug}}" />

  <!-- Twitter/X 전용 (large image 카드) -->
  <meta name="twitter:card" content="summary_large_image" />
  <meta name="twitter:title" content="{{.Course.Title}}" />
  <meta name="twitter:description" content="{{.Course.Description}}" />
  <meta name="twitter:image" content="{{.Course.ThumbnailURL}}" />

  <!-- SEO 기본 -->
  <meta name="description" content="{{.Course.Description}}" />
  <link rel="canonical" href="https://www.inflearn.com/course/{{.Course.Slug}}" />
{{end}}

{{define "structured_data"}}
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "Course",
  "name": "{{.Course.Title}}",
  "description": "{{.Course.Description}}",
  "provider": {
    "@type": "Organization",
    "name": "인프런"
  },
  "instructor": {
    "@type": "Person",
    "name": "{{.Course.Instructor.Name}}"
  },
  "aggregateRating": {
    "@type": "AggregateRating",
    "ratingValue": "{{.Course.Rating}}",
    "reviewCount": "{{.Course.ReviewCount}}"
  }
}
</script>
{{end}}

{{define "content"}}
<article>
  <h1>{{.Course.Title}}</h1>

  <section class="instructor">
    <h2>지식공유자: {{.Course.Instructor.Name}}</h2>
  </section>

  <section class="description">
    {{.Course.Description | safeHTML}}
  </section>

  <section class="curriculum">
    <h2>커리큘럼</h2>
    {{range .Course.Curriculum}}
    <details>
      <summary>{{.SectionTitle}} ({{len .Lectures}}개 강의)</summary>
      <ul>
        {{range .Lectures}}
        <li>{{.Title}} ({{.Duration}})</li>
        {{end}}
      </ul>
    </details>
    {{end}}
  </section>

  <section class="reviews">
    <h2>수강평 ({{.Course.ReviewCount}}개, 평점 {{.Course.Rating}})</h2>
    {{range .Course.TopReviews}}
    <blockquote>
      <p>{{.Content}}</p>
      <footer>{{.Author}} · {{.Rating}}점</footer>
    </blockquote>
    {{end}}
  </section>
</article>
{{end}}
```

### CI 파이프라인 (PR 시 자동 실행)

```yaml
# .github/workflows/template-check.yml
on:
  pull_request:
    paths: ['templates/**']

jobs:
  validate:
    steps:
      - name: 템플릿 컴파일 테스트
        run: make test-compile

      - name: 샘플 데이터로 렌더링
        run: make test-render

      - name: SEO 필수 요소 검증
        run: make test-seo
        # og:title, og:description, canonical, JSON-LD 존재 여부 체크

      - name: HTML 사이즈 체크
        run: make test-size
        # 50KB 초과 시 경고
```

---

## 5. 미리보기 시스템

FE 개발자가 봇이 보는 HTML을 브라우저에서 직접 확인할 수 있는 도구입니다.

```
┌──────────────────────────────────────────┐
│          Preview Server (:3001)           │
│                                          │
│  /preview/course/:id                     │
│    ├── ?source=fixture  (샘플 데이터)      │
│    └── ?source=live     (실제 API 호출)    │
│                                          │
│  /preview/_diff/course/:id               │
│    → 현재 프로덕션 HTML vs 로컬 렌더링 diff  │
│                                          │
│  /preview/_validate/course/:id           │
│    → SEO 필수 요소 체크 결과 표시            │
└──────────────────────────────────────────┘
```

`/preview/_diff`는 현재 Playwright로 생성된 프로덕션 HTML과 새 템플릿으로 렌더링한 결과를 비교해서, 마이그레이션 과정에서 빠뜨린 정보가 없는지 확인하는 용도입니다.

---

## 6. 페이지별 메타 설정 (FE 관리)

```yaml
# templates/config/course.yaml
page:
  path_pattern: "/course/:slug"

api:
  primary: "/api/v1/courses/{id}"
  additional:
    reviews: "/api/v1/courses/{id}/reviews?size=5"
    curriculum: "/api/v1/courses/{id}/curriculum"

meta:
  title: "{{.Title}} - 인프런 강의"
  og_title: "{{.Title}}"
  og_description: "{{.Description | truncate 150}}"
  og_image: "{{.ThumbnailURL}}"
  canonical: "https://www.inflearn.com/course/{{.Slug}}"

structured_data:
  - type: Course
    mapping:
      name: "{{.Title}}"
      description: "{{.Description}}"
      provider: "인프런"
      instructor: "{{.Instructor.Name}}"
  - type: AggregateRating
    mapping:
      ratingValue: "{{.Rating}}"
      reviewCount: "{{.ReviewCount}}"

cache:
  ttl: 10m
  invalidate_on: ["course.updated", "review.created"]
```

이렇게 하면 FE 개발자가 Go 코드를 건드리지 않고도 어떤 API 필드가 어떤 meta 태그에 매핑되는지, 캐시 정책은 어떤지를 한 곳에서 관리할 수 있습니다.

---

## 7. 성능 비교 (예상)

| 항목 | Playwright (현재) | Go Bot SSR |
|---|---|---|
| HTML 생성 시간 | ~3초 | ~50-100ms |
| HTML 크기 | 200-500KB | 20-30KB |
| 필요 리소스 | 2CPU, 4Gi (Chromium) | 128MB (Go 바이너리) |
| 배포 단위 | K8s Pod + Chromium 이미지 | 단일 Go 바이너리 (~20MB) |
| 동시 처리 | Pod당 1건 (Chromium 점유) | 수천 req/s (goroutine) |
| Cold Start | 수십 초 (Chromium 로딩) | <100ms |
| FE 변경 반영 | 자동 (다음 렌더링) | PR → 머지 → 배포 |

---

## 8. 단점 및 대응

| 단점 | 대응 방안 |
|---|---|
| FE가 관리할 템플릿이 추가됨 | config.yaml로 선언적 관리, 미리보기 서버로 빠른 피드백 루프 |
| API 스펙 변경 시 양쪽 수정 필요 | `types.go`와 config.yaml에 API 스펙 명시, CI에서 스키마 검증 |
| FE 변경이 자동 반영되지 않음 | 템플릿 변경은 별도 배포 없이 S3 + 런타임 로딩으로 개선 가능 (Phase 2) |
| Go 템플릿 문법 학습 필요 | `html/template`은 `{{.Field}}`와 `{{range}}`만 알면 충분 |
| 새 SNS 봇 등장 시 미리보기 누락 가능 | 오픈소스 봇 DB + 커스텀 SNS 목록 병합, unknown_ua 모니터링으로 사전 감지 |

---

## 9. 마이그레이션 단계

**Phase 1: PoC (2-3주)**
- 강의 상세 1개 페이지만 Go Bot SSR로 구현
- 프로덕션 Playwright 결과와 diff 비교
- 성능 벤치마크
- SNS 미리보기 검증: 카카오톡/페이스북/슬랙/트위터 공유 테스트
  - 각 SNS 디버깅 도구 활용 (Facebook Sharing Debugger, Twitter Card Validator, 카카오 OG 캐시 초기화 등)

**Phase 2: 점진적 전환 (4-6주)**
- 챌린지/멘토링/클립 상세 페이지 추가
- CloudFront Lambda@Edge에서 봇 트래픽 분기
- A/B: 일부 봇 트래픽만 Go SSR로 라우팅하여 검색 노출 영향 모니터링
- 봇 감지 모니터링 대시보드 구축 (unknown_ua 경고 알림)

**Phase 3: 완전 전환**
- Playwright 파이프라인 제거
- K8s Pod(2CPU, 4Gi) → Go 서버(128MB)로 인프라 축소
- FE App에서 SEO 전용 코드 제거, 유저 경험에만 집중하도록 리팩토링

