package com.blogcode.batch;

import com.blogcode.domain.Person;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Configuration
public class StepParamConfiguration {
    private static final String STEP_NAME = "stepParam";

    private EntityManagerFactory entityManagerFactory;
    private StepBuilderFactory stepBuilderFactory;

    private ItemProcessor<Person, Person> processor;
    private JpaItemWriter<Person> writer;

    public StepParamConfiguration(EntityManagerFactory entityManagerFactory, StepBuilderFactory stepBuilderFactory, ItemProcessor<Person, Person> processor, JpaItemWriter<Person> writer) {
        this.entityManagerFactory = entityManagerFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.processor = processor;
        this.writer = writer;
    }

    @Bean
    @StepScope
    public Step paramStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Person, Person>chunk(1)
                .reader(paramReader(null))
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public ItemReader<Person> paramReader(@Value("#{jobParameters[firstName]}") String firstName) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstName", firstName);

        JpaPagingItemReader<Person> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select p From Person p where p.firstName=:firstName");
        reader.setParameterValues(paramMap);
        reader.setPageSize(10);

        return reader;
    }
}
