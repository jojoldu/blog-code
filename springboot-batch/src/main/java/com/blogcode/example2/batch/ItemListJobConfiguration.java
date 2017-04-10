package com.blogcode.example2.batch;

import com.blogcode.example2.domain.Sales;
import com.blogcode.example2.domain.Tax;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 10.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = ItemListJobConfiguration.JOB_NAME)
public class ItemListJobConfiguration {
    public static final String JOB_NAME = "itemListJob";
    private static final String STEP_NAME = "itemListStep";

    private EntityManagerFactory entityManagerFactory;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    public ItemListJobConfiguration(EntityManagerFactory entityManagerFactory,
                                    JobBuilderFactory jobBuilderFactory,
                                    StepBuilderFactory stepBuilderFactory) {
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
                .<Sales, List<Tax>>chunk(2)
                .reader(reader())
                .processor(processor())
                //.writer(writer())
                .writer(writerList())
                .build();
    }

    private ItemProcessor<Sales, List<Tax>> processor() {
        return new ItemListProcessor();
    }

    private JpaPagingItemReader<Sales> reader() {
        JpaPagingItemReader<Sales> reader = new JpaPagingItemReader<>();
        reader.setQueryString("select s from Sales s");
        reader.setEntityManagerFactory(entityManagerFactory);
        return reader;
    }

    private JpaItemWriter<List<Tax>> writer() {
        JpaItemWriter<List<Tax>> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    private JpaItemListWriter<Tax> writerList() {
        JpaItemWriter<Tax> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);

        return new JpaItemListWriter<>(writer);
    }
}
