package com.jojoldu.blogcode.springbootjpa.domain.store;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 02/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(properties = "spring.jpa.properties.hibernate.default_batch_fetch_size=1000")
public class UseBatchSizeStoreServiceTest {
    @Autowired
    StoreRepository storeRepository;

    @Autowired
    StoreService storeService;

    @AfterEach
    public void tearDown() throws Exception {
        storeRepository.deleteAll();
    }

    @Test
    public void USE_Repository_의_BatchSize () throws Exception {
        Store store1 = new Store("서점", "서울시 강남구");
        store1.addProduct(new Product("책1_1", 10000L));
        store1.addProduct(new Product("책1_2", 20000L));
        store1.addEmployee(new Employee("직원1", LocalDate.now()));
        store1.addEmployee(new Employee("직원2", LocalDate.now()));
        storeRepository.save(store1);

        Store store2 = new Store("서점2", "서울시 강남구");
        store2.addProduct(new Product("책2_1", 10000L));
        store2.addProduct(new Product("책2_2", 20000L));
        store2.addEmployee(new Employee("직원2_1", LocalDate.now()));
        store2.addEmployee(new Employee("직원2_2", LocalDate.now()));
        storeRepository.save(store2);

        long size = storeService.find();

        assertThat(size).isEqualTo(60000L);
    }
}
