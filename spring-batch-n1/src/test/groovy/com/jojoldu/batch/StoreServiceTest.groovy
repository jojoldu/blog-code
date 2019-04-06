package com.jojoldu.batch

import com.jojoldu.batch.job.domain.Employee
import com.jojoldu.batch.job.domain.Product
import com.jojoldu.batch.job.domain.Store
import com.jojoldu.batch.job.domain.StoreRepository
import com.jojoldu.batch.job.domain.StoreService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDate


@SpringBootTest
class StoreServiceTest extends Specification{
    @Autowired
    StoreRepository storeRepository

    @Autowired
    StoreService storeService

    @Test
    void "Repository 의 BatchSize" () {
        given:
        Store store1 = new Store("서점", "서울시 강남구")
        store1.addProduct(new Product("책1_1", 10000L))
        store1.addProduct(new Product("책1_2", 20000L))
        store1.addEmployee(new Employee("직원1", LocalDate.now()))
        store1.addEmployee(new Employee("직원2", LocalDate.now()))
        storeRepository.save(store1)

        Store store2 = new Store("서점2", "서울시 강남구")
        store2.addProduct(new Product("책2_1", 10000L))
        store2.addProduct(new Product("책2_2", 20000L))
        store2.addEmployee(new Employee("직원2_1", LocalDate.now()))
        store2.addEmployee(new Employee("직원2_2", LocalDate.now()))
        storeRepository.save(store2)

        when:
        long size = storeService.find()

        then:
        size == 60000L
    }
}
