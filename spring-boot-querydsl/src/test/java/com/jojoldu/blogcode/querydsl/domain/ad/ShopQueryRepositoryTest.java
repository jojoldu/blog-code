package com.jojoldu.blogcode.querydsl.domain.ad;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 16/11/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ShopQueryRepositoryTest {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private ShopRepository shopRepository;
    @Autowired private CustomerQueryRepository shopQueryRepository;

    @AfterEach
    void after() {
        shopRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
    }

    @Test
    void querydsl_as없을경우_crossJoin_발생한다() throws Exception {
        //given
        Shop shop = shopRepository.save(new Shop("1", "name"));
        customerRepository.save(new Customer("2", "name", "bizNo", "ceo", shop));

        //when
        shopQueryRepository.crossJoin();
    }

    @Test
    void hibernate_as없을경우_crossJoin_발생한다() throws Exception {
        //given
        Shop shop = shopRepository.save(new Shop("1", "name"));
        customerRepository.save(new Customer("2", "name", "bizNo", "ceo", shop));

        //when
        customerRepository.crossJoin();
    }

    @Test
    void querydsl_as있을경우_crossJoin_발생하지않는다() throws Exception {
        //given
        Shop shop = shopRepository.save(new Shop("1", "name"));
        customerRepository.save(new Customer("2", "name", "bizNo", "ceo", shop));

        //when
        shopQueryRepository.notCrossJoin();
    }

    @Test
    void hibernate_as있을경우_crossJoin_발생하지않는다() throws Exception {
        //given
        Shop shop = shopRepository.save(new Shop("1", "name"));
        customerRepository.save(new Customer("2", "name", "bizNo", "ceo", shop));

        //when
        customerRepository.notCrossJoin();
    }
}
