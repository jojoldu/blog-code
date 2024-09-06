# 유동성 기반 아키텍처

유동성이 대단히 높은 시기의 유행하는 아키텍처와 유동성이 극도로 줄어든 시기의 유행하는 아키텍처는 다를 수 밖에 없다.  
  

여전히 그 시절의 유행하는 아키텍처를 개발 조직의 지향점으로 삼으면 안되는데, 아직도 그런 이야기가 주변에서 들리곤 한다.

어떤 특정 직군의 특정 프레임워크로 자꾸 문제를 풀려는 사람보다는 여러 계층을 폭넓게 바라보면서 더 대단하진 않더라도, 훨씬 더 효율적이고, 효과적인 방법을 선택하는 사람들이 회사에 꼭 필요한 시기인것 같다.

SSG vs CDN Cache 같은 경우를 예로 들 수 있다.  

Reddit에 아주 재미난 주제가 올라왔다.  
- [why does ssg make sense when you can always use the cdn cache?](https://www.reddit.com/r/nextjs/comments/1cxwg51/why_does_ssg_make_sense_when_you_can_always_use/)

SSG 환경을 구축하기 위해 들어가는 비용이나 관리의 문제가 과연 얼마나 큰 효과가 있는가?  
인프라 비용은 얼마나 절약이 가능한가?

근데 이만큼의 환경을 구축하면서 얻는 효과가 SSR + CDN 캐시에 비해 얼마나 효과적인가?
  
기본적으로 SSR로 구현하고 가장 앞 계층은 CloudFront나 Cloudflare를 통해 CDN 캐시만 적용해도 된다.  
