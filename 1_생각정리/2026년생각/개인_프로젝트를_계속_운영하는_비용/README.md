# 개인 프로젝트를 계속 운영하는 비용

개인 프로젝트들의 Node.js와 React 업데이트를 진행했다.  
업데이트를 마치고 나니 앞으로도 이런 소식을 계속 확인하고 필요한 업데이트를 반영해야 한다는 생각에 살짝 어질어질했다.

최근 1~2주 사이에 [Node.js](https://nodejs.org/en/blog/vulnerability/october-2026-security-releases), [Java](https://www.oracle.com/security-alerts/alert-cve-2026-91403.html), [PHP](https://blog.cloudflare.com/php-vulnerabilities-waf-protection/), [React 서버 컴포넌트(RSC)](https://github.com/react/react/security/advisories/GHSA-xqc2-xf4c-x3h3)의 심각한 보안 취약점이 연달아 공개됐다.  
여기에 Railway와 LiteLLM의 보안 사고까지 겹쳤다.

[Railway](https://blog.railway.com/p/preliminary-report-october-security-incident)에서는 내부 시스템 침입으로 일부 고객의 환경변수가 유출됐다.  
영향을 받은 고객들은 저장해둔 API 키 등의 인증정보를 직접 폐기하고 새로 발급받아야 했다.

LiteLLM은 [지난 3월](https://docs.litellm.ai/blog/security-update-march-2026)에 이어 [이번에도 악성 패키지가 배포되는 공급망 공격을 당했다](https://docs.litellm.ai/blog/security-incident-oct-21-2026-attack-chain).  
이번에는 공개됐던 내부 서비스의 마스터 키를 폐기하지 않은 것이 문제가 됐다.  
[사용자들도 설치한 패키지나 컨테이너 이미지가 영향을 받은 버전인지 직접 확인해야 했다.](https://docs.litellm.ai/blog/security-incident-oct-21-2026)

앞으로는 이런 보안 문제가 더 빠르게, 더 자주 드러날 거라는 이야기도 나온다.  
[로이터 기사](https://www.investing.com/news/stock-market-news/aidiscovered-bugs-may-soon-trigger-fresh-disruptions-cyber-experts-say-4968682)에서 Cloudflare CEO 매튜 프린스는 다른 핵심 소프트웨어의 심각한 취약점도 여럿 알고 있다고 밝혔다.  
앞으로 약 2년간은 매주 큰 보안 위기가 닥친 것처럼 느껴질 거라고도 했다.  
AI가 취약점을 찾고 공격 도구를 만드는 시간을 줄이면서, 운영자가 대응할 시간도 짧아지고 있다.

웹서비스 하나를 운영하는 데는 생각보다 많은 것들이 필요하다.

- Java와 Spring, PHP와 Laravel, Node.js와 Express 같은 백엔드 기술
- TypeScript와 React, Next.js 같은 프론트엔드 기술
- 리눅스와 Docker, CI/CD 같은 실행 및 배포 환경
- PostgreSQL 같은 데이터베이스
- GitHub, npm, Maven Central, Docker Hub 같은 저장소
- 도메인과 DNS, HTTPS 인증서

혼자 만들어서 쓰는 서비스라면 모니터링 도구까지 따로 도입하지는 않을 수도 있다.  
그렇지만 서비스를 실행하고 데이터를 저장하고 배포하는 데 필요한 것들만 적어도 이 정도다.

이 중 일부를 외부 서비스에 맡길 수도 있다.  
데이터베이스는 Supabase를 쓰고, 서버 실행과 배포는 Railway 같은 PaaS에 맡기는 식이다.  
그렇다고 관리할 일이 모두 없어지는 건 아니다.

[과거 CircleCI](https://circleci.com/blog/jan-4-2023-incident-report/)에서 환경변수가 유출됐을 때, 우리 팀은 기존 인증정보를 폐기하고 새로 발급받아 환경변수를 모두 교체했다.  
프로젝트 전체를 재배포했고, CI 환경도 전부 CircleCI에서 Jenkins로 이관했다.

개인용 서비스라도 계속 운영하려면 보안 사고 소식과 패치 내용을 확인해야 한다.  
내 서비스가 영향을 받는지 판단하고 필요한 업데이트를 반영한 뒤, 서비스가 제대로 동작하는지도 확인해야 한다.

그러다 보면 이런 생각이 든다.

"에이, 귀찮다.  
그냥 월 몇천 원 내고 누가 대신 관리해주면 좋겠다."

AI로 필요한 기능을 직접 만들 수 있으니 기존 SaaS를 쓸 이유가 줄어들 거라는 이야기가 많다.  
그런데 직접 만든 서비스를 계속 운영하는 비용까지 따져보면 어떨까?

서버비와 AI 사용료뿐 아니라 보안 업데이트와 사고 대응에 쓰는 시간까지 생각하면, 월 몇천 원을 내고 기존 SaaS를 쓰는 편이 낫겠다는 생각도 들었다.  
개인용 도구 하나를 쓰기 위해 이 모든 환경을 계속 관리하는 일까지 맡고 싶은지는 별개의 문제다.

안정적으로 서비스를 운영하고 배포와 모니터링까지 책임지는 SaaS라면, 합리적인 가격에 다시 경쟁력을 가질 수 있지 않을까?

일단 이번 주는 사이드 프로젝트 저장소마다 Renovate를 설치해둬야겠다.  
에이, 귀찮아!
