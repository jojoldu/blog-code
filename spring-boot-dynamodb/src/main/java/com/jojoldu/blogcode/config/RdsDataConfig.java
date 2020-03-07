package com.jojoldu.blogcode.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.jojoldu.blogcode.config.RdsDataConfig.RDS_DOMAIN_PACKAGE;

/**
 * DynamoDB Scan 과 JPA Scan이 겹쳐서 Repository가 2중 등록 되려는 현상이 발생
 * Repository간 서로 Scan 범위를 분리한다
 */
@RequiredArgsConstructor
@Configuration
@EnableJpaRepositories(basePackages = {RDS_DOMAIN_PACKAGE})
@EnableConfigurationProperties({JpaProperties.class, HibernateProperties.class})
public class RdsDataConfig {

    static final String RDS_DOMAIN_PACKAGE = "com.jojoldu.blogcode.jpa";

}
