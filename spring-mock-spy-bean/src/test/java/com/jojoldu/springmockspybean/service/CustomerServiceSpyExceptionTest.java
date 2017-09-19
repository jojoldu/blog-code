package com.jojoldu.springmockspybean.service;

import com.jojoldu.springmockspybean.domain.customer.Customer;
import com.jojoldu.springmockspybean.domain.customer.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 18.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceSpyExceptionTest {

    @Autowired
    private CustomerService customerService;

    @SpyBean(name = "customerRepository")
    private CustomerRepository customerRepository;

    @Test
    public void 레파지토리에_spybean_사용하면_오류발생한다 () throws Exception {
        final String CUSTOMER_NAME = "jojoldu";
        given(this.customerRepository.findCustomerByName(CUSTOMER_NAME))
                .willReturn(Optional.of(Customer.builder().name(CUSTOMER_NAME).build()));

        Customer customer = customerService.findByName(CUSTOMER_NAME);

        assertThat(customer.getName(), is(CUSTOMER_NAME));
    }
}
