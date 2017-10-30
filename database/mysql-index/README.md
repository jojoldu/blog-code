# MySQL 인덱스

MySQL 인덱스에 관해 정리를 하였습니다.  
MySQL을 잘 알아서 정리를 한것이 아니라, 잘 알기위해 정리한 것이라서 오류가 있을수도 있습니다.  

## 설치

혹시나 해서, Mac에서 MySQL 설치는 공식홈페이지가 아닌 **Homebrew로 설치**하는게 좋습니다.  

> 삭제 과정이 굉장히 귀찮습니다.

[Homebrew로 설치하기](https://gist.github.com/nrollr/3f57fc15ded7dddddcc4e82fe137b58e)

Homebrew Ruby 업데이트

```bash
brew upgrade ruby
```

Homebrew 업데이트

```bash
brew update
```

## 인덱스

### 인덱스 컬럼 순서와 조회 컬럼 순서

최근 RDBMS가 업데이트 되면서 이전과 같이 꼭 인덱스 순서와 조회 순서를 지킬 필요는 없습니다.  
단, 옵티마이저가 **조회 조건의 컬럼을 인덱스 컬럼 순서에 맞춰 재배열하는 과정이 추가**되기 때문에 특별한 이유가 없다면 인덱스 순서와 조회 컬럼 순서를 지켜주시면 더 좋을것 같습니다.

### 쿼리 프로파일링

```sql
show variables like 'profiling';
```

```sql
set profiling=1;
```



## 참고

* [what-makes-a-good-mysql-index-part-2-cardinality](https://webmonkeyuk.wordpress.com/2010/09/27/what-makes-a-good-mysql-index-part-2-cardinality/)
