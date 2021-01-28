const app = require('./app');

test('request용 options 생성', () => {
    const {hostname, path, method} = app.options('https://hooks.slack.com/services/test1/test2/test3');

    expect(hostname).toBe('hooks.slack.com');
    expect(path).toBe('/services/test1/test2/test3');
    expect(method).toBe('POST');
});

test('request용 message 생성', () => {
    const projectId = "settler";
    const reviewId = "ST-501";
    const user = "jojoldu";
    const mockEvent =  {
        "body": {
            "projectId": projectId,
            "dataType": "ReviewCreatedFeedEventBean",
            "data": {
                "base": {
                    "reviewId": reviewId,
                    "actor": {
                        "userName": user
                    }
                }
            }
        }
    };

    const upsourceHost = 'http://upsource.test.com';
    const {attachments} = app.message(mockEvent.body, upsourceHost);

    expect(attachments.length).toBe(1);
    expect(attachments[0].title).toBe('리뷰가 생성되었습니다.');
    expect(attachments[0].fields[0].value).toBe(`<${upsourceHost}/${projectId}/review/${reviewId}|${reviewId}> Report By ${user}`);
});


