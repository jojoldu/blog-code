package com.jojoldu.mockreader

import com.jojoldu.mockreader.batch.Customer
import com.jojoldu.mockreader.batch.CustomerBackup
import com.jojoldu.mockreader.batch.CustomerBackupRepository
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import static org.mockito.BDDMockito.given

/**
 * Created by jojoldu@gmail.com on 2017. 10. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@TestPropertySource(properties = "job.name=customerCopyJob")
@SpringBootTest
class CustomerCopyBatchMockTest extends Specification {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils

    @Autowired
    CustomerBackupRepository customerBackupRepository

    @MockBean(name = "itemReader")
    JpaPagingItemReader<Customer> mockItemReader

    def "[Mock] gmail.com을 가진 Customer 데이터를 CustomerBackup으로 복사한다" () {
        given:
        given(mockItemReader.read())
                .willReturn(new Customer(1L), new Customer(2L), null)

        JobParametersBuilder builder = new JobParametersBuilder()
        builder.addString("mailHost", "gmail.com")

        when:
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(builder.toJobParameters())

        then:
        List<CustomerBackup> customerBackups = customerBackupRepository.findAll()
        jobExecution.status == BatchStatus.COMPLETED
        customerBackups.size() == 2
    }
}
