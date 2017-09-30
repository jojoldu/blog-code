package com.jojoldu.spock

import com.jojoldu.spock.basic.AmountService
import com.jojoldu.spock.basic.FeeCalculateType
import com.jojoldu.spock.domain.Customer
import com.jojoldu.spock.domain.CustomerRepository
import com.jojoldu.spock.domain.CustomerService
import spock.lang.Specification

/**
 * Created by jojoldu@gmail.com on 2017. 9. 28.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

class MockUsageTest extends Specification {

    def "API를 통해 받은 값을 원단위 버림 계산한다." () {
        given:
        def mockAmountService = Mock(AmountService.class)

        when:
        long amount = mockAmountService.getAmount()

        then:
        mockAmountService.getAmount() >> 999
        999 == amount
        990L == FeeCalculateType.WON_UNIT_CUT.calculate(amount)
    }

    def "Vip는 이벤트에 참여하면 포인트 적립이 2번 발생한다." () {
        given:
        def mockCustomerRepository = Mock(CustomerRepository)
        CustomerService customerService = new CustomerService(mockCustomerRepository)
        long customerId = 1
        long point = 1000
        def customer = new Customer("jojoldu", "jojoldu@gmail.com", true)

        when:
        customerService.joinEvent(customerId, point)

        then:
        mockCustomerRepository.findOne(customerId) >> customer
        (_..2) * mockCustomerRepository.savePoint(_, _)
    }
}
