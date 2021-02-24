const app = require('./app');

test('request용 options 생성', () => {
    const {hostname, path, method} = app.options('https://hooks.slack.com/services/test1/test2/test3');

    expect(hostname).toBe('hooks.slack.com');
    expect(path).toBe('/services/test1/test2/test3');
    expect(method).toBe('POST');
});

describe('dataType에 맞게 메세지가 발행된다', () => {
    test('ReviewCreatedFeedEventBean 이면 리뷰 생성이다.', () => {
        const eventBody = {'dataType': 'ReviewCreatedFeedEventBean'};
        const result = app.convert(eventBody);

        expect(result).toBe('리뷰가 생성되었습니다.');
    });

    test('RevisionAddedToReviewFeedEventBean 이면 리뷰에 신규 코드 반영이다.', () => {
        const eventBody = {'dataType': 'RevisionAddedToReviewFeedEventBean'};
        const result = app.convert(eventBody);

        expect(result).toBe('리뷰에 새 코드가 반영되었습니다.');
    });

    test('ReviewStateChangedFeedEventBean에 newState가 1이면 리뷰 종료', () => {
        const eventBody = {
            'dataType': 'ReviewStateChangedFeedEventBean',
            'data': {
                newState: 1
            }
        };
        const result = app.convert(eventBody);

        expect(result).toBe('리뷰가 종료되었습니다.');
    });

    test('ReviewStateChangedFeedEventBean에 newState가 0 이면 리뷰 재오픈', () => {
        const eventBody = {
            'dataType': 'ReviewStateChangedFeedEventBean',
            'data': {
                newState: 0
            }
        };
        const result = app.convert(eventBody);

        expect(result).toBe('리뷰가 다시 오픈되었습니다.');
    });

    test('나머지는 모두 리뷰내용 변경이다.', () => {
        const eventBody = {'dataType': ''};
        const result = app.convert(eventBody);

        expect(result).toBe('리뷰에 변경 사항이 있습니다.');
    });
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



