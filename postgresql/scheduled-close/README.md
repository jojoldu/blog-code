# CloudWatch를 이용한 RDS 스케줄링 종료

```javascript
const AWS = require('aws-sdk');

exports.handler = (event, context, callback) => {
    console.log(JSON.stringify(event));
    const instances = event.instances;
    instances.forEach((instance) => {
        switch (event.action) {
            case 'stop':
                console.log(`Stopping instance '${instance}'...`);
                stopInstance(instance);
                break;
            case 'start':
                console.log(`Starting instance '${instance}'...`);
                startInstance(instance);
                break;
            default:
                throw `Invalid action ${event.action}`;
        }
    })
    callback(null, 'Done!');
};

function stopInstance (instanceId) {
  var rds = new AWS.RDS();
  var today = new Date();
  var params = {
    DBInstanceIdentifier: instanceId,
    DBSnapshotIdentifier: `${instanceId}-${today.getDate()}-${today.getMonth() + 1}-${today.getFullYear()}-${today.getTime()}`
  };
  rds.stopDBInstance(params, (err, data) => {
    if (err) console.log(err, err.stack); // an error occurred
    else console.log(data);           // successful response
  });
};


function startInstance (instanceId) {
  var rds = new AWS.RDS();
  var params = {
    DBInstanceIdentifier: instanceId
  };
  rds.startDBInstance(params, function (err, data) {
    if (err) console.log(err, err.stack); // an error occurred
    else console.log(data);           // successful response
  });
};
```

## Cloud Watch 규칙 추가

```bash
cron(Minutes Hours Day-of-month Month Day-of-week Year)
```

* day-of-month 또는 day-of-week 값 중 하나는 반드시 물음표(?)여야 합니다.

[UTC-KST 확인](https://savvytime.com/converter/kst-to-utc)


종료

```bash
0 10 ? * MON-FRI *
```

시작

```bash
0 12 ? * MON-FRI *
```

