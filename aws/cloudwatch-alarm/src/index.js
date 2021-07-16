const ENV = process.env
if (!ENV.webhook) throw new Error('Missing environment variable: webhook')

const webhook = ENV.webhook;
const https = require('https')

const statusColorsAndMessage = {
    ALARM: {"color": "danger", "message":"위험"},
    INSUFFICIENT_DATA: {"color": "warning", "message":"데이터 부족"},
    OK: {"color": "good", "message":"정상"}
}

const comparisonOperator = {
    "GreaterThanOrEqualToThreshold": ">=",
    "GreaterThanThreshold": ">",
    "LowerThanOrEqualToThreshold": "<=",
    "LessThanThreshold": "<",
}

exports.handler = async (event) => {
    await exports.processEvent(event);
}

exports.processEvent = async (event) => {
    console.log('Event:', JSON.stringify(event))
    const snsMessage = event.Records[0].Sns.Message;
    console.log('SNS Message:', snsMessage);
    const postData = exports.buildSlackMessage(JSON.parse(snsMessage))
    await exports.postSlack(postData, webhook);
}

exports.buildSlackMessage = (data) => {
    const newState = statusColorsAndMessage[data.NewStateValue];
    const oldState = statusColorsAndMessage[data.OldStateValue];
    const executeTime = exports.toYyyymmddhhmmss(data.StateChangeTime);
    const description = data.AlarmDescription;
    const cause = exports.getCause(data);

    return {
        attachments: [
            {
                title: `[${data.AlarmName}]`,
                color: newState.color,
                fields: [
                    {
                        title: '언제',
                        value: executeTime
                    },
                    {
                        title: '설명',
                        value: description
                    },
                    {
                        title: '원인',
                        value: cause
                    },
                    {
                        title: '이전 상태',
                        value: oldState.message,
                        short: true
                    },
                    {
                        title: '현재 상태',
                        value: `*${newState.message}*`,
                        short: true
                    },
                    {
                        title: '바로가기',
                        value: exports.createLink(data)
                    }
                ]
            }
        ]
    }
}

exports.createLink = (data) => {
    return `https://console.aws.amazon.com/cloudwatch/home?region=${exports.exportRegionCode(data.AlarmArn)}#alarm:alarmFilter=ANY;name=${encodeURIComponent(data.AlarmName)}`;
}

exports.exportRegionCode = (arn) => {
    return  arn.replace("arn:aws:cloudwatch:", "").split(":")[0];
}

exports.getCause = (data) => {
    const trigger = data.Trigger;
    const evaluationPeriods = trigger.EvaluationPeriods;
    const minutes = Math.floor(trigger.Period / 60);

    if(data.Trigger.Metrics) {
        return exports.buildAnomalyDetectionBand(data, evaluationPeriods, minutes);
    }

    return exports.buildThresholdMessage(data, evaluationPeriods, minutes);

}

exports.buildAnomalyDetectionBand = (data, evaluationPeriods, minutes) => {
    const metrics = data.Trigger.Metrics;
    const metric = metrics.find(metric => metric.Id === 'm1').MetricStat.Metric.MetricName;
    const expression = metrics.find(metric => metric.Id === 'ad1').Expression;
    const width = expression.split(',')[1].replace(')', '').trim();

    return `${evaluationPeriods * minutes} 분 동안 ${evaluationPeriods} 회 ${metric} 지표가 범위(약 ${width}배)를 벗어났습니다.`;
}

exports.buildThresholdMessage = (data, evaluationPeriods, minutes) => {
    const trigger = data.Trigger;
    const threshold = trigger.Threshold;
    const metric = trigger.MetricName;
    const operator = comparisonOperator[trigger.ComparisonOperator];

    return `${evaluationPeriods * minutes} 분 동안 ${evaluationPeriods} 회 ${metric} ${operator} ${threshold}`;
}

// 타임존 UTC -> KST
exports.toYyyymmddhhmmss = (timeString) => {

    if(!timeString){
        return '';
    }

    const kstDate = new Date(new Date(timeString).getTime() + 32400000);

    function pad2(n) { return n < 10 ? '0' + n : n }

    return kstDate.getFullYear().toString()
        + '-'+ pad2(kstDate.getMonth() + 1)
        + '-'+ pad2(kstDate.getDate())
        + ' '+ pad2(kstDate.getHours())
        + ':'+ pad2(kstDate.getMinutes())
        + ':'+ pad2(kstDate.getSeconds());
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

