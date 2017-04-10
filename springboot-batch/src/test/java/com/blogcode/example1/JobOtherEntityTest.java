package com.blogcode.example1;

import com.blogcode.example1.batch.JobOtherEntityConfiguration;
import com.blogcode.example1.domain.PersonCopyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"job.name=" + JobOtherEntityConfiguration.JOB_NAME})
public class JobOtherEntityTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PersonCopyRepository personCopyRepository;

    @Test
    public void other_entity_test() throws Exception{
        JobParameters jobParameters =
                new JobParametersBuilder().addString("lastName", "Lee").toJobParameters();

        assertThat(personCopyRepository.findByLastName("Lee").size(), is(0));
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(jobExecution.getStatus(), is(BatchStatus.COMPLETED));
        assertThat(personCopyRepository.findByLastName("Lee").get(0).getFirstName(), is("Donguk"));
    }
}
