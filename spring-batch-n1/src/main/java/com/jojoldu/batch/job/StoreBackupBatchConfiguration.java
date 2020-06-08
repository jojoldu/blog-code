package com.jojoldu.batch.job;

import com.jojoldu.batch.job.common.JpaPagingFetchItemReader;
import com.jojoldu.batch.job.domain.Store;
import com.jojoldu.batch.job.domain.StoreHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
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
    private static final String STEP_NAME = JOB_NAME + "Step";

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

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

        return new JpaPagingItemReaderBuilder<Store>()
                .pageSize(chunkSize)
                .parameterValues(parameters)
                .queryString("SELECT s FROM Store s WHERE s.address LIKE :address order by s.id")
                .entityManagerFactory(entityManagerFactory)
                .name("reader")
                .transacted(false)
                .build();
    }

//    @Bean
//    @StepScope
//    public JpaPagingFetchItemReader<Store> reader(
//            @Value("#{jobParameters[address]}") String address) {
//
//        Map<String, Object> parameters = new LinkedHashMap<>();
//        parameters.put("address", address + "%");
//
//        JpaPagingFetchItemReader<Store> reader = new JpaPagingFetchItemReader<>();
//        reader.setEntityManagerFactory(entityManagerFactory);
//        reader.setQueryString("SELECT s FROM Store s WHERE s.address LIKE :address order by s.id");
//        reader.setParameterValues(parameters);
//        reader.setPageSize(chunkSize);
//
//        return reader;
//    }

//    @Bean
//    @StepScope
//    public QuerydslPagingItemReader<Store> reader(@Value("#{jobParameters[address]}") String address){
//        return new QuerydslPagingItemReader<>(entityManagerFactory, chunkSize, queryFactory -> {
//            // 요청 시간 기준으로 만료 기간이 지났지만, "적립" 포인트가 남아있는 경우 조회
//            return queryFactory
//                    .selectFrom(store)
//                    .where(store.address.like(address+"%"));
//        });
//    }

//    @Bean
//    @StepScope
//    public QuerydslCursorItemReader<Store> reader(@Value("#{jobParameters[address]}") String address){
//        return new QuerydslCursorItemReader<>(entityManagerFactory, chunkSize, queryFactory -> {
//            // 요청 시간 기준으로 만료 기간이 지났지만, "적립" 포인트가 남아있는 경우 조회
//            return queryFactory
//                    .selectFrom(store)
//                    .where(store.address.like(address+"%"));
//        });
//    }

//    @Bean
//    @StepScope
//    public HibernateCursorItemReader<Store> reader(@Value("#{jobParameters[address]}") String address) {
//        Map<String, Object> parameters = new LinkedHashMap<>();
//        parameters.put("address", address+"%");
//        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
//
//        HibernateCursorItemReader<Store> reader = new HibernateCursorItemReader<>();
//        reader.setQueryString("FROM Store s WHERE s.address LIKE :address");
//        reader.setParameterValues(parameters);
//        reader.setSessionFactory(sessionFactory);
//        reader.setFetchSize(chunkSize);
//        reader.setUseStatelessSession(false);
//
//        return reader;
//    }

    //    @Bean
//    @StepScope
//    public HibernatePagingItemReader<Store> reader(@Value("#{jobParameters[address]}") String address) {
//        Map<String, Object> parameters = new LinkedHashMap<>();
//        parameters.put("address", address + "%");
//        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
//
//        HibernatePagingItemReader<Store> reader = new HibernatePagingItemReader<>();
//        reader.setQueryString("FROM Store s WHERE s.address LIKE :address");
//        reader.setParameterValues(parameters);
//        reader.setSessionFactory(sessionFactory);
//        reader.setFetchSize(chunkSize);
//        reader.setUseStatelessSession(false);
//
//        return reader;
//    }

    @Value("${chunkSize:2}")
    private int chunkSize;

    private static int count = 0;

    @Bean
    @StepScope
    public ItemProcessor<Store, StoreHistory> processor() {
        return item -> {
//            count++;
//            if (count > 2) {
//                throw new IllegalStateException();
//            }
            return new StoreHistory(item, item.getProducts(), item.getEmployees());
        };
    }


    public JpaItemWriter<StoreHistory> writer() {
        JpaItemWriter<StoreHistory> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}