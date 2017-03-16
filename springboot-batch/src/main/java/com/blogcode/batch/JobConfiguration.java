package com.blogcode.batch;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    private static final String JOB_NAME = "job";

    private JobBuilderFactory jobBuilderFactory;

    private Step step;
    private Step paramStep;

    public JobConfiguration(JobBuilderFactory jobBuilderFactory, Step step, Step paramStep) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.step = step;
        this.paramStep = paramStep;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(step)
                .build();
    }

    @Bean
    public Job paramJob() {
        return jobBuilderFactory.get("paramJob")
                .start(paramStep)
                .build();
    }

}
