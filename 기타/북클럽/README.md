# 북클럽 페이지 가이드

제목:

```text
Thanks for Playing
```

본문:

```text
8주 동안 우리는 책을 완독하는 것보다, 책 앞에서 멈춘 자기 생각을 꺼내는 일을 더 오래 연습했습니다.

누군가는 AI 시대의 불안을 말했고, 누군가는 커리어의 방향을, 누군가는 건강과 루틴과 사람 사이의 거리를 말했습니다. 네 권의 책은 결국 각자의 삶을 다시 읽게 하는 맵이 되었습니다.

북클럽은 끝났지만, 각자의 달리기는 계속됩니다. 이 페이지가 언젠가 다시 필요한 순간에 조용히 열리는 저장 파일이 되기를 바랍니다.
```

클리어 배지:

```text
BOOK CLUB QUEST CLEAR
2026.03.02 - 2026.04.25
```

푸터:

```text
향로와 함께한 북클럽 1기 · 단단한 나를 만드는 시간 · MEMORY SAVED
```

## 8. 디자인 요구사항

### 8.1 전체 스타일

- 픽셀 RPG 게임 UI
- 게임 포스터, 티켓, 저장 파일, 월드맵, 퀘스트 카드, 대화창, 인벤토리, 엔딩 크레딧 같은 구조
- 굵은 검정 테두리
- 또렷한 픽셀 그림자
- 연한 격자 배경
- 각 섹션은 명확히 구분하되, 하나의 게임 세계처럼 이어져야 한다.

### 8.2 색상

주요 색상:

- 진한 남색/검정: 텍스트, 테두리, 대화창 배경
- 하늘색: 그림자, 강조
- 핑크: 클리어 문구, 강조 카드
- 노랑: 티켓, 버튼, 강조 라벨
- 초록: 북클럽/성장 느낌
- 보라: 보조 강조

권장 CSS 변수 예시:

```css
:root {
  --ink: #132536;
  --ink-deep: #07131e;
  --paper: #f7fbff;
  --paper-2: #e8f0f7;
  --blue: #53c4f3;
  --blue-dark: #1595cc;
  --pink: #ef4b6e;
  --pink-dark: #b62f4d;
  --yellow: #ffd85a;
  --yellow-dark: #dcae2c;
  --green: #60c653;
  --purple: #8f78dc;
}
```

### 8.3 타이포그래피

- 큰 영문 타이틀은 Impact 계열 또는 굵은 Condensed 계열 사용
- 한국어 본문은 `Pretendard`, `Apple SD Gothic Neo`, `Malgun Gothic`, system-ui 계열 사용
- 라벨, 번호, 작은 게임 UI 텍스트는 monospace 사용

폰트 예시:

```css
--body-font: "Pretendard", "Apple SD Gothic Neo", "Malgun Gothic", system-ui, sans-serif;
--display-font: Impact, Haettenschweiler, "Arial Narrow Bold", "Arial Black", sans-serif;
--mono-font: "Courier New", "D2Coding", monospace;
```

### 8.4 레이아웃

- 최대 콘텐츠 너비: 약 1160px
- 모바일에서는 모든 그리드가 1열로 자연스럽게 내려가야 한다.
- YouTube 영상은 16:9 비율 유지
- 텍스트가 버튼이나 카드 밖으로 넘치지 않아야 한다.

## 9. 인터랙션 요구사항

### 9.1 World Map

퀘스트 노드 클릭 시 다음 요소를 변경한다.

- 기간
- 장소
- 책 제목
- 설명 문구
- active 상태

### 9.2 Dialogue Log

질문 탭 클릭 시 다음 요소를 변경한다.

- 스피커
- 질문
- 설명
- 선택지
- active 상태
- `aria-selected` 값

### 9.3 Memory Items

- hover 또는 focus 시 설명 툴팁 표시
- 키보드 접근성을 위해 `tabindex="0"` 적용

## 10. 접근성 요구사항

- `lang="ko"` 설정
- `meta viewport` 설정
- 상단에 본문 바로가기 링크 제공
- 각 섹션은 `aria-labelledby` 사용
- 인터랙션 영역에는 적절한 `aria-label`, `aria-live`, `role="tablist"`, `role="tab"` 사용
- 이미지에는 alt 제공
- 버튼과 링크는 키보드로 접근 가능해야 한다.

## 11. 필요한 자산

필수:

- 참가자 8명 캐릭터 이미지
- 파일명 권장: `북클럽1기_참가자_캐릭터.png`
- YouTube 영상 embed URL
- 책 소개 및 발제문
- 독후감 발췌 또는 익명화된 회고 문장

선택:

- 각 참가자 캐릭터 개별 이미지
- 각 책 표지 이미지
- 실제 북클럽 사진 또는 스크린샷
- 회차별 대표 문장

## 12. 검수 체크리스트

- [ ] 첫 화면에서 북클럽 1기, QUEST CLEAR, 4권/8주/8명 정보가 바로 보이는가
- [ ] 픽셀 RPG 콘셉트가 페이지 전반에 일관되게 보이는가
- [ ] Party Members 섹션에서 8명 참가자가 명확히 보이는가
- [ ] 참가자 이미지가 없을 때도 fallback UI가 깨지지 않는가
- [ ] World Map의 퀘스트 클릭 인터랙션이 정상 동작하는가
- [ ] Dialogue Log의 탭 클릭 인터랙션이 정상 동작하는가
- [ ] YouTube iframe이 정상 표시되는가
- [ ] 모바일에서 텍스트와 카드가 겹치지 않는가
- [ ] 내비게이션 앵커 이동이 정상 동작하는가
- [ ] 페이지를 로컬 HTML 파일로 열어도 동작하는가

## 13. 한 줄 요약

8명이 8주 동안 4권의 책을 함께 읽은 북클럽 1기를, 픽셀 RPG 게임의 퀘스트 클리어 기록처럼 보여주는 회고형 웹페이지를 제작한다.