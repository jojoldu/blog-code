# M1에서 bcrypt 문제 발생할 때

```bash
npm ERR! bcrypt@5.0.1 install: node-pre-gyp install --fallback-to-build
```

```bash
npm i bcrypt@5.1.0
```

Node.js v16.0.0 이전의 버전을 설치할 때는 arch -x86_64 zsh를 사용한다.

```bash
$ arch -x86_64 zsh
$ nvm install 14.15.1
```

Node.js v16.0.0 이후의 버전을 설치할 때는 arch -arm64 zsh를 사용한다.

```bash
$ arch -arm64 zsh
$ nvm install 18.12.0
```

```bash
$ node
Welcome to Node.js v16.15.1.
Type ".help" for more information.
> process.arch
'arm64'
```
