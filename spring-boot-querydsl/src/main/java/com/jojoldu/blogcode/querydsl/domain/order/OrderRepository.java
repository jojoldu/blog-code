package com.jojoldu.blogcode.querydsl.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 22/11/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderNo(String orderNo);
}
