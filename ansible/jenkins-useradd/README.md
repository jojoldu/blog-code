# 젠킨스로 Ansible 관리하기 - useradd

## 1. 설치

Centos 6,7 관계 없이 yum repo에 기본적으로 등록 되어 있으니 바로 yum install 합니다.

```bash
sudo yum install ansible - y
```

## 2. 노드 설정

```bash
vim /etc/ansible/hosts
```

```bash
ansible all -m ping
```

```bash
ansible all -m ping -k
```

### 노드 Ping 테스트

## 3. Playbook
