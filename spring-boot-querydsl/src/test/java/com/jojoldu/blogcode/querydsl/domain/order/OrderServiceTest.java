package com.jojoldu.blogcode.querydsl.domain.order;

/**
 * Created by jojoldu@gmail.com on 22/11/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Period;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderService orderService;

    @AfterEach
    void after() {
        orderRepository.deleteAllInBatch();
    }

    @Test
    void 조회시에도_더티체킹이_발생한다() throws Exception {
        //given
        String orderNo = "a";
        orderRepository.save(new Order(
                orderNo,
                Period.of(2000,11,27),
                new Member("memNo", "memName"),
                new Pay("code", 1000L, new Pay.PayDetail("d", 900L))));
        //when
        orderService.showPayDetailAmount(orderNo);

        //then
    }
}
