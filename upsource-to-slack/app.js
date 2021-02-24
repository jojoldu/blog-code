const https = require('https');
const UPSOURCE_HOST = 'http://upsource.test.com'; // 본인 Upsource 주소

exports.handler = async (event) => {
    console.log('event: '+JSON.stringify(event));

    const webhook = event.params.querystring.webhook;
    const message = exports.message(event.body, UPSOURCE_HOST);

    return await exports.postSlack(message, webhook);
};

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

exports.message = (eventBody, upsourceHost) => {
    const reviewId = eventBody.data.base.reviewId;
    const title = exports.convert(eventBody);
    const user = eventBody.data.base.actor.userName;
    const projectId = eventBody.projectId;

    return {
        attachments: [
            {
                color: '#2eb886',
                title: `${title}`,
                fields: [
                    {
                        value: `<${upsourceHost}/${projectId}/review/${reviewId}|${reviewId}> Report By ${user}`,
                        short: false
                    }
                ]
            }
        ]
    };
}

exports.convert = (eventBody) => {
    const dataType = eventBody.dataType;
    console.log(`[dataType] = ${dataType}`)

    if(dataType === 'ReviewCreatedFeedEventBean') {
        return '리뷰가 생성되었습니다.';
    } else if (dataType === 'RevisionAddedToReviewFeedEventBean') {
        return '리뷰에 새 코드가 반영되었습니다.';
    } else if (dataType === 'ReviewStateChangedFeedEventBean') {
        const newState = eventBody.data.newState;
        if(newState === 1) {
            return '리뷰가 종료되었습니다.';
        } else {
            return '리뷰가 다시 오픈되었습니다.';
        }
    } else {
        return '리뷰에 변경 사항이 있습니다.';
    }
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

