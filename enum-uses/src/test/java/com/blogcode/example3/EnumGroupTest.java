package com.blogcode.example3;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 8.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class EnumGroupTest {

    @Test
    public void 결제그룹구분() {
        //given
        PaymentOption toss = PaymentOption.TOSS;
        PaymentOption simplePay = PaymentOption.SIMPLE_PAY;
        PaymentOption point = PaymentOption.POINT;

        //then
        PaymentGroup tossGroup = PaymentGroup.findGroup(toss);
        PaymentGroup simplePayGroup = PaymentGroup.findGroup(simplePay);
        PaymentGroup pointGroup = PaymentGroup.findGroup(point);

        assertThat(tossGroup.getViewName(), is("현금"));
        assertThat(simplePayGroup.getViewName(), is("결제대행사"));
        assertThat(pointGroup.getViewName(), is("기타"));
    }
}
