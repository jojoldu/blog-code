package com.jojoldu.spy.domain

import com.jojoldu.spy.dto.RequestDto
import com.jojoldu.spy.exception.PayFailException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.mockito.BDDMockito.given

/**
 * Created by jojoldu@gmail.com on 2017. 10. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */
import static org.mockito.Matchers.any
import static org.mockito.Matchers.anyString
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.doNothing

@SpringBootTest
class CustomerServiceBootTest extends Specification {

    @MockBean
    RestTemplate restTemplate

    @SpyBean
    CustomerService customerService

    def "[Spock+SpringBoot] 결제가 실패하면 PayFailException이 발생한다"() {
        given:
        RequestDto requestDto = new RequestDto()

        given(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .willThrow(new RestClientException("결제실패"))

        doNothing()
                .when(customerService)
                .alarm(any())
        when:
        customerService.pay(requestDto)

        then:
        thrown(PayFailException.class)
    }
}
