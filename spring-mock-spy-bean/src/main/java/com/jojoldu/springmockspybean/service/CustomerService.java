package com.jojoldu.springmockspybean.service;

import com.jojoldu.springmockspybean.domain.customer.Customer;
import com.jojoldu.springmockspybean.domain.customer.CustomerRepository;
import com.jojoldu.springmockspybean.domain.order.CustomerOrderRepository;
import com.jojoldu.springmockspybean.domain.order.OrderProductMap;
import com.jojoldu.springmockspybean.domain.product.ProductRepository;
import com.jojoldu.springmockspybean.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 18.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
public class CustomerService {

    private HttpSession httpSession;
    private CustomerRepository customerRepository;
    private CustomerOrderRepository customerOrderRepository;

    public CustomerService(HttpSession httpSession, CustomerRepository customerRepository, CustomerOrderRepository customerOrderRepository) {
        this.httpSession = httpSession;
        this.customerRepository = customerRepository;
        this.customerOrderRepository = customerOrderRepository;
    }

    @Transactional(readOnly = true)
    public Customer findByName(String name){
        return customerRepository.findCustomerByName(name)
                .orElseThrow(()-> new ResourceNotFoundException("해당 고객정보는 존재하지 않습니다: " +name));
    }

    @Transactional(readOnly = true)
    public long findMyOrderPriceSum(){
        Customer customer = (Customer)httpSession.getAttribute("loginUser");

        return customerOrderRepository.findAllByCustomer(customer)
                .mapToLong(co -> sum(co.getProducts()))
                .sum();
    }

    private long sum(List<OrderProductMap> orderProducts){
        return orderProducts.stream()
                .mapToLong(m -> m.getProduct().getPrice())
                .sum();
    }

}
