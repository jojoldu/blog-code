package com.jojoldu.spock.domain;

import org.springframework.stereotype.Service;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 29.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public String getCustomerName(long id){
        return customerRepository.findOne(id).getName();
    }
}
