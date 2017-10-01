package com.jojoldu.spock.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import spock.lang.Specification

import static org.mockito.BDDMockito.given

/**
 * Created by jojoldu@gmail.com on 2017. 9. 29.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@SpringBootTest
class CustomerServiceTest extends Specification {

    @Autowired
    CustomerService customerService

    @MockBean(name = "customerRepository")
    CustomerRepository customerRepository

    def "Customer id로 이름을 조회" () {
        given:
        long id = 1
        given(customerRepository.findOne(id))
                .willReturn(new Customer("jojoldu", "jojoldu@gmail.com"))

        when:
        String name = customerService.getCustomerName(id)

        then:
        name == "jojoldu"
    }
}
