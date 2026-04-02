# Copilot Instructions

이 저장소에서 GitHub Copilot이 따라야 할 지침입니다.

## 언어 규칙

- **커밋 메시지**: 반드시 한국어로 작성하며, `feat:`, `fix:` 등 conventional commit prefix는 사용하지 않는다 (예: "기능 추가", "버그 수정", "문서 업데이트")
- **코드 리뷰/설명**: 한국어로 작성
- **코드 주석**: 한국어로 작성
- **변수명/함수명/클래스명**: 영어로 작성 (코드 표준 준수)

## 프로젝트 구조

- 각 디렉토리는 독립적인 프로젝트로, 개별 빌드 설정을 가짐
- 주요 스택: Java/Kotlin + Spring Boot + Gradle
- 테스트 프레임워크: JUnit 4/5, Spock

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
