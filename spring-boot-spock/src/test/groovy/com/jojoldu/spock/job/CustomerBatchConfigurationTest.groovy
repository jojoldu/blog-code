package com.jojoldu.spock.job

import com.jojoldu.spock.domain.Customer
import com.jojoldu.spock.domain.CustomerBackup
import com.jojoldu.spock.domain.CustomerBackupRepository
import com.jojoldu.spock.domain.CustomerRepository
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static org.mockito.BDDMockito.given

/**
 * Created by jojoldu@gmail.com on 2017. 10. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@SpringBootTest
@TestPropertySource(properties = "job.name=customerBackup")
class CustomerBatchConfigurationTest extends Specification {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils

    @Autowired
    CustomerBackupRepository customerBackupRepository

    @MockBean(name = "customerRepository")
    CustomerRepository customerRepository // MockBean

    def cleanup() {
        customerBackupRepository.deleteAll()
    }

    def "Customer 전체 데이터를 CustomerBackup 테이블에 복사한다." () {
        given:
        given(customerRepository.findAll())
                .willReturn(Arrays.asList(
                new Customer("jojoldu", "jojoldu@gmail.com"),
                new Customer("jojoldu1", "jojoldu1@gmail.com")
        ))
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("version", new Date())
                .toJobParameters()
        when:
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters)

        then:
        jobExecution.status == BatchStatus.COMPLETED
        def customerBackups = customerBackupRepository.findAll()
        customerBackups.size() == 2
        customerBackups.get(0).name == "jojoldu"
    }
}
