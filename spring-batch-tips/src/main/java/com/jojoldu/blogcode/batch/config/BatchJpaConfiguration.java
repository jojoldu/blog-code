package com.jojoldu.blogcode.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jojoldu@gmail.com on 24/05/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties({JpaProperties.class, HibernateProperties.class})
public class BatchJpaConfiguration {
    public static final String MAIN_ENTITY_MANAGER_FACTORY = "entityManagerFactory";
    public static final String MAIN_TRANSACTION_MANAGER = "transactionManager";

    public static final String OTHER_ENTITY_MANAGER_FACTORY = "otherEntityManagerFactory";
    public static final String OTHER_TRANSACTION_MANAGER = "otherTransactionManager";

    private final JpaProperties properties;
    private final HibernateProperties hibernateProperties;
    private final ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders;
    private final EntityManagerFactoryBuilder entityManagerFactoryBuilder;

    @Primary
    @Bean(name = MAIN_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource) {

        return new EntityManagerCreator(properties, hibernateProperties, metadataProviders, entityManagerFactoryBuilder, dataSource)
                .entityManagerFactory("com.jojoldu.blogcode.batch.domain", "main");
    }

    @Bean(name = OTHER_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean otherEntityManagerFactory(
            @Qualifier("otherDataSource") DataSource dataSource) {

        return new EntityManagerCreator(properties, hibernateProperties, metadataProviders, entityManagerFactoryBuilder, dataSource)
                .entityManagerFactory("com.jojoldu.blogcode.batch.domain", "other");
    }
}
