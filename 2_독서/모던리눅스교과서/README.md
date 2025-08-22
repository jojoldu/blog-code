# 모던 리눅스 교과서

## 실습환경

```yaml
version: '3'
services:
  centos8-container:
    image: centos:8
    container_name: my-centos8-container
    command: tail -f /dev/null
```

```Bash
docker-compose up -d
```

```Bash
docker exec -it my-centos8-container /bin/bash
```

```Bash
docker-compose down
```

## 2장

### 시스템 콜

```Bash
strace ls
```

```Bash
strace -c \
curl -s https://mhausenblas.info > /dev/null
```

## 3장