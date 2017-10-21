package com.jojoldu.spy.domain;

import com.jojoldu.spy.dto.RequestDto;
import com.jojoldu.spy.exception.PayFailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceJunitSuccessTest {

    @Spy
    @InjectMocks
    private CustomerService customerService;

    @Mock
    private RestTemplate restTemplate;

    @Test(expected = PayFailException.class)
    public void 결제가_실패하면_PayFailException이_발생한다() {
        // given
        RequestDto requestDto = new RequestDto();

        given(restTemplate.postForEntity(CustomerService.PAY_URL, requestDto, String.class))
                .willThrow(new RestClientException("결제실패"));

        doNothing()
                .when(customerService)
                .alarm(requestDto);

        // when
        customerService.pay(requestDto);
    }
}
