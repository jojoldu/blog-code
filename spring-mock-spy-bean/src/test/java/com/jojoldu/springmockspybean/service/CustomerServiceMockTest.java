package com.jojoldu.springmockspybean.service;

import com.jojoldu.springmockspybean.domain.customer.Customer;
import com.jojoldu.springmockspybean.domain.customer.CustomerRepository;
import com.jojoldu.springmockspybean.domain.order.CustomerOrder;
import com.jojoldu.springmockspybean.domain.order.CustomerOrderRepository;
import com.jojoldu.springmockspybean.domain.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;

import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 19.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceMockTest {

    @Autowired
    private CustomerService customerService;

    @MockBean(name = "httpSession")
    private HttpSession httpSession;

    @MockBean(name = "customerOrderRepository")
    private CustomerOrderRepository customerOrderRepository;

    @Test
    public void findMyOrderPriceSum_로그인사용자의_주문상품금액합계가_반환된다 () throws Exception {
        //given
        Customer customer = new Customer();

        given(httpSession.getAttribute("loginUser"))
                .willReturn(customer);

        CustomerOrder order = new CustomerOrder();

        order.addProduct(Product.builder()
                .price(10000L)
                .build());

        order.addProduct(Product.builder()
                .price(15000L)
                .build());

        given(customerOrderRepository.findAllByCustomer(customer))
                .willReturn(Stream.of(order));

        //when
        long sum = customerService.findMyOrderPriceSum();

        //then
        assertThat(sum, is(25000L));
    }
}
