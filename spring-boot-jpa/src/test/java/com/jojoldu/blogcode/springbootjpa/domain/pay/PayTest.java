package com.jojoldu.blogcode.springbootjpa.domain.pay;

import org.hibernate.PersistentObjectException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 30/03/2021
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class PayTest {

    @Autowired
    private PayRepository payRepository;

    @AfterEach
    void after() {
        payRepository.deleteAll();
    }

    @Test
    void 동일Embedded사용시_중복이슈_발생() throws Exception {
        // given
        Pay pay1 = new Pay();

        String payName = "testPay";
        pay1.addPayDetail(new PayDetail(payName));

        payRepository.save(pay1);

        //when
        Pay pay2 = new Pay();

        payRepository.save(pay2);
    }

}
