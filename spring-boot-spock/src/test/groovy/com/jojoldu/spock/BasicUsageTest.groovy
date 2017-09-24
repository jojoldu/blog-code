package com.jojoldu.spock

import spock.lang.Specification

import java.math.RoundingMode

/**
 * Created by jojoldu@gmail.com on 2017. 9. 24.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

class BasicUsageTest extends Specification {

    def "495를 원단위로 반올림하면 500이 된다"() {
        given:
        BigDecimal 금액 = BigDecimal.valueOf(495)

        when:
        BigDecimal 원단위_반올림 = 금액.setScale(-1, RoundingMode.HALF_UP)

        then:
        원단위_반올림 == 500
    }
}
