# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

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

## 에세이 작성/수정 워크플로우

`1_생각정리`, `2_독서` 등 에세이 디렉토리의 README.md를 작성하거나 수정한 뒤에는 반드시 다음 워크플로우를 안내한다:

1. **Claude/Claude Code로 글 작성 또는 수정**
2. **essay-editing 피드백 수행** — 이 단계에서 첨삭 피드백
3. **피드백 반영 후 수정**
4. **Codex CLI로 크로스 피드백** — 아래 명령어로 OpenAI Codex CLI에 essay-editing 피드백을 요청:
   ```bash
   codex "이 글을 essay-editing 지침에 따라 피드백해줘: $(cat 대상파일경로/README.md)"
   ```
5. **Codex 피드백 평가 후 탈고** — Codex 피드백을 Claude에 전달하여 외부 피드백 평가 형식으로 검토, 최종 반영

에세이 작성/수정 작업이 끝나면 항상 이 워크플로우의 다음 단계를 안내할 것.
