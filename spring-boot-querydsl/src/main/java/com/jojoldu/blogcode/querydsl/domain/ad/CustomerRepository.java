package com.jojoldu.blogcode.querydsl.domain.ad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 11/08/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c " +
            "FROM Customer c " +
            "WHERE c.customerNo > c.shop.shopNo")
    List<Customer> crossJoin();

    @Query("SELECT c " +
            "FROM Customer c " +
            "JOIN c.shop s " +
            "WHERE c.customerNo > s.shopNo")
    List<Customer> notCrossJoin();
}
