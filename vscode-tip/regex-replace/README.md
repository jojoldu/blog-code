# VSCode에서 정규표현식(Regex)로 원하는 문자열 치환하기


![base](./images/base.png)


```bash
\([0-9]\)
```

```
<$0>
```

괄호`()`를 기준으로 인덱스 번호가 매겨진다.

![pattern](./images/pattern.png)


```bash
\(([0-9])\)
```

```bash
<$1>
```

![success](./images/success.png)