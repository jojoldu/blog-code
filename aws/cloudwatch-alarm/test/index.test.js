const testObject = require('./message2.json');

process.env.webhook = 'test';
const {buildSlackMessage, toYyyymmddhhmmss, buildThresholdMessage} = require("../src");

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
        const message = buildThresholdMessage(testObject);
        console.log(message);
    });

});
