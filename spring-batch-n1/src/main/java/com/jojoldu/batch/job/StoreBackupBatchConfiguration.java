package com.jojoldu.batch.job;

import com.jojoldu.batch.job.domain.Store;
import com.jojoldu.batch.job.domain.StoreHistory;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.jojoldu.batch.job.StoreBackupBatchConfiguration.JOB_NAME;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 27.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
public class StoreBackupBatchConfiguration {

    public static final String JOB_NAME = "storeBackupBatch";
    private static final String STEP_NAME = JOB_NAME+"Step";

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Value("${chunkSize:1000}")
    private int chunkSize;

    private static String ADDRESS_PARAM = null;

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean
    @JobScope
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Store, StoreHistory>chunk(chunkSize)
                .reader(reader(ADDRESS_PARAM))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Store> reader (
            @Value("#{jobParameters[address]}") String address) {

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("address", address+"%");

        JpaPagingItemReader<Store> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT s FROM Store s WHERE s.address LIKE :address order by s.id");
        reader.setParameterValues(parameters);
        reader.setPageSize(chunkSize);
        return reader;
    }

//    @Bean
//    @StepScope
//    public HibernateCursorItemReader<Store> reader(
//            @Value("#{jobParameters[address]}") String address) {
//
//        Map<String, Object> parameters = new LinkedHashMap<>();
//        parameters.put("address", address+"%");
//        final SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
//
//        HibernateCursorItemReader<Store> itemReader = new HibernateCursorItemReader<>();
//        itemReader.setQueryString("FROM Store s WHERE s.address LIKE :address");
//        itemReader.setParameterValues(parameters);
//        itemReader.setSessionFactory(sessionFactory);
//
//        return itemReader;
//    }

    @Bean
    @StepScope
    public ItemProcessor<Store, StoreHistory> processor() {
        return item -> new StoreHistory(item, item.getProducts(), item.getEmployees());
    }

    public JpaItemWriter<StoreHistory> writer() {
        JpaItemWriter<StoreHistory> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}
