package com.blogcode.batch;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blogcode.batch.JobConfiguration.JOB_NAME;


@Configuration
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
public class JobConfiguration {

    public static final String JOB_NAME = "job";

    private JobBuilderFactory jobBuilderFactory;

    private Step step;

    public JobConfiguration(JobBuilderFactory jobBuilderFactory, Step step) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.step = step;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step)
                .build();
    }



}
