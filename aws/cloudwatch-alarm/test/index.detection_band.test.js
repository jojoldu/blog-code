process.env.webhook = 'test';

const testObject = require('./messageAlbRequestHigh.json');
const {buildSlackMessage, buildAnomalyDetectionBand} = require("../src");

describe('index.js', () => {

    it('file load', () => {
        console.log(testObject);
    });

    it('buildSlackMessage', () => {
        const slackMessage = buildSlackMessage(testObject);
        console.log(JSON.stringify(slackMessage));
    });

    it('buildAnomalyDetectionBand', () => {
        const message = buildAnomalyDetectionBand(testObject, 1, 1);

        console.log(message);
    });


});
