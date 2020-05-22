package com.jojoldu.blogcode.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by jojoldu@gmail.com on 22/05/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties
public class EntityManagerConfig {
    public static final String MAIN_ENTITY_MANAGER_FACTORY = "entityManagerFactory";
    public static final String MAIN_TRANSACTION_MANAGER = "transactionManager";

    public static final String OTHER_ENTITY_MANAGER_FACTORY = "otherEntityManagerFactory";
    public static final String OTHER_TRANSACTION_MANAGER = "otherTransactionManager";

    private final JpaProperties jpaProperties;
    private final EntityManagerFactoryBuilder entityManagerFactoryBuilder;

    @Primary
    @Bean(name = MAIN_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource) {

        return this.entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.jojoldu.blogcode.batch.domain")
                .persistenceUnit("main")
                .properties(this.jpaProperties.getProperties())
                .build();
    }

    @Primary
    @Bean(name = MAIN_TRANSACTION_MANAGER)
    public PlatformTransactionManager transactionManager(
            @Qualifier("dataSource") DataSource dataSource,
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Primary
    @Bean(name = OTHER_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean otherEntityManagerFactory(
            @Qualifier("otherDataSource") DataSource dataSource) {

        return this.entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.jojoldu.blogcode.batch.domain")
                .persistenceUnit("other")
                .properties(this.jpaProperties.getProperties())
                .build();
    }

    @Primary
    @Bean(name = OTHER_TRANSACTION_MANAGER)
    public PlatformTransactionManager otherTransactionManager(
            @Qualifier("otherDataSource") DataSource dataSource,
            @Qualifier("otherEntityManagerFactory") EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
