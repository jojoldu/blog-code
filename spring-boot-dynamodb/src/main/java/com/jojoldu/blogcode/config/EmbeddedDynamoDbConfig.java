package com.jojoldu.blogcode.config;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@RequiredArgsConstructor
@Configuration
@Profile("local")
@ConditionalOnProperty(name = "embedded-dynamodb.use", havingValue = "true") // Dynamo가 필요없는 곳에선 의존성이 있어도 임베디드를 실행하지 않도록 한다
public class EmbeddedDynamoDbConfig {

    private DynamoDBProxyServer server;

    @PostConstruct
    public void start() {
        try {
            AwsDynamoDbLocalTestUtils.initSqLite();
            server = ServerRunner.createServerFromCommandLineArgs(new String[]{"-inMemory"});
            server.start();
            log.info("Start Embedded DynamoDB");
        } catch(Exception e) {
            throw new IllegalStateException("Fail Start Embedded DynamoDB", e);
        }
    }

    @PreDestroy
    public void stop() {
        try {
            if (server != null) {
                log.info("Stop Embedded DynamoDB");
                server.stop();
            }
        } catch (Exception e) {
            throw new IllegalStateException("Fail Stop Embedded DynamoDB", e);
        }
    }
}
