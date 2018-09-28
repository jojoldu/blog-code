package com.jojoldu.spock

import com.jojoldu.spock.domain.Customer
import com.jojoldu.spock.domain.CustomerRepository
import com.jojoldu.spock.domain.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.mockito.BDDMockito.given

/**
 * Created by jojoldu@gmail.com on 2018. 9. 27.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@SpringBootTest
class SpockMockTest extends Specification {
    @Autowired
    CustomerService customerService

    @Autowired
    CustomerRepository customerRepository

    @TestConfiguration
    static class SpockMockConfiguration {

        DetachedMockFactory factory = new DetachedMockFactory()

        @Bean
        CustomerRepository customerRepository() {
            return factory.Mock(CustomerRepository)
        }
    }


    def "Customer id로 이름을 조회" () {
        given:
        customerRepository.findById(_) >> Optional.of(new Customer("jojoldu", "jojoldu@gmail.com"))

        when:
        String name = customerService.getCustomerName(1)

        then:
        name == "jojoldu"
    }
}
