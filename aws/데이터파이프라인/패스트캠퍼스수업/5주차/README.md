# 5주차

```bash

#15 presto 설정
$ /usr/lib/presto/bin/presto-cli-0.240.1.amzn.0-executable --server localhost:8889 --catalog hive --schema default
presto:default> show tables ;
presto:default> select * from hive.njkwon.danji_user_view;

#16 zeppelin jdbc interprinter 설치
sudo su - 
export JAVA_TOOL_OPTIONS="-Dzeppelin.interpreter.dep.mvnRepo=http://insecure.repo1.maven.org/maven2/"
cd /usr/lib/zeppelin
./bin/install-interpreter.sh --name jdbc
./bin/zeppelin-daemon.sh stop (수회 수행)
./bin/zeppelin-daemon.sh start
sudo systemctl stop zeppelin
ps -ef|grep zeppelin 
sudo systemctl stop zeppelin
/usr/lib/zeppelin/conf
cp zeppelin-site.xml.template zeppelin-site.xml
vi zeppelin-site.xml
<property>
  <name>zeppelin.interpreter.dep.mvnRepo</name>
  <value>http://insecure.repo1.maven.org/maven2/</value>
  <description>Remote principal repository for interpreter's additional dependency loading</description>
</property>


사용자에 의해 패치된 presto-jdbc 사용
    https://github.com/gabrielrcouto/presto/releases
    cd /home/hadoop
    sudo wget https://github.com/gabrielrcouto/presto/releases/download/0.225/presto-jdbc-0.225-SNAPSHOT.jar 
    sudo chown hadoop:hadoop presto-jdbc-0.225-SNAPSHOT.jar 

#19
default.driver : com.facebook.presto.jdbc.PrestoDriver
default.url    : jdbc:presto://ec2-3-36-60-231.ap-northeast-2.compute.amazonaws.com:8889
default.user   : hive
Dependencies
com.facebook.presto:presto-jdbc:0.179 
curl -L -O https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.19/mysql-connector-java-8.0.19.jar
또는
wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.19/mysql-connector-java-8.0.19.jar
다운받아서 다음 위치에 넣어 주면 된다.
(zeppelin home)/interpreter/jdbc
$ZEPPELIN_HOME/interpreter/jdbc/
athena.ap-northeast-2.amazonaws.com
s3://fc-datapipeline/sql/
AKIAYLNFI54NRH672R3B    /98XX/HI1L7KJaIqiyiZ0C/xfWjI1Vge91EWCxVt
Driver download 설명서
https://www.tableau.com/ko-kr/support/drivers?edition=pro&lang=ko-kr&platform=mac&cpu=64&version=2019.2&__full-version=20192.20.0119.2115#athena
Download site 
https://docs.aws.amazon.com/athena/latest/ug/connect-with-jdbc.html
Mac : 
$cd /Users/njkwon/Library/Tableau/Drivers
$cp ~/Downloads/AthenaJDBC42.jar .
Window : 
C:\Program Files\Tableau\Drivers
#47
$ sudo yum install -y java-1.8.0-openjdk-devel.x86_64

5.2. logstash - producer 프로그램설치  from twitter p53 
$ sudo yum install -y java-1.8.0-openjdk-devel.x86_64
$ wget https://artifacts.elastic.co/downloads/logstash/logstash-7.4.0.tar.gz
$ tar xvzf logstash-7.4.0.tar.gz
$ ln -s logstash-7.4.0 logstash
- 초기화 파일에 추가해준다. : 어느 폴더에서도 logstash 실행가능 
$ vi ~/.bash_profile
export LS_HOME=/home/ec2-user/logstash
PATH=$PATH:$LS_HOME/bin
$ source ~/.bash_profile
$ logstash --version
$ mkdir producer
$ cd producer
$ vi producer_test.conf
input {
  s3 {
      bucket => "fc-datapipeline"
      codec => "json"
      prefix => "source/json/"
      region => "ap-southeast-2"
      interval => "10"
      watch_for_new_files => false
    }
  }
output {
stdout {codec=>rubydebug}
}
$mkdir json 
$cp producer_test.conf producer.conf
input {
s3 {
access_key_id => "AKIAYLNFI54NXSIVZUEN"
secret_access_key => "GJkyGGwffK6KMg8C0+vRz2s2UJYil2yWiGAiaUrc"
bucket => "fc-datapipeline"
region => "ap-northeast-2"
prefix => "logs/samplejson"
}
}
filter {
  json {
    source => "message"
  }
}
output {
  elasticsearch {
    hosts => ["https://search-data33-45bpee6yy4is4bnnrhisnjvbde.ap-northeast-2.es.amazonaws.com:443"]
    ssl => true
    index => "data33-%{+YYYY.MM.dd}"
    user => "data33"
    password => "Kwonpark77~"
    ilm_enabled => false
  }
}
$aws s3 sync  s3://fc-datapipeline/source/json/  .
PUT _template/template_3
{
  "index_patterns": ["data33*"],
  "mappings": {
    "doc": {
      "properties": {
        "timestamp": {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss"
        }, 
        "base_date": {
          "type": "date",
          "format": "yyyy-MM-dd"
        }, 
          "base_dt": {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss"
        } 
      }
    }
  }
} 
```