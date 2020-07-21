# AWS Lightsail 시작하기

![aws1](./images/aws1.png)

![aws2](./images/aws2.png)

![aws3](./images/aws3.png)

![aws4](./images/aws4.png)

![aws5](./images/aws5.png)

![aws6](./images/aws6.png)

![aws7](./images/aws7.png)

![aws8](./images/aws8.png)

![aws9](./images/aws9.png)

![aws10](./images/aws10.png)

![aws11](./images/aws11.png)

![aws12](./images/aws12.png)

![aws13](./images/aws13.png)

![aws14](./images/aws14.png)

![aws15](./images/aws15.png)

![aws16](./images/aws16.png)

![aws17](./images/aws17.png)

```bash
ssh ec2-user@퍼블릭IP -i /Users/idong-uk/.ssh/jojoldu-lightsail.pem -o StrictHostKeyChecking=no
```

```bash
Warning: Permanently added 'XXX.XXX.XXX.XXX' (ECDSA) to the list of known hosts.
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@         WARNING: UNPROTECTED PRIVATE KEY FILE!          @
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
Permissions 0644 for '/Users/idong-uk/.ssh/jojoldu-lightsail.pem' are too open.
It is required that your private key files are NOT accessible by others.
This private key will be ignored.
Load key "/Users/idong-uk/.ssh/jojoldu-lightsail.pem": bad permissions
ec2-user@XXX.XXX.XXX.XXX: Permission denied (publickey).
```

```bash
chmod 600 라이트세일pem키
```


```bash
sudo yum update -y
```




 
## 더이상 사용하지 않을 시

![aws-delete](./images/aws-delete-eip1.png)

![aws-delete](./images/aws-delete-eip2.png)

![aws-delete](./images/aws-delete.png)