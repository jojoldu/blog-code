package com.jojoldu.springmockspybean.service;

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

import javax.servlet.http.HttpSession;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

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

    @MockBean (name = "customerOrderRepository")
    private CustomerOrderRepository customerOrderRepository;

    @SpyBean (name = "httpSession")
    private HttpSession httpSession;

    @Test
    public void saveMyOrderProductCountInSession () throws Exception {

        // given
        Customer customer = new Customer ();

        given (httpSession.getAttribute ("loginUser"))
                .willReturn (customer);

        CustomerOrder order = new CustomerOrder ();
        order.addProduct (new Product ());
        order.addProduct (new Product ());

        given (customerOrderRepository.findAllByCustomer (customer))
                .willReturn (Stream.of (order));

        // when
        customerService.saveMyOrderProductCountInSession ();
        int count = (Integer) httpSession.getAttribute ("orderCount");

        // then
        assertThat (count, is (2));
    }

}
