# Mac에 PostgreSQL없이 PSQL만 설치하기

일반적인 GUI 클라이언트에서는 PostgreSQL의 `psql`을 지원하지 않습니다.  
  
그러다보니 `psql` 명령어를 입력이 필요하면 별도 설치가 필요합니다.  

> 물론 PC에 PostgreSQL 을 설치하셨으면 `psql` 이 설치되어있겠지만, Cloud, Docker 가 대중화된 시점에서 직접 PC에 PostgreSQL을 설치하는 경우는 거의 없는것 같습니다.

## 1. 설치

먼저 기존에 설치된 `psql`이 있는지 확인해봅니다.

```sql
psql --version
```

해당 명령어 실행시 설치가 안된 것이 확인이 되면 Homebrew를 통해 `libpq`을 설치합니다.

```bash
brew install libpq
```

해당 패키지 설치시 psql, pg_dump 및 기타 클라이언트 유틸리티 전체를 사용할 수 있습니다.  
  
다만, `PostgreSQL` 패키지에 포함 된 것과 동일한 유틸리티를 제공하기 때문에 기본적으로 PATH에 등록되진 않습니다.  
  
그래서 설치된 `libpq`를 PATH에 추가합니다.  
  
**zsh**

```bash
echo 'export PATH="/usr/local/opt/libpq/bin:$PATH"' >> ~/.zshrc
```

```bash
source ~/.zshrc
```

**bash**

```bash
echo 'export PATH="/usr/local/opt/libpq/bin:$PATH"' >> ~/.bash_profile
```

```bash
source ~/.bash_profile
```

설치와 PATH 지정이 끝나셨으면 다시 `psql --version` 로 확인해봅니다.

![install](./images/install.png)

설치가 잘되셨으면 한번 `psql`로 DB에 접속해봅니다.

![connect](./images/connect.png)


## 주의

위처럼 수동이 아니라 brew를 통해 "link all of its binaries to the PATH anyway" (모든 바이너리를 PATH에 연결) 할 수도 있습니다.

```bash
brew link --force libpq
```

하지만 이럴경우 나중에 PC에 PostgreSQL을 설치할 수 없습니다.  
혹시 모르니 직접 `link` 하시기 보다는 수동으로 PATH 등록하시길 추천드립니다.