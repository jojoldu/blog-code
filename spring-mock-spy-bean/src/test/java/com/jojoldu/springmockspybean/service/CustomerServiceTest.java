package com.jojoldu.springmockspybean.service;

import com.jojoldu.springmockspybean.domain.customer.Customer;
import com.jojoldu.springmockspybean.domain.customer.CustomerRepository;
import com.jojoldu.springmockspybean.domain.order.CustomerOrder;
import com.jojoldu.springmockspybean.domain.order.CustomerOrderRepository;
import com.jojoldu.springmockspybean.domain.product.Product;
import com.jojoldu.springmockspybean.domain.product.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 19.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerService customerService;

    @Test
    public void findMyOrderPriceSum_로그인사용자의_주문상품금액합계가_반환된다 () throws Exception {
        //given
        Customer customer = customerRepository.save(
                Customer.builder()
                        .name("jojoldu")
                        .email("jojoldu@gmail.com")
                        .mobileNumber("01011239877")
                        .build()
        );

        //세션에 저장
        httpSession.setAttribute("loginUser", customer);

        Product 키보드 = productRepository.save(
                Product.builder()
                        .name("키보드")
                        .manufactureDate(LocalDate.of(2017,9,19))
                        .price(10000)
                        .build()
        );

        Product 마우스 = productRepository.save(
                Product.builder()
                        .name("마우스")
                        .manufactureDate(LocalDate.of(2017,9,19))
                        .price(15000)
                        .build()
        );

        CustomerOrder customerOrder = customerOrderRepository.save(
                CustomerOrder.builder()
                        .customer(customer)
                        .address("장은빌딩")
                        .orderDateTime(LocalDateTime.of(2017,9,19,20,0))
                        .isGiftPackaging(true)
                        .build()
        );

        customerOrder.addProduct(키보드);
        customerOrder.addProduct(마우스);

        customerOrderRepository.save(customerOrder);

        //when
        long sum = customerService.findMyOrderPriceSum();

        //then
        assertThat(sum, is(25000L));

    }


}
