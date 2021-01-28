# Slack 웹훅으로 Upsource 코드리뷰 알람 받기


[Integration with slack is not working via webhook](https://youtrack.jetbrains.com/issue/UP-10213)

API

```bash
http://본인들 Upsoure 도메인/~api_doc/index.html#webhooks
```

```bash
POST /my-webhook HTTP/1.1
Host: example.com
Content-Type: application/json; charset=UTF-8
Content-Length: XXX
Connection: close
User-Agent: Jakarta Commons-HttpClient/3.1
X-Request-Id: c8a5b163-508f-42b5-939a-e11b02caf72e

{
    "majorVersion": 3,
    "minorVersion": 0,
    "projectId": "demo-project",
    "dataType": "NewRevisionEventBean",
    "data": {
        "revisionId": "c1f4de8e6c5aca9b5615fa6656e1f26e4f26d0d0",
        "branches": [
        "master"
        ],
        "author": "John Doe <john.doe@mycompany.com>",
        "message": "#DSH-325 extract directives describes to top level\n",
        "date": 1454432013000
    }
}
```

```bash
http://본인들 Upsoure 도메인/~api_doc/integration/index.html
```

![upsource-api1](./images/upsource-api1.png)

![upsource-api2](./images/upsource-api2.png)

![upsource-api3](./images/upsource-api3.png)
