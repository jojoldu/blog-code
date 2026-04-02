# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## Repository Overview

This is a collection of independent sample code projects for blog posts at http://jojoldu.tistory.com/. Each subdirectory is a standalone project with its own build configuration, typically demonstrating a specific concept or technique.

## Architecture

- **No unified build**: There is no root-level build system. Each project is self-contained.
- **Primary stack**: Java/Kotlin with Spring Boot and Gradle (various Spring Boot versions from 1.x to 2.x)
- **Test frameworks**: JUnit 4/5, Spock (Groovy), with Spring Boot Test
- **Common patterns**: JPA/Hibernate, Spring Batch, QueryDSL, REST APIs

## Working with Individual Projects

Each project directory contains its own `gradlew` wrapper. Navigate to the specific project directory first.

```bash
# Build a specific project
cd spring-boot-tips
./gradlew build

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.example.SomeTest"

# Run a single test method
./gradlew test --tests "com.example.SomeTest.testMethod"
```

## Directory Structure Patterns

- `spring-*`, `springboot-*`: Spring Boot example projects
- `jpa-*`: JPA/Hibernate focused examples
- `database/`: MySQL/database optimization examples
- `aws/`: AWS integration examples
- `1_생각정리`, `2_독서`, etc.: Korean-named directories contain blog post drafts/notes (non-code)
- Each project's README.md contains the related blog post content

## 언어 규칙

- **커뮤니케이션**: 모든 응답과 설명은 한국어로 작성
- **커밋 메시지**: 반드시 한국어로 작성하며, `feat:`, `fix:` 등 conventional commit prefix는 사용하지 않는다 (예: "기능 추가", "버그 수정", "문서 업데이트")
- **코드 주석/문서화**: 한국어로 작성
- **변수명/함수명/클래스명**: 영어로 작성 (코드 표준 준수)

디렉토리명과 README 등 기존 문서도 대부분 한국어로 작성되어 있음.

## 글쓰기 스타일

- **소제목 사용 금지**: `1_생각정리`, `2_독서` 등의 디렉토리에 있는 블로그 글에는 `##`, `###` 등의 소제목을 사용하지 않는다. 제목(`#`)만 사용
- **자연스러운 흐름**: 소제목 없이 문단 전환만으로 내용이 자연스럽게 이어지도록 작성

## 에세이 첨삭 (essay-editing)

`1_생각정리`, `2_독서` 등 에세이 디렉토리의 README.md 파일에 대해 첨삭·피드백 요청이 오면 아래 지침을 따른다.

### 글의 특성

- **플랫폼**: 티스토리 블로그 (jojoldu.tistory.com)
- **형식**: 마크다운, 제목(`#`)만 사용하고 소제목(`##`, `###`)은 사용하지 않는다
- **문체**: 구어체 에세이. "~했다", "~인 것 같다" 등 말하듯 쓰는 톤
- **구조**: 소제목 없이 문단 전환만으로 내용이 자연스럽게 이어진다
- **독자**: 개발자 커뮤니티 (주니어~시니어)

### 피드백 원칙

1. **독자 관점으로 읽어라** — 처음 읽는 독자가 맥락 없이 이해할 수 있는지, 앞뒤 문단이 자연스럽게 이어지는지, 핵심 메시지가 한 문장으로 요약 가능한지 판단한다.
2. **에세이 문체를 존중하라** — 구어체를 교정하지 않는다. "근데", "진짜", "~거다" 같은 표현은 의도적 선택이다. 설명체로 바꾸지 않는다. 불릿 리스트보다 산문을 권하고, 볼드체는 최소화를 권한다.
3. **구조적 문제를 먼저, 문장 문제를 나중에** — 핵심 메시지 명확성 → 도입-본론-결말 연결 → 반복과 군더더기 → 문장 톤 일관성 → 개별 문장 다듬기 순서로 피드백한다.
4. **핵심 문장은 보호하라** — 글에서 가장 강한 문장(펀치라인)을 식별하고, 제목·결말까지 이어지는지 확인한다. 결말은 새로운 개념을 도입하지 말고 이미 쓴 어휘로 마무리한다.
5. **비유/전문용어의 정확성을 검증하라** — 해당 분야에서 용어가 맞는지, 비유가 논지와 정확히 대응하는지 확인한다. 검증할 수 없는 구체적 사례는 삭제를 권한다.

### 피드백 형식

첨삭 요청 시:

```
**좋아진 점:** (이전 버전 대비 개선된 점, 초안이면 생략)

**걸리는 점 N가지:**
1. [구체적 문제] — [왜 문제인지] — [개선 방향]
2. ...

(전체 완성도 한 줄 평가)
```

- 걸리는 점은 3~5개로 제한한다.
- 각 항목은 "문제 → 이유 → 방향"을 반드시 포함한다.
- 취향 영역인 것은 명시한다.

외부 피드백 평가 요청 시:

```
**강하게 동의하는 것:** (항목 + 이유)
**조건부 동의:** (항목 + 조건/취향 영역 명시)
**동의하지 않는 것:** (항목 + 반론)
**반영 추천 우선순위:** (번호 매긴 목록)
```
