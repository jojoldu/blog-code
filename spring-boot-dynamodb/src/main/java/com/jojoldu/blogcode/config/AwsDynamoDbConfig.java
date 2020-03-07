package com.jojoldu.blogcode.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@EnableDynamoDBRepositories(basePackages = {"com.jojoldu.blogcode.dynamodb"})
public class AwsDynamoDbConfig {

    @Bean
    @Primary
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.DEFAULT;
    }

    @Profile({"!local"})
    @Bean
    @Primary
    public AmazonDynamoDB amazonDynamoDB() {
        log.info("Start AWS Amazon DynamoDB Client");
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(InstanceProfileCredentialsProvider.getInstance()) // IAM Role 기반 인증
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }

    @Profile({"local"})
    @Bean(name = "amazonDynamoDB")
    @Primary
    public AmazonDynamoDB localAmazonDynamoDB() {
        log.info("Start Local Amazon DynamoDB Client");
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("test", "test");
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", Regions.AP_NORTHEAST_2.getName()))
                .build();
    }

    @Bean
    @Primary
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
        return new DynamoDBMapper(amazonDynamoDB, config);
    }
}
