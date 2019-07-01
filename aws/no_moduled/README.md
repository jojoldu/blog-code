# ImportError: No module named httpsession 문제 발생시


![1](./images/1.png)

```bash
from botocore.httpsession import URLLib3Session
ImportError: No module named httpsession
```

```bash
aws --version
```

```bash
pip install awscli --user --force-reinstall --upgrade --ignore-installed
```

```bash
sudo su - jenkins
```

만약 계정에 권한이 없다면 아래와 같이 젠킨스 job에 커맨드를 등록해서 실행합니다.

![2](./images/2.png)

![3](./images/3.png)


