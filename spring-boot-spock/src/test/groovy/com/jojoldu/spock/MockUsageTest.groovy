package com.jojoldu.spock

import com.jojoldu.spock.domain.AmountService
import com.jojoldu.spock.domain.FeeCalculateType
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
}
