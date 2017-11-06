package com.jojoldu.webenv.problem;

import com.jojoldu.webenv.problem.domain.Customer;
import com.jojoldu.webenv.problem.domain.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 11. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@AllArgsConstructor
@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<Customer> findAll(){
        return customerRepository.findAll();
    }
}
