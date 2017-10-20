package com.jojoldu.mockitospyusages.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
@AllArgsConstructor
public class CustomerService {

    private RestTemplate restTemplate;



    private void alarm() {

    }
}
