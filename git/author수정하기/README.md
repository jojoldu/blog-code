# 커밋된 내용에서 author 수정하기
하나의 Source Tree로 github 계정 여러개를 사용할 경우 간혹 신규생성 or 새로 클론 받은 저장소 작성자가 원치않는 계정으로 등록되어 커밋될 경우가 있습니다.  
이럴 경우 원하는 계정으로 커밋이 되지 않아 github 컨트리뷰션에 남지않는 불상사가 생기는데요, 이럴때 rebase를 이용하여 문제를 해결할 수 있습니다.  
이번 시간에는 rebase에 초점을 맞추는 것이 아니기 때문에 rebase에 대해 좀 더 찾아보고 싶으신 분들은 [아웃사이더님의 포스팅](https://blog.outsider.ne.kr/666)과 [김찬웅님의 NDC2016 발표자료(p.38부터)](http://www.slideshare.net/kexplo/ndc2016-effective-git) 을 참고 부탁드립니다.  

1) rebase 시작
```
git rebase -i B
```
2) 변경 원하는 커밋을 ```edit```로 변경 및 나가기

3) author 수정
```
git commit --amend --author="사용자명 <이메일>"
```

4) 다음 커밋으로 계속 진행
```
git rebase --continue
```

5) 수정을 원하는 커밋일 경우 다시 rebase로 수정
```
git commit --amend --author="사용자명 <이메일>"
```

6) 3~5 과정 반복

### 소스트리
소스트리 가능한지 체크
### 참고
[stackoverflow](http://stackoverflow.com/questions/3042437/change-commit-author-at-one-specific-commit)
