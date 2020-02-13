# Centos6 에서 Python3 & Pip3 설치하기


```bash
 yum install -y https://centos6.iuscommunity.org/ius-release.rpm
```

```bash
yum install -y python36u python36u-libs python36u-devel python36u-pip
```

```bash
curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
```

```bash
alternatives --install /usr/bin/python python3 /usr/bin/python3.6 5
```


```bash
python3 --version
```

```bash
ln -s /usr/bin/pip3.6 /usr/bin/pip3
```

```bash
ls -l /usr/bin/pip*
```

![pip_result](./images/pip_result.png)

pip3가 잘 반영되었다면 본인이 원하는 패키지들을 적절히 설치하자.  
ex) awscli

```bash
pip3 install awscli
```

