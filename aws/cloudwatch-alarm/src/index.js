'use strict'

const ENV = process.env
if (!ENV.webhook) throw new Error('Missing environment variable: webhook')

const webhook = ENV.webhook;

const AWS = require('aws-sdk')
const https = require('https')

const statusColors = {
    ALARM: 'danger',
    INSUFFICIENT_DATA: 'warning',
    OK: 'good'
}

exports.handler = (event, context, callback) => {
    processEvent(event, context, callback);
}

function processEvent(event, context, callback) {
    console.log('Event:', JSON.stringify(event))
    const snsMessage = parseSNSMessage(event.Records[0].Sns.Message)
    const postData = buildSlackMessage(snsMessage)
    post(webhook, postData, callback)
}

function handleResponse(response, callback) {
    const statusCode = response.statusCode
    console.log('Status code:', statusCode)
    let responseBody = ''
    response
        .on('data', chunk => {
            responseBody += chunk
        })
        .on('end', () => {

            console.log('Response:', responseBody)
            if (statusCode >= 200 && statusCode < 300) {
                callback(null, 'Request completed successfully.')
            } else {
                callback(new Error(`Request failed with status code ${statusCode}.`))
            }
        })
}

function post(requestURL, data, callback) {
    const body = JSON.stringify(data)
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Content-Length': Buffer.byteLength(body)
        }
    }

    https
        .request(requestURL, options, response => {
            handleResponse(response, callback)
        })
        .on('error', err => {
            callback(err)
        })
        .end(body)
}

function buildSlackMessage(data) {
    return {
        attachments: [
            {
                fallback: data.AlarmName,
                title: data.AlarmName,
                text: data.AlarmDescription,
                color: statusColors[data.NewStateValue],
                fields: [
                    {
                        title: 'Status',
                        value: data.NewStateValue,
                        short: true
                    }
                ]
            }
        ]
    }
}

function parseSNSMessage(message) {
    console.log('SNS Message:', message)
    return JSON.parse(message)
}
