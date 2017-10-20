package com.jojoldu.spy.domain;

import com.jojoldu.spy.dto.RequestDto;
import com.jojoldu.spy.exception.PayFailException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
@AllArgsConstructor
public class CustomerService {

    public static final String ALARM_URL = "http://alarm.com";
    public static final String PAY_URL = "http://pay.com";

    private RestTemplate restTemplate;

    @Transactional
    public void pay(RequestDto requestDto){
        try{
            restTemplate.postForEntity(PAY_URL, requestDto, String.class);
        } catch (RestClientException e){
            alarm(requestDto);
            throw new PayFailException("결제가 실패하였습니다.");
        }
    }

    void alarm(RequestDto requestDto) {
        requestDto.verifyEmptyField();
        restTemplate.postForEntity(ALARM_URL, requestDto, String.class);
    }
}
