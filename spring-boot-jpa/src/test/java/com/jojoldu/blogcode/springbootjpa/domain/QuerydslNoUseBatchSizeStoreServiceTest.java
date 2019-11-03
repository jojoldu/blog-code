package com.jojoldu.blogcode.springbootjpa.domain;

import com.jojoldu.blogcode.springbootjpa.querydsl.store.StoreQuerydslRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 02/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuerydslNoUseBatchSizeStoreServiceTest {
    @Autowired
    StoreQuerydslRepository storeQuerydslRepository;

    @Autowired StoreService storeService;

    @After
    public void tearDown() throws Exception {
        storeQuerydslRepository.deleteAll();
    }

    @Test
    public void querydsl의_멀티페치조인() throws Exception {
        Store store1 = new Store("서점", "서울시 강남구");
        store1.addProduct(new Product("책1_1", 10000L));
        store1.addProduct(new Product("책1_2", 20000L));
        store1.addEmployee(new Employee("직원1", LocalDate.now()));
        store1.addEmployee(new Employee("직원2", LocalDate.now()));
        storeQuerydslRepository.save(store1);

        Store store2 = new Store("서점2", "서울시 강남구");
        store2.addProduct(new Product("책2_1", 10000L));
        store2.addProduct(new Product("책2_2", 20000L));
        store2.addEmployee(new Employee("직원2_1", LocalDate.now()));
        store2.addEmployee(new Employee("직원2_2", LocalDate.now()));
        storeQuerydslRepository.save(store2);

        long size = storeService.findByQuerydsl();

        assertThat(size).isEqualTo(60000L);
    }
}
