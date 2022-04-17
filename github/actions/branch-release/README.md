# Release Merge시 Tag 생성, Release 삭제하기 (feat. Gihtub Action)

```yml
name: echo branch Name
on:
  push:
    branches:
      - master
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Get branch names
        id: branch-name
        uses: tj-actions/branch-names@v4.9

      - name: 버전 정보 추출(from Branch Name)
        run: echo "TAG=$(echo '${{ steps.branch-name.outputs.current_branch }}')" >> $GITHUB_ENV

      - name: echo current branch name
        run: echo "current branch name - ${{ env.TAG }}"
```

```yml
name: Release
on:
  pull_request:
    branches:
      - master
    types: [ closed ]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Get branch names
        id: branch-name
        uses: tj-actions/branch-names@v4.9

      - name: 버전 정보 추출(from Branch Name)
        run: echo "TAG=$(echo '${{ steps.branch-name.outputs.current_branch }}' | egrep -o '[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}')" >> $GITHUB_ENV

      - name: Release & Tag 생성
        if: github.event.pull_request.merged == true
        uses: actions/create-release@v1
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
        with:
          tag_name: ${{ env.TAG }}
          release_name: ${{ env.TAG }}
```