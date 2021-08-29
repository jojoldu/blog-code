# M1 맥에서 node sqlite3 설치가 안될 경우


```bash
npm config set python "$(which python3)"
```

```bash
gyp WARN download NVM_NODEJS_ORG_MIRROR is deprecated and will be removed in node-gyp v4, please use NODEJS_ORG_MIRROR
gyp ERR! configure error 
gyp ERR! stack Error: Command failed: /usr/bin/python3 -c import sys; print "%s.%s.%s" % sys.version_info[:3];
gyp ERR! stack   File "<string>", line 1
gyp ERR! stack     import sys; print "%s.%s.%s" % sys.version_info[:3];
gyp ERR! stack                       ^
gyp ERR! stack SyntaxError: invalid syntax
gyp ERR! stack 
gyp ERR! stack     at ChildProcess.exithandler (node:child_process:326:12)
gyp ERR! stack     at ChildProcess.emit (node:events:365:28)
gyp ERR! stack     at maybeClose (node:internal/child_process:1067:16)
gyp ERR! stack     at Socket.<anonymous> (node:internal/child_process:453:11)
gyp ERR! stack     at Socket.emit (node:events:365:28)
gyp ERR! stack     at Pipe.<anonymous> (node:net:661:12)
gyp ERR! System Darwin 20.6.0

```


```bash
node-gyp -v
```

```bash
v3.8.0
```


```bash
npm install -g node-gyp
```

```bash
npm install -g node-pre-gyp -g 
```