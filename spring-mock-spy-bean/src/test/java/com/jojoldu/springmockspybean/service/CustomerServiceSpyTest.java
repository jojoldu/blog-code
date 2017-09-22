package com.jojoldu.springmockspybean.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.springmockspybean.domain.customer.Customer;
import com.jojoldu.springmockspybean.domain.order.CustomerOrder;
import com.jojoldu.springmockspybean.domain.order.CustomerOrderRepository;
import com.jojoldu.springmockspybean.domain.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 21.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceSpyTest {

    @Autowired
    private CustomerService customerService;

    @MockBean(name = "customerOrderRepository")
    private CustomerOrderRepository customerOrderRepository;

    @SpyBean(name = "objectMapper")
    private ObjectMapper objectMapper;

    @Test
    public void getCustomerJsonString_JSON문자열반환 () throws Exception {
        //given
        Customer customer = Customer.builder()
                .name("jojoldu")
                .build();

        CustomerOrder order = new CustomerOrder();

        order.addProduct(Product.builder()
                .price(10000L)
                .build());

        order.addProduct(Product.builder()
                .price(15000L)
                .build());

        given(customerOrderRepository.findAllByCustomer(customer))
                .willReturn(Stream.of(order));

        doReturn(customer).when(objectMapper).readValue("", Customer.class);
        
        //when
        final String customerJsonString = customerService.getCustomerJsonString(objectMapper.writeValueAsString(""));

        //then
        System.out.println(customerJsonString);
    }

}
