package com.jojoldu.spock.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 27.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface CustomerRepository extends JpaRepository<Customer, Long>{
    void savePoint(Customer customer, long point);
}
