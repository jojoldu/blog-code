package com.blogcode.example3.batch;

import com.blogcode.example3.domain.History;
import com.blogcode.example3.domain.PurchaseOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(name="job.name", havingValue = JpaPagingItemReaderSizeIssueJobConfig.JOB_NAME)
public class JpaPagingItemReaderSizeIssueJobConfig {
    public static final String JOB_NAME = "jpaPagingItemReaderSizeIssueJob";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean(name = JOB_NAME)
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean(name = JOB_NAME+"_step")
    public Step step() {
        return stepBuilderFactory.get(JOB_NAME+"_step")
                .<PurchaseOrder, History>chunk(100)
                .reader(reader())
//                .reader(fixReader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(name = JOB_NAME +"_reader")
    @StepScope
    public JpaPagingItemReader<PurchaseOrder> reader() {
        return new JpaPagingItemReaderBuilder<PurchaseOrder>()
                .name(JOB_NAME +"_reader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select o from PurchaseOrder o")
                .pageSize(10)
                .build();
    }

    @Bean(name = JOB_NAME +"_fixReader")
    @StepScope
    public JpaPagingItemReader<PurchaseOrder> fixReader() {
        return new JpaPagingItemReaderBuilder<PurchaseOrder>()
                .name(JOB_NAME +"_fixReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select o from PurchaseOrder o")
                .pageSize(100)
                .build();
    }

    private ItemProcessor<PurchaseOrder, History> processor() {
        return item -> History.builder()
                .purchaseOrderId(item.getId())
                .productIdList(item.getProductList())
                .build();
    }

    private JpaItemWriter<History> writer() {
        JpaItemWriter<History> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
