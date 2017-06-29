package com.jojoldu.case3;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 29.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class PayGroupTest {


    @Test
    public void PayGroup에게_직접_결제종류_물어보기_문자열 () throws Exception {
        String payCode = selectPayCode();
        PayGroup payGroup = PayGroup.findByPayCode(payCode);

        assertThat(payGroup.name(), is("BAEMIN_PAY"));
        assertThat(payGroup.getTitle(), is("배민페이"));
    }


    private String selectPayCode(){
        return "BAEMIN_PAY";
    }


    @Test
    public void PayGroup에게_직접_결제종류_물어보기_PayType () throws Exception {
        PayType payType = selectPayType();
        PayGroupAdvanced payGroupAdvanced = PayGroupAdvanced.findByPayType(payType);

        assertThat(payGroupAdvanced.name(), is("BAEMIN_PAY"));
        assertThat(payGroupAdvanced.getTitle(), is("배민페이"));
    }

    private PayType selectPayType(){
        return PayType.BAEMIN_PAY;
    }
}
