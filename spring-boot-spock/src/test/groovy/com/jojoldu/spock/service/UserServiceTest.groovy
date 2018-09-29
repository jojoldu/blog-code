package com.jojoldu.spock.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.jojoldu.spock.service.dto.OrderDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import spock.lang.Specification

import java.time.LocalDateTime

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

/**
 * Created by jojoldu@gmail.com on 2018. 9. 29.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RestClientTest(value=[UserService.class])
class UserServiceTest extends Specification {

    @Autowired
    private UserService userService

    @Autowired
    private MockRestServiceServer mockServer

    @Autowired
    private ObjectMapper objectMapper

    private String orderApiUrl = "http://localhost:8090/order?orderNo="

    def "[Json String 사용] OrderDto는 OrderAPI의 Json 결과값을 담을 수 있다."() {
        given:
        String expectOrderNo = "1"
        Long expectAmount = 1000L
        LocalDateTime expectOrderDateTime = LocalDateTime.of(2018,9,29,0,0)

        String expectResult = "{\"orderNo\":\"1\",\"amount\":1000,\"orderDateTime\":\"2018-09-29 00:00:00\"}"
        mockServer.expect(requestTo(orderApiUrl+expectOrderNo))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON))

        when:
        OrderDto response = userService.getUserOrder(expectOrderNo)

        then:
        response.getOrderNo() == expectOrderNo
        response.getAmount() == expectAmount
        response.getOrderDateTime() == expectOrderDateTime
    }

    def "[ExpectOrderDto 사용] OrderDto는 OrderAPI의 Json 결과값을 담을 수 있다."() {
        given:
        String expectOrderNo = "1"
        Long expectAmount = 1000L
        LocalDateTime expectOrderDateTime = LocalDateTime.of(2018,9,29,0,0)

        String expectResult = objectMapper.writeValueAsString(new ExpectOrderDto(expectOrderNo, expectAmount, "2018-09-29 00:00:00"))
        mockServer.expect(requestTo(orderApiUrl+expectOrderNo))
                .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON))

        when:
        OrderDto response = userService.getUserOrder(expectOrderNo)

        then:
        response.getOrderNo() == expectOrderNo
        response.getAmount() == expectAmount
        response.getOrderDateTime() == expectOrderDateTime
    }

    static class ExpectOrderDto {
        private String orderNo
        private Long amount
        private String orderDateTime

        ExpectOrderDto() {}

        ExpectOrderDto(String orderNo, Long amount, String orderDateTime) {
            this.orderNo = orderNo
            this.amount = amount
            this.orderDateTime = orderDateTime
        }

        String getOrderNo() {
            return orderNo
        }

        Long getAmount() {
            return amount
        }

        String getOrderDateTime() {
            return orderDateTime
        }
    }
}
