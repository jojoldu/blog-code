# Linux에서 crontab 실행시 명령어 찾지 못할때

crontab의 기본 PATH는 ```/usr/bin``` 밖에 없다.  
즉, ```/usr/local/bin``` 등에 있는 command에 대해서는 crontab 이 인식하지 못한다.

```bash
PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin
LD_LIBRARY_PATH=/usr/local/lib

5 9  * * 1,2,3,4,5 my_bin some_args
```