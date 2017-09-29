package com.jojoldu.spock

import com.jojoldu.spock.basic.FeeCalculateType
import com.jojoldu.spock.exception.NegativeNumberException
import spock.lang.Specification
import spock.lang.Unroll

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

    // 공식문서의 where feature 메소드 예제를 좀 더 가독성 있게 변경
    def "computing the maximum of two numbers"() {
        expect:
        Math.max(a, b) == c

        where:
        a | b | c
        5 | 1 | 5
        3 | 9 | 9
    }

    @Unroll // 메소드이름에 지정된 템플릿에 따라 테스트 결과를 보여준다
    def "금액이 주어지면 원단위 반올림 결과가 반환된다 [금액: #amount, 결과: #result]" () {
        given:
        def feeCalculator = FeeCalculateType.WON_UNIT_CUT

        expect:
        feeCalculator.calculate(amount) == result

        where:
        amount | result
        500L   | 500L
        495L   | 490L
        -500L  | -500L
        -495L  | -490L
    }


    def "음수가 입력되면 NagativeNumberException 발생한다" () {
        given:
        def feeCalculator = FeeCalculateType.WON_UNIT_CUT

        when:
        feeCalculator.calculate(-1)

        then:
        def e = thrown(NegativeNumberException.class)
        e.message == "음수는 허용하지 않습니다."
    }


    
}
