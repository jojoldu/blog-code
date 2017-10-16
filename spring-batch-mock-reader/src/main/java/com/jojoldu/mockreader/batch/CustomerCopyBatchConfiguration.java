package com.jojoldu.mockreader.batch;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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

import static com.jojoldu.mockreader.batch.CustomerCopyBatchConfiguration.JOB_NAME;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@AllArgsConstructor
@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
public class CustomerCopyBatchConfiguration {
    public static final String JOB_NAME = "customerCopyJob";
    private static final String STEP_NAME = "customerCopyStep";
    private static final int CHUNK_SIZE = 100;

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private EntityManagerFactory emf;

    /**
     * Mock 객체로 교체할 수 있게 Autowired 받도록 수정
     */
    private JpaPagingItemReader<Customer> itemReader;

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step())
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Customer, CustomerBackup>chunk(CHUNK_SIZE)
                .reader(itemReader) // 인잭션 받은 itemReader를 사용한다
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Customer> reader(
            @Value("#{jobParameters[mailHost]}") String mailHost) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mailHost", "%@"+mailHost);

        JpaPagingItemReader<Customer> reader = new JpaPagingItemReader<>();
        reader.setQueryString("SELECT c FROM Customer c where c.email like :mailHost");
        reader.setParameterValues(paramMap);
        reader.setPageSize(CHUNK_SIZE);
        reader.setEntityManagerFactory(emf);

        return reader;
    }

    @Bean
    public ItemProcessor<Customer, CustomerBackup> processor() {
        return item -> new CustomerBackup(item.getId());
    }

    @Bean
    public JpaItemWriter<CustomerBackup> writer() {
        final JpaItemWriter<CustomerBackup> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(emf);
        return itemWriter;
    }
}
