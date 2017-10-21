package com.jojoldu.spy.domain

import com.jojoldu.spy.dto.RequestDto
import com.jojoldu.spy.exception.PayFailException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * Created by jojoldu@gmail.com on 2017. 10. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */
class CustomerServiceSpockTest extends Specification {

    def "customerService를 Mocking 하면 실패한다"() {
        given:
        RequestDto requestDto = new RequestDto()
        def restTemplate = Stub(RestTemplate)
        def customerService = Spy(CustomerService, constructorArgs: [restTemplate])

        when:
        customerService.pay(requestDto)

        then:
        restTemplate.postForEntity(_,_,_) >> { throw new RestClientException("결제실패") }
        customerService.alarm(_) >> { }
        thrown(PayFailException.class)
    }

}
