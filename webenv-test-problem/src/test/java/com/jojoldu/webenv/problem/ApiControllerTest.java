package com.jojoldu.webenv.problem;

import com.jojoldu.webenv.WebIntegrationTest;
import com.jojoldu.webenv.problem.domain.Customer;
import com.jojoldu.webenv.problem.domain.CustomerRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 11. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class ApiControllerTest extends WebIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void URL로_전체데이터_가져온다() throws Exception {
        //given:
        customerRepository.save(new Customer("고객1", "customer1@gmail.com"));
        customerRepository.save(new Customer("고객2", "customer2@gmail.com"));

        //when:
        List<Customer> customers = this.restTemplate.getForObject("/customer", List.class);

        //then:
        assertThat(customers.size(), is(2));
    }
}
