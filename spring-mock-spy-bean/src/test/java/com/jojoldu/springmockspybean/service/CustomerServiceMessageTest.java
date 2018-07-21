package com.jojoldu.springmockspybean.service;

import com.jojoldu.springmockspybean.domain.customer.Customer;
import com.jojoldu.springmockspybean.domain.customer.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;

/**
 * Created by jojoldu@gmail.com on 21/07/2018
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceMessageTest {

    @MockBean
    QueueMessagingTemplate queueMessagingTemplate;

    @Autowired
    CustomerService customerService;

    @Test(expected = RuntimeException.class) // then
    public void 메세지전송이_실패하면_RuntimeException이_발생한다() {
        //given
        String queueName = "order";
        String message = "주문1";

        doThrow(new RestClientException("SQS가 죽었네요?")) //
                .when(queueMessagingTemplate).convertAndSend(anyString(), anyString());

        //when
        customerService.sendMessage(queueName, message);
    }

    @Test(expected = RuntimeException.class) // then
    public void MockBean없이_메세지전송이_실패하면_RuntimeException이_발생한다() {
        //given
        String queueName = "order";
        String message = "주문1";

        QueueMessagingTemplate mockMessageTemplate = new MockQueueMessagingTemplate();
        QueueMessageSender queueMessageSender = new QueueMessageSender(mockMessageTemplate);

        //when
        queueMessageSender.sendMessage(queueName, message);
    }

    static class MockQueueMessagingTemplate extends QueueMessagingTemplate {

        @Override
        public void convertAndSend(String destinationName, Object payload) {
            throw new RestClientException("SQS가 죽었네요?");
        }
    }
}
