package com.jojoldu.blogcode.querydsl.domain.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jojoldu@gmail.com on 22/11/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public void showPayDetailAmount (String orderNo) {
        log.info(">>>>>> findAllByOrderNo");
        orderRepository.findAllByOrderNo(orderNo);
    }
}
