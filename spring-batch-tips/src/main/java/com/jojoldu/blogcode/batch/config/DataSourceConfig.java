package com.jojoldu.blogcode.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/**
 * Created by jojoldu@gmail.com on 22/05/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties
public class DataSourceConfig {

    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;
    private final ObjectProvider<PersistenceUnitManager> persistenceUnitManager;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "main.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "other.datasource")
    public DataSource otherDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Primary
    @Bean("entityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        JpaVendorAdapter jpaVendorAdapter = jpaVendorAdapter();

        return new EntityManagerFactoryBuilder(
                jpaVendorAdapter,
                hibernateProperties.determineHibernateProperties(this.jpaProperties.getProperties(), new HibernateSettings()),
                this.persistenceUnitManager.getIfAvailable());
    }

    private JpaVendorAdapter jpaVendorAdapter() {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(this.jpaProperties.isShowSql());
        adapter.setDatabase(this.jpaProperties.getDatabase());
        adapter.setDatabasePlatform(this.jpaProperties.getDatabasePlatform());
        adapter.setGenerateDdl(this.jpaProperties.isGenerateDdl());
        return adapter;
    }
}
