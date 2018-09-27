package com.jojoldu.spock.job;

import com.jojoldu.spock.domain.Customer;
import com.jojoldu.spock.domain.CustomerBackup;
import com.jojoldu.spock.domain.CustomerBackupRepository;
import com.jojoldu.spock.domain.CustomerRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

import static com.jojoldu.spock.job.CustomerBackupBatchConfiguration.JOB_NAME;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
public class CustomerBackupBatchConfiguration {
    public static final String JOB_NAME = "customerBackup";
    private static final String STEP_NAME = JOB_NAME+"Step";

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private CustomerRepository customerRepository;
    private CustomerBackupRepository customerBackupRepository;

    public CustomerBackupBatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, CustomerRepository customerRepository, CustomerBackupRepository customerBackupRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.customerRepository = customerRepository;
        this.customerBackupRepository = customerBackupRepository;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .flow(step())
                .end()
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet(backup())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet backup(){
        return (contribution, chunkContext) -> {
            List<Customer> customers = customerRepository.findAll();
            List<CustomerBackup> customerBackups = customers.stream()
                    .map(CustomerBackup::new)
                    .collect(Collectors.toList());

            customerBackupRepository.saveAll(customerBackups);

            return RepeatStatus.FINISHED;
        };
    }
}

