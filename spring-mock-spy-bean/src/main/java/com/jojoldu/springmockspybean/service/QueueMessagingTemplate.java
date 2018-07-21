package com.jojoldu.springmockspybean.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by jojoldu@gmail.com on 21/07/2018
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Slf4j
@Component
public class QueueMessagingTemplate {
    public void convertAndSend(String destinationName, Object payload) {
        log.info("SQS로 메세지 전송!");
    }
}
