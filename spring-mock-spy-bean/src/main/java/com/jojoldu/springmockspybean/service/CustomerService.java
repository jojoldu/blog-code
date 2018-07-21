package com.jojoldu.springmockspybean.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.springmockspybean.domain.customer.Customer;
import com.jojoldu.springmockspybean.domain.customer.CustomerRepository;
import com.jojoldu.springmockspybean.domain.order.CustomerOrder;
import com.jojoldu.springmockspybean.domain.order.CustomerOrderRepository;
import com.jojoldu.springmockspybean.domain.order.OrderProductMap;
import com.jojoldu.springmockspybean.domain.product.Product;
import com.jojoldu.springmockspybean.dto.OrderResponseDto;
import com.jojoldu.springmockspybean.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 18.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final HttpSession httpSession;
    private final ObjectMapper objectMapper;
    private final QueueMessagingTemplate queueMessagingTemplate;

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

        List<Product> products = customerOrderRepository.findTopByCustomer(customer)
                .map(o -> o.getProducts().stream()
                        .map(OrderProductMap::getProduct)
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());

        return objectMapper.writeValueAsString(new OrderResponseDto(customer.getName(), products));
    }

}
