# 커밋 전체에서 사용자 정보 변경하기

```bash
#!/bin/sh

git filter-branch -f --env-filter '

an="$GIT_AUTHOR_NAME"
am="$GIT_AUTHOR_EMAIL"
cn="$GIT_COMMITTER_NAME"
cm="$GIT_COMMITTER_EMAIL"

if [ "$GIT_COMMITTER_EMAIL" = "jojoldu@gmail.com" ]
then
    cn="jojoldu"
    cm="jojoldu@gmail.com"
fi
if [ "$GIT_AUTHOR_EMAIL" = "jojoldu@gmail.com" ]
then
    an="jojoldu"
    am="jojoldu@gmail.com"
fi

export GIT_AUTHOR_NAME="$an"
export GIT_AUTHOR_EMAIL="$am"
export GIT_COMMITTER_NAME="$cn"
export GIT_COMMITTER_EMAIL="$cm"
'
```