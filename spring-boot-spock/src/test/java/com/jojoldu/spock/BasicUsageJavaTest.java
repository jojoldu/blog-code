package com.jojoldu.spock;

import com.jojoldu.spock.domain.FeeCalculateType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 9. 25.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class BasicUsageJavaTest {

    @Test
    public void calculate_금액이_주어지면_원단위_반올림_결과가_반환된다 () throws Exception {
        //given
        FeeCalculateType feeCalculator = FeeCalculateType.WON_UNIT_CUT;

        //when & then
        final long case1 = feeCalculator.calculate(500);
        assertThat(case1, is(500L));

        final long case2 = feeCalculator.calculate(495);
        assertThat(case2, is(490L));

        final long case3 = feeCalculator.calculate(-500);
        assertThat(case3, is(-500L));

        final long case4 = feeCalculator.calculate(-495);
        assertThat(case4, is(-490L));

    }
}
