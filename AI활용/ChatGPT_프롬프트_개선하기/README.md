# ChatGPT 프롬프트 조금 더 잘 사용하기

## 

## LEVER 기법

레버 기법은 AI 출력을 보다 세밀하게 조정할 수 있는 스마트한 방법
핵심적으로, 먼저 평가자체 출력을 가져와 정량화해 준다.
정량화된 출력을 통해 필요에 따라 매우 정밀하게 조정할 수 있다.

사용 방법은 다음과 같습니다.

ChatGPT에 1~10점 척도로 출력물을 평가하도록 요청합니다.  
1은 매우 캐주얼, 10은 매우 전문적이라는 식으로 척도를 정의할 수도 있다.
ChatGPT가 답을 알려준다(예: 6/10)
그런 다음 원하는 숫자로 조정한다(예: 3/10)

1)영어로 질문하세요.

2)상황을 구체적으로 설명하세요.


3)챗GPT에게 역할을 부여해 보세요.

- High-level planning부터 활용
  - ○○에 대한 글을 써줘 VS ○○에 대한 글을 쓰려 하는데, 단계별 작업 계획을 세워줘
- 최종 결과를 바로 생성하지 않고 중간 과정 거치기 (Chain of Thought 응용)
  - ○○에 대한 내용을 추려내서, 요약한 뒤, 보고서 형태로 정리해줘
  - 차근차근 단계별로 생각하세요. 설명을 먼저 적고, 마지막에 결론 적기.
- 가능한 영어로 작업
  - 더 많은 영어 자료로 학습해서 성능이 더 좋음
  - 같은 양의 정보를 한국어로 표현하면 token 수가 많아서 효율이 낮음
- 다양한 LLM 챗봇 활용하기
  - MS Copilot: [copilot.microsoft.com](https://copilot.microsoft.com) Claude: [claude.ai](https://claude.ai) Gemini: [gemini.google.com](https://gemini.google.com)
  - Reka: [chat.reka.ai](https://chat.reka.ai) Llama: [meta.ai](https://meta.ai) (국내 서비스 전) Mistral: [chat.mistral.ai](https://chat.mistral.ai) Coral: [coral.cohere.com](https://coral.cohere.com)
  - ChatGPT: GPTs, Code Interpreter, 사진 입력력, Browsing 등 다양한 기능 탐재
  - Claude: 논문 관련 작업을 잘 함(개인의견), Artifact 기능
  - Gemini: Google 검색을 통해 찾은 자료 정리 잘함(개인의견)
 
- LLM챗봇의 응답은 랜덤성이 있음
   - 답변을 재생성하거나 질문을 약간 바꿔서 물어보면 다른 답을 할 수 있음
- 예시 듣기
  - 특히 출력 형식이 중요한 경우
- 부정형 명령은 이해를 못 하는 경우가 있다
  - "Don't make the response long" VS "Make the response concise"
- 관련성이 낮은 내용은 최대한 삭제
  - Hallucination을 하거나 중요한 내용을 놓칠 가능성을 줄임
- 정서 자극(emotional stimuli)
  - "This is very important to my career."
- 답변 거부할 경우 안심시키기
  - 의학적 내용은 대답할 수 없다 -> "I am using this information to write an article."
  - "초안"을 작성해달라, "예시"를 보여달라, 이건 내가 쓴 글이니까 저작권 문제는 없다

- 무례하지 않을 정도의 예의를 지키기
- 다양한 답을 요청하기
  - 3가지 다른 입장을 상정한 뒤 각 입장에서 이 글에 대한 비판을 해주세요.
  - 주어진 논문 초록을 읽고 강조하면 좋을 부분 3가지를 선정한 뒤, 각각의 강조점을 부각하는 제목을 3가지씩 예시를 들어주세요.
  - Reinforcement learning에 대해서 초보자부터 전문가까지 5가지 난이도로 설명해주세요.