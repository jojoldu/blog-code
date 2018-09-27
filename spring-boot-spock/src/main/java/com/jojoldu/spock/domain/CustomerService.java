package com.jojoldu.spock.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 29.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public String getCustomerName(long id){
        return customerRepository.findById(id).get().getName();
    }

    public void joinEvent(long customerId, long point){
        Customer customer = customerRepository.findById(customerId).get();

        customerRepository.savePoint(customer, point);

        if(customer.isVip()){
            customerRepository.savePoint(customer, point);
        }
    }

}
