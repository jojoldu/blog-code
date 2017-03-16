package com.blogcode.batch;

import com.blogcode.domain.Person;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
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
public class StepDefaultConfiguration {

    private static final String STEP_NAME = "step";

    private EntityManagerFactory entityManagerFactory;
    private StepBuilderFactory stepBuilderFactory;

    public StepDefaultConfiguration(EntityManagerFactory entityManagerFactory, StepBuilderFactory stepBuilderFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Person, Person>chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public ItemProcessor<Person, Person> processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public ItemReader<Person> reader() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstName", "Donguk");

        JpaPagingItemReader<Person> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select p From Person p where p.firstName=:firstName");
        reader.setParameterValues(paramMap);
        reader.setPageSize(10);

        return reader;
    }

    @Bean
    public JpaItemWriter<Person> writer() {
        JpaItemWriter<Person> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
