package com.blogcode;

import com.blogcode.batch.JobParamConfiguration;
import com.blogcode.domain.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {"job.name=" + JobParamConfiguration.JOB_NAME})
public class JobParamTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private PersonRepository personRepository;

	@Test
	public void param_test() throws Exception{
		JobParameters jobParameters =
				new JobParametersBuilder().addString("firstName", "Sungsu").toJobParameters();

		assertNull(personRepository.findByFirstName("SUNGSU"));

		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

		assertThat(jobExecution.getStatus(), is(BatchStatus.COMPLETED));
		assertThat(personRepository.findByFirstName("SUNGSU").getFirstName(), is("SUNGSU"));
	}

}
