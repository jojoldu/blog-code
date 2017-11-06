package com.jojoldu.webenv.problem;

import com.jojoldu.webenv.problem.domain.Customer;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 11. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RestController
@AllArgsConstructor
public class ApiController {
    private CustomerService customerService;

    @GetMapping("/customer")
    public List<Customer> findAll(){
        return customerService.findAll();
    }
}
