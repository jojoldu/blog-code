# 1주차

* Data Lake
  * 장점은 이미 많지만, 단점은 **관리 비용**이 많이 필요
* Stream Queue
  * 일정기간동안 계속 보관중 (24시간 등)
    * 그 기간 동안은 계속 데이터 가져가서 분석이 가능
  * 일반 Queue
    * 바구니에 담긴 공처럼 누군가 가져가면 바구니엔 해당 공이 없음
* Amazon Kinesis Firehose
  * 큐에 담긴 데이터를 S3에 바로 저장해주는 서비스
    * S3뿐만 아니라 다른 서비스에도 저장 가능
* Amazon Pinpoint
  * 마케팅 도구
  * Stream으로 저장이 가능
  * 가격이 API Gateway보다 저렴함
* AWS DMS
  * 데이터 마이그레이션 서비스
  * 스파크 보다 DB에 부하를 덜 주면서 DB로 데이터 이관이 가능하다는 장점
* AWS Glue
  * Scalability가 떨어지는걸로 알고 있음
  * 비용이 비쌈 (하루만 잘못키면 100만원 나옴)

## 실습

```bash
2. web service 실행2- ec2 생성시

#!/bin/bash 
yum update -y 
yum install httpd -y 
sudo service httpd start

echo  "<html><h1> <br> <br> <center> 파이프라인 캠프 5 기 </center> </h1></html>" > /var/www/html/index.html 

3. java 설치 
3.1. java 가 설치되어 있지 않을 경우
java -version
sudo yum install -y java-1.8.0-openjdk-devel.x86_64
java -version
 
p41 

4. EC2에 kafka Server설치  

4.1. Download & Unzip
wget https://downloads.apache.org/kafka/2.6.0/kafka_2.13-2.6.0.tgz
tar xvf kafka_2.13-2.6.0.tgz  

4.2. START THE KAFKA ENVIRONMENT p42

# Start the Kafka broker service 
ln -s kafka_2.13-2.6.0 kafka  (키보드에서 직접 넣어준다.)
cd kafka

./bin/zookeeper-server-start.sh config/zookeeper.properties & 

start kakfa broker 
./bin/kafka-server-start.sh config/server.properties & 

# 데몬확인
$ sudo netstat -anp | egrep "9092|2181"

# Topic 생성 
# 이름은 twitter 

$ bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic twitter &

# Topic 확인 
$ bin/kafka-topics.sh --list --zookeeper localhost:2181 


4.3. consumer 실행 - kafka server

./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic twitter --from-beginning

5. Producer 설치 
: kafka 와 logstash 설치 

5.1. kafka client 설치 및 producer 실행 
-  Download & Unzip
wget https://downloads.apache.org/kafka/2.6.0/kafka_2.13-2.6.0.tgz
tar xvf kafka_2.13-2.6.0.tgz  
ln -s kafka_2.13-2.6.0 kafka
cd kafka
bin/kafka-console-producer.sh --topic twitter --bootstrap-server 172.31.0.205:9092
위에 ip-address는 kafka server의 private ip입니다. 

#kafka server에서 확인한다. 
[ec2-user@ip-172-31-14-164 kafka]$ ifconfig -a
eth0: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 9001
        inet 172.31.14.164  netmask 255.255.240.0  broadcast 172.31.15.255
        inet6 fe80::f5:49ff:fea3:9192  prefixlen 64  scopeid 0x20<link>
        ether 02:f5:49:a3:91:92  txqueuelen 1000  (Ethernet)
        RX packets 230281  bytes 277424147 (264.5 MiB)
        RX errors 0  dropped 0  overruns 0  frame 0
        TX packets 105574  bytes 11669643 (11.1 MiB)
        TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0

5.2. logstash - producer 프로그램설치  from twitter p53 

wget https://artifacts.elastic.co/downloads/logstash/logstash-7.4.0.tar.gz

tar xvzf logstash-7.4.0.tar.gz
ln -s logstash-7.4.0 logstash

- 초기화 파일에 추가해준다. : 어느 폴더에서도 logstash 실행가능 
vi ~/.bash_profile

export LS_HOME=/home/ec2-user/logstash
PATH=$PATH:$LS_HOME/bin

source ~/.bash_profile

logstash --version

vi producer_test.conf

input {

twitter {
consumer_key => ""
consumer_secret => ""
oauth_token => ""
oauth_token_secret => ""
keywords => ["news","game","bigdata","부동산"]
full_tweet => true
}
}
output{
stdout{
codec => rubydebug
}
}
 
#실행한다.  - 화면에 twitter 정보가 print out 되는것을 확인한다. 
logstash -f producer_test.conf

p57 
# twitter 정보를 kafka topic twitter에 보낸다. 
# 확인 방법 : kafka server에 consumer console에 twitter 정보가 나오는것을 확인한다.
# ip정보는 kafka private ip address 

cp producer_test.conf producer.conf 
vi producer.conf

input {

twitter {
consumer_key => ""
consumer_secret => ""
oauth_token => ""
oauth_token_secret => ""
keywords => ["news","game","bigdata","AI"]
full_tweet => true
}
}

output {
kafka {
bootstrap_servers => "172.31.0.205:9092"
codec => json{}
acks => "1"
topic_id => "twitter"
}
}


logstash -f producer.conf


p63 -- logstash 로 따로 consumer를 구성한다. 참고.

input {
  kafka {
    bootstrap_servers => "172.31.26.26:9092"
    topics => ["twitter"]
    consumer_threads => 1
    decorate_events => true
    }
}

output {
stdout {
codec=> rubydebug
}
}

```