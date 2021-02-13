package com.jojoldu.batch.job.n1

import com.jojoldu.batch.job.domain.*
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import java.time.LocalDate


@SpringBootTest
@TestPropertySource(properties = "job.name=n1JpaPagingJob2")
class N1JpaCursorJobConfigTest extends Specification {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils

    @Autowired
    StoreRepository storeRepository

    @Autowired
    StoreHistoryRepository storeHistoryRepository

    def "Store 정보가 StoreHistory로 복사된다" () {
        given:
        Store store1 = new Store("서점", "서울시 강남구")
        store1.addProduct(new Product("책1_1", 10000L))
        store1.addProduct(new Product("책1_2", 20000L))
        storeRepository.save(store1)

        Store store2 = new Store("서점2", "서울시 강남구")
        store2.addProduct(new Product("책2_1", 10000L))
        store2.addProduct(new Product("책2_2", 20000L))
        storeRepository.save(store2)

        Store store3 = new Store("서점3", "서울시 강남구")
        store3.addProduct(new Product("책3_1", 10000L))
        store3.addProduct(new Product("책3_2", 20000L))
        storeRepository.save(store3)

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("address", "서울")
                .toJobParameters()
        when:
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters)

        then:
        jobExecution.status == BatchStatus.COMPLETED
    }
}
