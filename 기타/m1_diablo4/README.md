# M1 Mac Mini에서 디아블로4 실행하기

DirectX 12 윈도우 게임들을 맥북에서 돌릴수있는 Translation layer! 

- https://twitter.com/0xggoma/status/1666735315447582720

## 설치


- [Command Line_Tools_for_Xcode_15_beta.dmg 설치](https://developer.apple.com/download/all/?q=xcode%2015)
- [Game porting toolkit beta 설치](https://developer.apple.com/download/all/?q=game%20porting%20toolkit)


## 터미널로 추가 설치


로젠타 설치

```bash
softwareupdate --install-rosetta
```

```bash
agree
```

Homebrew 설치

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

- https://jinnynote.com/learn/how-to-add-paths-on-mac/

```bash
brew tap apple/apple https://github.com/apple/homebrew-apple
```

```bash
brew -v install apple/apple/game-porting-toolkit
```

```bash
WINEPREFIX=~/my-game-prefix `brew --prefix game-porting-toolkit`/bin/wine64 winecfg
```

Windows 10
-> 적용
-> 확인