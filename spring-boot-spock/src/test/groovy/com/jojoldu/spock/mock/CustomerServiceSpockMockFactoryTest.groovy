package com.jojoldu.spock.mock


import com.jojoldu.spock.domain.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification
import spock.mock.DetachedMockFactory

/**
 * Created by jojoldu@gmail.com on 2018. 9. 27.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@SpringBootTest
class CustomerServiceSpockMockFactoryTest extends Specification {

    @Autowired
    CustomerService customerService

    @Autowired
    JdbcTemplate jdbcTemplate

    @TestConfiguration
    static class SpockMockConfiguration {

        def factory = new DetachedMockFactory()

        @Bean
        JdbcTemplate jdbcTemplate() {
            return factory.Mock(JdbcTemplate)
        }
    }

    def "Customer id로 이름을 조회" () {
        given:
        jdbcTemplate.query(_, _) >> Arrays.asList("jojoldu")

        when:
        List<String> queryResult = customerService.execute("sql")

        then:
        queryResult.get(0) == "jojoldu"
    }
}
