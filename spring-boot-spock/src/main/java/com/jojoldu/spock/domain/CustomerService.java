package com.jojoldu.spock.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 29.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final JdbcTemplate jdbcTemplate;

    public String getCustomerName(long id){
        return customerRepository.findById(id)
                .map(Customer::getName)
                .orElse("");
    }

    public void joinEvent(long customerId, long point){
        Customer customer = customerRepository.findById(customerId).get();

        customerRepository.save(customer);

        if(customer.isVip()){
            customerRepository.save(customer);
        }
    }

    public List<String> execute(String sql) {
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(String.class));
    }

}
