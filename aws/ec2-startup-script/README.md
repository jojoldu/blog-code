# EC2 Amazon Linux2 Startup Service

```bash
#!/bin/bash

# start hbase
/home/ec2-user/pinpoint/hbase/bin/start-hbase.sh

# start web
nohup java -jar -Dpinpoint.zookeeper.address=localhost /home/ec2-user/pinpoint/pinpoint-web-boot-2.2.2.jar >/dev/null 2>&1 &

# start collector
nohup java -jar -Dpinpoint.zookeeper.address=localhost /home/ec2-user/pinpoint/pinpoint-collector-boot-2.2.2.jar >/dev/null 2>&1 &
```

```bash
chmod +x startup.sh
```

```bash
vim /etc/systemd/system/pinpoint.service
```

```bash
[Unit]
Description=Pinpoint startup script
After=network.target

[Service]
Type=simple
ExecStart=/home/ec2-user/pinpoint/startup.sh
TimeoutStartSec=0

[Install]
WantedBy=default.target
```

```bash
sudo vim /etc/systemd/system/pinpoint.service
```

```bash
sudo systemctl daemon-reload
```

```bash
$ sudo systemctl enable pinpoint.service
Created symlink from /etc/systemd/system/default.target.wants/pinpoint.service to /etc/systemd/system/pinpoint.service.
```

```bash
sudo systemctl start pinpoint.service
```




* https://www.thegeekdiary.com/centos-rhel-7-how-to-make-custom-script-to-run-automatically-during-boot/

