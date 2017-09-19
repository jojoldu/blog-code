package com.jojoldu.springmockspybean.domain.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 18.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface CustomerRepository extends JpaRepository<Customer, Long>{

    Optional<Customer> findCustomerByName(String name);
}
