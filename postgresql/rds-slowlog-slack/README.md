# PostgreSQL RDS Slow log Slack으로 알람 보내기

## Slow Log 남기기

* log_statement
  * none, ddl, mod, all
  * DML (SELECT, UPDATE, DELETE, INSERT 등) 에 대한 로그만 남기고 싶다면 `mod`를 선택
* log_min_duration_statement
  * 1000 (1초)

### 강제 Slow쿼리

```sql
SELECT pg_sleep(2);
```



## Lambda

### 테스트 환경 구성

[gzip 압축](https://www.multiutil.com/text-to-gzip-compress/)

[https://jsonformatter.org/](https://jsonformatter.org/)

```json
{"messageType":"DATA_MESSAGE","owner":"123456789123","logGroup":"testLogGroup","logStream":"testLogStream","subscriptionFilters":["testFilter"],"logEvents":[{"id":"eventId1","timestamp":1440442987000,"message":"2021-05-07 08:28:23 UTC:127.0.0.1(56644):test@test:[7221]:LOG:  duration: 10045.607 ms  execute <unnamed>: SELECT pg_sleep(10)"}]}
```

### 테스트1

```javascript
var zlib = require('zlib');
exports.handler = function(input, context) {
    var payload = Buffer.from(input.awslogs.data, 'base64');
    zlib.gunzip(payload, function(e, result) {
        if (e) { 
            context.fail(e);
        } else {
            result = JSON.parse(result.toString('ascii'));
            console.log("Event Data:", JSON.stringify(result, null, 2));
            context.succeed();
        }
    });
};
```

### 테스트2 (KST)

```javascript
var zlib = require('zlib');
exports.handler = function(input, context) {
    var payload = Buffer.from(input.awslogs.data, 'base64');
    zlib.gunzip(payload, function(e, result) {
        if (e) { 
            context.fail(e);
        } else {
            result = JSON.parse(result.toString('ascii'));
            const logEvent = result.logEvents[0];
            const datetime = toYyyymmddhhmmss(logEvent.timestamp);
            console.log("KST Time:", datetime);
            console.log("Event Data:", JSON.stringify(result, null, 2));
            context.succeed();
        }
    });
};

// 타임존 UTC -> KST
function toYyyymmddhhmmss(timestamp) {

    if(!timestamp){
        return '';
    }

    function pad2(n) { return n < 10 ? '0' + n : n }

    var kstDate = new Date(timestamp + 32400000);
    return kstDate.getFullYear().toString()
        + '-'+ pad2(kstDate.getMonth() + 1)
        + '-'+ pad2(kstDate.getDate())
        + ' '+ pad2(kstDate.getHours())
        + ':'+ pad2(kstDate.getMinutes())
        + ':'+ pad2(kstDate.getSeconds());
}
```


## 전체

```javascript
const https = require('https');
const zlib = require('zlib');
const SLOW_TIME_LIMIT = 5; // 5초이상일 경우 슬랙 발송
const SLACK_URL = '슬랙주소';

exports.handler = (input, context) => {
    var payload = Buffer.from(input.awslogs.data, 'base64');
    
    zlib.gunzip(payload, async(e, result) => {
        if (e) { 
            context.fail(e);
        } 
        
        result = JSON.parse(result.toString('ascii'));
        const log = toJson(result.logEvents[0]);
        const message = slackMessage(log);
        console.log(`slackMessage = ${JSON.stringify(message)}`);
        
        await exports.postSlack(message, SLACK_URL);
       
    });
};

function toJson(logEvent) {
    const message = logEvent.message;
    const currentTime = toYyyymmddhhmmss(logEvent.timestamp);
    const dateTimeRegex = new RegExp('(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2}) UTC:');
    const matchArray = message.match(dateTimeRegex);
    const removedUtcMessage = message.replace(matchArray[0], '');
    const messages = removedUtcMessage.split(':');
    const timeSplit = messages[5].trim().split(' ');
    
    return {
        "currentTime": currentTime,
        "userIp": messages[0].trim(),
        "user": messages[1].trim(),
        "pid": messages[2].trim().replace('[', '').replace(']', ''),
        "queryTime": (Number(timeSplit[0]) / 1000).toFixed(3),
        "query": messages[6].trim()
    }
}

// 타임존 UTC -> KST
function toYyyymmddhhmmss(timestamp) {

    if(!timestamp){
        return '';
    }

    function pad2(n) { return n < 10 ? '0' + n : n }

    var kstDate = new Date(timestamp + 32400000);
    return kstDate.getFullYear().toString()
        + '-'+ pad2(kstDate.getMonth() + 1)
        + '-'+ pad2(kstDate.getDate())
        + ' '+ pad2(kstDate.getHours())
        + ':'+ pad2(kstDate.getMinutes())
        + ':'+ pad2(kstDate.getSeconds());
}

function slackMessage(messageJson) {
    const title = `[${SLOW_TIME_LIMIT}초이상 실행된 쿼리]`;
    const message = `언제: ${messageJson.currentTime}\n계정: ${messageJson.user}\n계정IP: ${messageJson.userIp}\npid: ${messageJson.pid}\nQueryTime: ${messageJson.queryTime}초\n쿼리: ${messageJson.query}`;
    
    return {
        attachments: [
            {
                color: '#2eb886',
                title: `${title}`,
                fields: [
                    {
                        value: message,
                        short: false
                    }
                ]
            }
        ]
    };
}

exports.postSlack = async (message, slackUrl) => {
    return await request(exports.options(slackUrl), message);
}

exports.options = (slackUrl) => {
    const {host, pathname} = new URL(slackUrl);
    return {
        hostname: host,
        path: pathname,
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    };
}


function request(options, data) {

    return new Promise((resolve, reject) => {
        const req = https.request(options, (res) => {
            res.setEncoding('utf8');
            let responseBody = '';

            res.on('data', (chunk) => {
                responseBody += chunk;
            });

            res.on('end', () => {
                resolve(responseBody);
            });
        });

        req.on('error', (err) => {
            console.error(err);
            reject(err);
        });

        req.write(JSON.stringify(data));
        req.end();
    });
}
```