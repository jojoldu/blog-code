package com.blogcode.jobparameter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2018. 9. 15.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"job.name="+UniqueParameterJobConfiguration.JOB_NAME})
public class UniqueParameterJobConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void A_실패테스트() throws Exception {
        //given
        String requestDate = "2020-03-09";
        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addString("requestDate", requestDate);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(builder.toJobParameters());

        //then
        assertThat(jobExecution.getStatus(), is(BatchStatus.FAILED));
    }

    @Test
    public void B_성공테스트() throws Exception {
        //given
        String requestDate = "2020-03-10";
        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addString("requestDate", requestDate);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(builder.toJobParameters());

        //then
        assertThat(jobExecution.getStatus(), is(BatchStatus.COMPLETED));
    }
}
