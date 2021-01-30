# Slack Webhook API 생성하기

![slack1](./images/slack1.png)

![slack2](./images/slack2.png)

![slack3](./images/slack3.png)

![slack4](./images/slack4.png)

![slack5](./images/slack5.png)

![slack6](./images/slack6.png)

![slack7](./images/slack7.png)

```js
{
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
}
```