package com.jojoldu.springmockspybean.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.springmockspybean.domain.customer.Customer;
import com.jojoldu.springmockspybean.domain.customer.CustomerRepository;
import com.jojoldu.springmockspybean.domain.order.CustomerOrder;
import com.jojoldu.springmockspybean.domain.order.CustomerOrderRepository;
import com.jojoldu.springmockspybean.domain.order.OrderProductMap;
import com.jojoldu.springmockspybean.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 18.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
public class CustomerService {

    private HttpSession httpSession;
    private ObjectMapper objectMapper;
    private CustomerRepository customerRepository;
    private CustomerOrderRepository customerOrderRepository;

    public CustomerService(HttpSession httpSession, ObjectMapper objectMapper, CustomerRepository customerRepository, CustomerOrderRepository customerOrderRepository) {
        this.httpSession = httpSession;
        this.objectMapper = objectMapper;
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

    @Transactional(readOnly = true)
    public void saveMyOrderProductCountInSession() {
        Customer customer = (Customer)httpSession.getAttribute("loginUser");
        int count = customerOrderRepository.findAllByCustomer(customer)
                .mapToInt(co -> co.getProducts().size())
                .sum();

        httpSession.setAttribute("orderCount", count);
    }

    @Transactional(readOnly = true)
    public String getCustomerJsonString(String requestBody) throws IOException {
        Customer customer = objectMapper.readValue(requestBody, Customer.class);

        return objectMapper.writeValueAsString(customer);
    }

}
