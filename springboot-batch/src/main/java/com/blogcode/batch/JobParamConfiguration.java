package com.blogcode.batch;

import com.blogcode.domain.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

import static com.blogcode.batch.JobParamConfiguration.JOB_NAME;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 17.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
public class JobParamConfiguration {

    public static final String JOB_NAME = "jobParam";
    private static final String STEP_NAME = "stepParam";

     private EntityManagerFactory entityManagerFactory;
     private JobBuilderFactory jobBuilderFactory;
     private StepBuilderFactory stepBuilderFactory;

    public JobParamConfiguration(EntityManagerFactory entityManagerFactory, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
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
    public JpaPagingItemReader<Person> reader(@Value("#{jobParameters[firstName]}") String firstName){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstName", firstName);

        JpaPagingItemReader<Person> reader = new JpaPagingItemReader<>();
        reader.setQueryString("select p From Person p where p.firstName=:firstName");
        reader.setParameterValues(paramMap);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(10);

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
