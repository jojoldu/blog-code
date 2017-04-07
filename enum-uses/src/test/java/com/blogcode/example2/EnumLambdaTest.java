package com.blogcode.example2;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 7.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class EnumLambdaTest {

    @Test
    public void 매출타입별계산() {
        //given
        long txAmount = 10000L;

        //then
        long originAmount = SalesAmountType.ORIGIN_AMOUNT.calculate(txAmount);
        assertThat(originAmount, is(10000L));

        long supplyAmount = SalesAmountType.SUPPLY_AMOUNT.calculate(txAmount);
        assertThat(supplyAmount, is(9091L));

        long vatAmount = SalesAmountType.VAT_AMOUNT.calculate(txAmount);
        assertThat(vatAmount, is(909L));

        long notUsed = SalesAmountType.NOT_USED.calculate(txAmount);
        assertThat(notUsed, is(0L));
    }
}
