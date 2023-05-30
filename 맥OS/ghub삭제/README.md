# Logitech Ghub 완전 삭제하기

```bash
ls /Applications/lghub.app/Contents/MacOS/lghub_updater.app
```

해당 `lghub_updater.app` 안에 `/Contents/MacOS/lghub_updater` 를 통해 삭제를 할 수 있다.

```bash
sudo /Applications/lghub.app/Contents/MacOS/lghub_updater.app/Contents/MacOS/lghub_updater --uninstall
sudo rm -rf "/Applications/lghub.app"
sudo rm -rf "/Users/$(whoami)/Library/Application Support/lghub"
sudo rm -rf "/Users/Shared/LGHUB/"
sudo rm -rf "/Users/Shared/.logishrd"
```