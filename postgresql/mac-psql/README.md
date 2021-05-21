# Mac에 PostgreSQL없이 PSQL만 설치하기

일반적인 GUI 클라이언트에서는 PostgreSQL의 `psql`을 지원하지 않습니다.  
  

```sql
psql --version
```

```bash
brew install libpq
```

![install](./images/install.png)

```bash
echo 'export PATH="/usr/local/opt/libpq/bin:$PATH"' >> ~/.zshrc
```

```bash
source ~/.zshrc
```

```bash
echo 'export PATH="/usr/local/opt/libpq/bin:$PATH"' >> ~/.bash_profile
```

```bash
source ~/.bash_profile
```


![connect](./images/connect.png)


## 주의

위처럼 수동이 아니라 brew를 통해 "link all of its binaries to the PATH anyway" (모든 바이너리를 PATH에 연결) 할 수도 있습니다.

```bash
brew link --force libpq
```

하지만 나중에 postgresql 패키지를 설치할 수 없습니다.  
r