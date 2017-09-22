package com.jojoldu.springmockspybean.domain.order;

import com.jojoldu.springmockspybean.domain.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 19.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long>{

    Stream<CustomerOrder> findAllByCustomer(Customer customer);
    Optional<CustomerOrder> findTopByCustomer(Customer customer);
}
