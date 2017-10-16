package com.jojoldu.mockreader

import com.jojoldu.mockreader.batch.Customer
import com.jojoldu.mockreader.batch.CustomerRepository
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

/**
 * Created by jojoldu@gmail.com on 2017. 10. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@TestPropertySource(properties = "job.name=customerCopyJob")
@SpringBootTest
class CustomerCopyBatchRealTest extends Specification {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils

    @Autowired
    CustomerRepository customerRepository

    def "[Real] gmail.com을 가진 Customer 데이터를 CustomerBackup으로 복사한다" () {
        given:
        customerRepository.save(new Customer("jojoldu", "jojoldu@gmail.com"))
        customerRepository.save(new Customer("jojoldu1", "jojoldu@naver.com"))

        JobParametersBuilder builder = new JobParametersBuilder()
        builder.addString("mailHost", "gmail.com")

        when:
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(builder.toJobParameters())

        then:
        jobExecution.status == BatchStatus.COMPLETED
    }
}
