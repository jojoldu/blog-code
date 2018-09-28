package com.jojoldu.spock.mock

import com.jojoldu.spock.domain.CustomerService
import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.BDDMockito.given

/**
 * Created by jojoldu@gmail.com on 2018. 9. 28.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@SpringBootTest
class CustomerServiceSpockMockTest extends Specification {

    @Autowired
    CustomerService customerService

    @MockBean(name = "jdbcTemplate")
    JdbcTemplate jdbcTemplate

    def "Customer_id로_이름을_조회"() {
        given:
        given(jdbcTemplate.query(anyString(), any(BeanPropertyRowMapper.class)))
                .willReturn(Arrays.asList("jojoldu"))
        when:
        List<String> queryResult = customerService.execute("sql")

        then:
        queryResult.get(0) == "jojoldu"
    }
}