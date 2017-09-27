package com.jojoldu.spock.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * Created by jojoldu@gmail.com on 2017. 9. 27.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@SpringBootTest
class CustomerRepositoryTest extends Specification {

    @Autowired
    CustomerRepository customerRepository

    //@Before
    def setup() {
        customerRepository.save(new Customer("jojoldu", "jojoldu@gmail.com"))
        customerRepository.save(new Customer("jojoldu1", "jojoldu1@gmail.com"))
        customerRepository.save(new Customer("jojoldu2", "jojoldu2@gmail.com"))
    }

    def "findAll 전체 Customer가 조회된다" () {
        when:
        List<Customer> customers = customerRepository.findAll()

        then:
        customers.size() == 3
        customers.get(0).name == "jojoldu"
        customers.get(0).email == "jojoldu@gmail.com"
    }
}
