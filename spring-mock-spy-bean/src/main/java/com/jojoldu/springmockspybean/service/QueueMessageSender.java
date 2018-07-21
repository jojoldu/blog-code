package com.jojoldu.springmockspybean.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by jojoldu@gmail.com on 21/07/2018
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class QueueMessageSender {

    private final QueueMessagingTemplate queueMessagingTemplate;

    public void sendMessage(String queueName, Object message){
        try{
            queueMessagingTemplate.convertAndSend(queueName, message);
        } catch (Exception e){
            log.error("[메세지전송] Message: {}", message);
            log.error("[메세지전송] Exception", e);
            throw new RuntimeException("SQS 메세지 전송이 실패했습니다.", e);
        }
    }
}
