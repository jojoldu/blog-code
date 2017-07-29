package com.blogcode.paging;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 26.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

import com.blogcode.paging.domain.OrderHistory;
import com.blogcode.paging.domain.ShopOrder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = PagingBatchJobConfiguration.JOB_NAME)
public class PagingBatchJobConfiguration {
    public static final String JOB_NAME = "pagingJob";
    private static final String STEP_NAME = "pagingStep";
    private static final int CHUNK_SIZE = 1000;

    private EntityManagerFactory entityManagerFactory;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    public PagingBatchJobConfiguration(EntityManagerFactory entityManagerFactory, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
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
                .<ShopOrder, OrderHistory>chunk(CHUNK_SIZE)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    private JpaPagingItemReader<ShopOrder> reader() {
        JpaPagingItemReader<ShopOrder> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("select o from ShopOrder o join fetch o.customer c where c.id=1");
        reader.setPageSize(CHUNK_SIZE);

        return reader;
    }


    private ItemProcessor<ShopOrder,OrderHistory> processor() {
        return item -> new OrderHistory(item.getId(), item.getCustomer().getName());
    }

    private ItemWriter<OrderHistory> writer() {
        JpaItemWriter<OrderHistory> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
