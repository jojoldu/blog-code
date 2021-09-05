# gitbook CLI 실행시 cb.apply 발생할 경우

```bash
npm install -g gitbook-cli
```

```bash
gitbook init
```

```typescript
Installing GitBook 3.2.3
/usr/local/lib/node_modules/gitbook-cli/node_modules/npm/node_modules/graceful-fs/polyfills.js:287
      if (cb) cb.apply(this, arguments)
TypeError: cb.apply is not a function
    at /usr/local/lib/node_modules/gitbook-cli/node_modules/npm/node_modules/graceful-fs/polyfills.js:287:18

```


```bash
cd /usr/local/lib/node_modules/gitbook-cli/node_modules/npm/node_modules/
```

```bash
npm install graceful-fs@latest --save
```

* https://stackoverflow.com/a/64211387