# 젠킨스에서 파이프라인으로 기존 Job 다루기

## 기존 잡 실행

## 병렬 기존 잡 실행

## 병렬 + 직렬 잡 실행

## 전체 결과에 따른 후 처리

```bash
pipeline {
    agent any
    stages {
        stage('1. Execute Sync Check') {
            parallel {
                stage('1-1. syncCheckOrdBatch') {
                    steps {
                        build job: 'syncCheckOrdBatch',
                                parameters: [
                                        string(name: 'targetDateTime', value: "${targetDateTime}"),
                                        string(name: 'overMinute', value: "${overMinute}"),
                                        string(name: 'version', value: "${version}"),
                                        string(name: 'thresholdCount', value: "${thresholdCount}"),
                                        string(name: 'chunkSize', value: "${chunkSize}")
                                ]
                    }
                }

                stage('1-2. syncCheckTastyBatch') {
                    steps {
                        build job: 'syncCheckTastyBatch',
                                parameters: [
                                        string(name: 'targetDateTime', value: "${targetDateTime}"),
                                        string(name: 'overMinute', value: "${overMinute}"),
                                        string(name: 'version', value: "${version}"),
                                        string(name: 'thresholdCount', value: "${thresholdCount}"),
                                        string(name: 'chunkSize', value: "${chunkSize}")
                                ]
                    }
                }

                stage('1-3. syncCheckPaymentBatch') {
                    steps {
                        build job: 'syncCheckPaymentBatch',
                                parameters: [
                                        string(name: 'targetDateTime', value: "${targetDateTime}"),
                                        string(name: 'overMinute', value: "${overMinute}"),
                                        string(name: 'version', value: "${version}"),
                                        string(name: 'thresholdCount', value: "${thresholdCount}"),
                                        string(name: 'chunkSize', value: "${chunkSize}")
                                ]
                    }
                }
            }
        }
        stage('2. Slack Send') {
            steps {
                slackSend(color: '#34a201', channel: '#alarm', message: "[개발] 전체 Sync 검증 배치가 성공했습니다. targetDateTime=${targetDateTime}, overMinute=${overMinute}")
            }
        }
    }
    post {
        success {
            slackSend(color: '#34a201', channel: '#alarm', message: "[개발] 전체 Sync 검증 배치가 성공했습니다. targetDateTime=${targetDateTime}, overMinute=${overMinute}")
        }
        failure {
            slackSend(color: '#34a201', channel: '#alarm', message: "[개발] 전체 Sync 검증 배치가 실패했습니다. 꼭 확인해주세요! targetDateTime=${targetDateTime}, overMinute=${overMinute}")
        }
    }
}

```