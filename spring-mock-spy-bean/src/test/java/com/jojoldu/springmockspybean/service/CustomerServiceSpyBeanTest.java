package com.jojoldu.springmockspybean.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.springmockspybean.domain.customer.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.doReturn;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 22.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceSpyBeanTest {

    @Autowired
    private CustomerService customerService;

    @SpyBean
    private ObjectMapper objectMapper;

    @Test
    public void test() throws Exception {
        String customer = objectMapper.writeValueAsString(new Customer());
        doReturn(new Customer()).when(objectMapper).readValue(customer, Customer.class);

        customerService.getCustomerJsonString(customer);
    }
}
