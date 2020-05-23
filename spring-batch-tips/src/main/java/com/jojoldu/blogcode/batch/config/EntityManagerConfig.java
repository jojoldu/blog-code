package com.jojoldu.blogcode.batch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
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
public class EntityManagerConfig {
    public static final String MAIN_ENTITY_MANAGER_FACTORY = "entityManagerFactory";
    public static final String MAIN_TRANSACTION_MANAGER = "transactionManager";

    public static final String OTHER_ENTITY_MANAGER_FACTORY = "otherEntityManagerFactory";
    public static final String OTHER_TRANSACTION_MANAGER = "otherTransactionManager";

    private final JpaProperties jpaProperties;
    private final ObjectProvider<PersistenceUnitManager> persistenceUnitManager;

    @Primary
    @Bean(name = MAIN_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("dataSource") DataSource dataSource) {

        return this.entityManagerFactoryBuilder()
                .dataSource(dataSource)
                .packages("com.jojoldu.blogcode.batch.domain")
                .persistenceUnit("main")
                .properties(this.jpaProperties.getProperties())
                .build();
    }

    @Bean(name = OTHER_ENTITY_MANAGER_FACTORY)
    public LocalContainerEntityManagerFactoryBean otherEntityManagerFactory(
            @Qualifier("otherDataSource") DataSource dataSource) {

        return this.entityManagerFactoryBuilder()
                .dataSource(dataSource)
                .packages("com.jojoldu.blogcode.batch.domain")
                .persistenceUnit("other")
                .properties(this.jpaProperties.getProperties())
                .build();
    }

    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        JpaVendorAdapter jpaVendorAdapter = jpaVendorAdapter();

        return new EntityManagerFactoryBuilder(
                jpaVendorAdapter,
                this.jpaProperties.getProperties(),
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
