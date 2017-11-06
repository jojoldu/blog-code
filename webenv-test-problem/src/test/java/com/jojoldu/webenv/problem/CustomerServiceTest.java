package com.jojoldu.webenv.problem;

import com.jojoldu.webenv.problem.domain.Customer;
import com.jojoldu.webenv.problem.domain.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.jojoldu.webenv.Application.CONFIG_LOCATIONS;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 11. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@TestPropertySource(properties = CONFIG_LOCATIONS)
@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void 전체고객을_가져온다() throws Exception {
        //given:
        customerRepository.save(new Customer("고객1", "customer1@gmail.com"));
        customerRepository.save(new Customer("고객2", "customer2@gmail.com"));

        //when:
        List<Customer> customers = customerService.findAll();

        //then:
        assertThat(customers.size(), is(2));
    }
}
