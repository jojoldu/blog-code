# Ruby Gem::FilePermissionError 에러 발생시 해결 방법 


```bash
$ gem install bundler
ERROR:  While executing gem ... (Gem::FilePermissionError)
    You don't have write permissions for the /Library/Ruby/Gems/2.3.0 directory.
```

```bash
brew update
brew install rbenv ruby-build
```

```bash
$ rbenv versions
* system (set by /Users/idong-uk/.rbenv/version)
```

```bash
$ rbenv install -l
```

```bash
vim ~/.zsrhc
```

```bash
vim ~/.bash_profile
```

```bash
[[ -d ~/.rbenv  ]] && \
  export PATH=${HOME}/.rbenv/bin:${PATH} && \
  eval "$(rbenv init -)"
```

```bash
source ~/.zshrc
```