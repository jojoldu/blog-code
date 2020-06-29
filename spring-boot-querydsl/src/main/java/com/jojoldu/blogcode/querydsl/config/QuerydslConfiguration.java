package com.jojoldu.blogcode.querydsl.config;

import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointEvent;
import com.jojoldu.blogcode.querydsl.domain.student.Student;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import com.querydsl.sql.types.DateTimeType;
import com.querydsl.sql.types.LocalDateType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pl.exsio.querydsl.entityql.QExporter;
import pl.exsio.querydsl.entityql.config.EntityQlQueryFactory;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;

import static pl.exsio.querydsl.entityql.EntityQL.qEntity;

/**
 * Created by jojoldu@gmail.com on 21/06/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Slf4j
@RequiredArgsConstructor
@Configuration
public class QuerydslConfiguration {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    @Profile("local")
    public SQLTemplates h2SqlTemplates() {
        return new H2Templates();
    }

    @Bean
    @Profile("!local")
    public SQLTemplates mySqlTemplates() {
        return new MySQLTemplates();
    }

    @Bean
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource, SQLTemplates sqlTemplates) {
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(sqlTemplates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        configuration.register(new DateTimeType());
        configuration.register(new LocalDateType());

        return new EntityQlQueryFactory(configuration, dataSource)
                .registerEnumsByName("com.jojoldu.blogcode.querydsl.domain");
    }

    @Bean
    public QExporter entityQlScanner() {
        String fileNamePattern = "E%s.java"; // file/class name pattern
        String packageName = "com.jojoldu.blogcode.querydsl.domain"; //package of the generated class
        String destinationPath = "src/main/generated"; //physical location of resulting *.java file

        QExporter qExporter = new QExporter();
        try {
            qExporter.export(qEntity(Book.class), fileNamePattern, packageName, destinationPath);
            qExporter.export(qEntity(PointEvent.class), fileNamePattern, packageName, destinationPath);
            return qExporter;
        } catch (Exception e) {
            log.error("qExporter Scan Exception", e);
            throw new IllegalStateException("qExporter Scan Exception", e);
        }
    }
}
