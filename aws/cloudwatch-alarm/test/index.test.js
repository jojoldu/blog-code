process.env.webhook = 'test';

const testObject = require('./message2.json');
const {createLink} = require("../src");
const {buildSlackMessage, toYyyymmddhhmmss, buildThresholdMessage, exportRegionCode} = require("../src");

describe('index.js', () => {

    it('file load', () => {
        expect(testObject.AlarmName).toBe('RDS-CPUUtilization-high');
    });

    it('buildSlackMessage', () => {
        const slackMessage = buildSlackMessage(testObject);
        console.log(JSON.stringify(slackMessage));
    });

    it('시간 변경', () => {
        const time = '2021-07-14T23:20:50.708+0000';
        const kstTime = toYyyymmddhhmmss(time);
        expect(kstTime).toBe('2021-07-15 17:20:50');
    });

    it('threshold 메시지', () => {
        const message = buildThresholdMessage(testObject, 1, 1);
        console.log(message);
    });

    it('region code 추출', () => {
        const arn = "arn:aws:cloudwatch:ap-northeast-2:123123:alarm:Aurora PostgreSQL CPU 알람 (60%이상시)";
        const result = exportRegionCode(arn);

        expect(result).toBe('ap-northeast-2');
    });

    it('link', () => {
        const result = createLink(testObject);
        expect(result).toBe('https://console.aws.amazon.com/cloudwatch/home?region=ap-northeast-2#alarm:alarmFilter=ANY;name=Aurora%20PostgreSQL%20CPU%20%EC%95%8C%EB%9E%8C%20(60%25%EC%9D%B4%EC%83%81%EC%8B%9C)');
    });

});
