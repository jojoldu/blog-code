package com.jojoldu.spock.service;

import com.jojoldu.spock.domain.Customer;
import com.jojoldu.spock.domain.CustomerRepository;
import com.jojoldu.spock.service.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 29.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate;

    public CustomerService(CustomerRepository customerRepository, JdbcTemplate jdbcTemplate, RestTemplateBuilder restTemplateBuilder) {
        this.customerRepository = customerRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.restTemplate = restTemplateBuilder.build();
    }

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
