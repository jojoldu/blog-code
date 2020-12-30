# 6주차

```bash
1. JupiterNote Lab 
$cd work
$wget https://fc-datapipeline.s3.ap-northeast-2.amazonaws.com/notebook/1.readwriteFile2.ipynb
EMR Steps 
s3://ap-northeast-2.elasticmapreduce/libs/script-runner/script-runner.jar
s3://fc-datapipeline/scripts/mysql.sh

2. Redshift Spectrum 연결하기
create external schema data33_DM (redshift에서 사용할 schema명) from data catalog 
database 'data33' (glue에 정의 되어 있는 database명)
iam_role 'arn:aws:iam::574261620507:role/dataSpectrumRole' 
region 'ap-northeast-2';

create external schema data33_dm from data catalog 
database 'data33'
iam_role 'arn:aws:iam::574261620507:role/dataSpectrumRole' 
region 'ap-northeast-2';

### Redshift를 이용한 realtime 분석 
$ sudo chmod 400 njkwon.pem 
$ ls -al njkwon.pem 
$ ssh -i "njkwon.pem" ec2-user@**********.ap-northeast-1.compute.amazonaws.com
 
2. aws kinesis agent 설치
$sudo yum install aws-kinesis-agent -y
3. s3 에 있는 로그 생성 python 프로그램을 다운 받는다. 
aws s3 cp s3://fc-datapipeline/real/ . --recursive
chmod a+x LogG.py 
4. log 저장위치 폴더 생성
sudo mkdir /var/log/fclog/
5. vi /etc/aws-kinesis/agent.json 
{
  "cloudwatch.emitMetrics": true,
  "kinesis.endpoint": "kinesis.ap-northeast-2.amazonaws.com",    #서비스 엔드포인트
  "firehose.endpoint": "firehose.ap-northeast-2.amazonaws.com",  #서비스 엔드포인트
  "flows": [
    {
      "filePattern": "/var/log/fclog/*.log",  #로그위치
      "kinesisStream": "data33",   #kinesis stream name
      "partitionKeyOption": "RANDOM"
    }
  ]
}
6. kinesis agent 기동 
sudo service aws-kinesis-agent stop
sudo service aws-kinesis-agent start
$sudo LogG.py 100
$more /var/log/fclog/20201230-055135.log
7. kinesis stream 
 
/etc/aws-kinesis/agent.json
{
  "cloudwatch.emitMetrics": true,
  "kinesis.endpoint": "kinesis.ap-northeast-2.amazonaws.com",
  "firehose.endpoint": "firehose.ap-northeast-2.amazonaws.com",
  
  "flows": [
    {
      "filePattern": "/var/log/skcamp/*.log",
      "kinesisStream": "njkwonstream",
      "partitionKeyOption": "RANDOM",
      "dataProcessingOptions": [
         {
            "optionName": "CSVTOJSON",
            "customFieldNames": ["timestamp", "user_id", "event_origin", "event_name", "event_goods_id", "event_shop_id"]
         }
      ]
    }
  ]
}
CREATE OR REPLACE STREAM "ALARM_STREAM" (order_count INTEGER);
CREATE OR REPLACE PUMP "STREAM_PUMP" AS 
    INSERT INTO "ALARM_STREAM"
        SELECT STREAM order_count
        FROM (
            SELECT STREAM COUNT(*) OVER TEN_SECOND_SLIDING_WINDOW AS order_count
            FROM "SOURCE_SQL_STREAM_001"
            WINDOW TEN_SECOND_SLIDING_WINDOW AS (RANGE INTERVAL '10' SECOND PRECEDING)
        )
        WHERE order_count >= 10;
CREATE OR REPLACE STREAM TRIGGER_COUNT_STREAM(
    order_count INTEGER,
    trigger_count INTEGER);
    
CREATE OR REPLACE PUMP trigger_count_pump AS INSERT INTO TRIGGER_COUNT_STREAM
SELECT STREAM order_count, trigger_count
FROM (
    SELECT STREAM order_count, COUNT(*) OVER W1 as trigger_count
    FROM "ALARM_STREAM"
    WINDOW W1 AS (RANGE INTERVAL '1' MINUTE PRECEDING)
)
WHERE trigger_count >= 1;
1. source 관리 - 
- DAG 
- docker images
2. log관리
3. timezone 관리
4. security 관리 

```