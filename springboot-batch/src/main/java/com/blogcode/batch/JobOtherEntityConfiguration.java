package com.blogcode.batch;

import com.blogcode.domain.Person;
import com.blogcode.domain.PersonCopy;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 17.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JobOtherEntityConfiguration.JOB_NAME)
public class JobOtherEntityConfiguration {

    public static final String JOB_NAME = "jobOtherEntity";
    private static final String STEP_NAME = "stepOtherEntity";

    private EntityManagerFactory entityManagerFactory;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    public JobOtherEntityConfiguration(EntityManagerFactory entityManagerFactory, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    private Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Person, PersonCopy>chunk(4)
                .reader(reader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Person> reader(@Value("#{jobParameters[lastName]}") String lastName){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("lastName", lastName);

        JpaPagingItemReader<Person> reader = new JpaPagingItemReader<>();
        reader.setQueryString("select p From Person p where p.lastName=:lastName");
        reader.setParameterValues(paramMap);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(1);

        return reader;
    }

    private ItemProcessor<Person, PersonCopy> processor() {
        return new PersonCopyItemProcessor();
    }

    private JpaItemWriter<PersonCopy> writer() {
        JpaItemWriter<PersonCopy> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
