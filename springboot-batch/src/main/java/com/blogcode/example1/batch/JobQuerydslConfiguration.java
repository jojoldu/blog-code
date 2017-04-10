package com.blogcode.example1.batch;

import com.blogcode.example1.domain.Person;
import com.blogcode.step.QuerydslPagingItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 21.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JobQuerydslConfiguration.JOB_NAME)
public class JobQuerydslConfiguration {
    public static final String JOB_NAME = "jobOtherEntity";
    private static final String STEP_NAME = "stepOtherEntity";

    private EntityManagerFactory entityManagerFactory;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    public JobQuerydslConfiguration(EntityManagerFactory entityManagerFactory, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }

    private Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Person, Person>chunk(1)
                .reader(reader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public QuerydslPagingItemReader<Person> reader(@Value("#{jobParameters[firstName]}") String firstName){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstName", firstName);
        QuerydslPagingItemReader reader = new QuerydslPagingItemReader();


        return reader;
    }

    private ItemProcessor<Person, Person> processor() {
        return new PersonItemProcessor();
    }

    private JpaItemWriter<Person> writer() {
        JpaItemWriter<Person> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
