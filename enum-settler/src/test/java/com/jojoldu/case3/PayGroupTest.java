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

        assertThat(payGroup.name(), is("CARD"));
        assertThat(payGroup.getTitle(), is("카드"));
    }


    private String selectPayCode(){
        return "BAEMIN_PAY";
    }


    @Test
    public void PayGroup에게_직접_결제종류_물어보기_PayType () throws Exception {
        PayType payType = selectPayType();
        PayGroupAdvanced payGroupAdvanced = PayGroupAdvanced.findByPayType(payType);

        assertThat(payGroupAdvanced.name(), is("CARD"));
        assertThat(payGroupAdvanced.getTitle(), is("카드"));
    }



    private PayType selectPayType(){
        return PayType.BAEMIN_PAY;
    }


    @Test
    public void PayGroup으로_여러번_메소드_실행시켜야함_Legacy버전 () throws Exception {

        /*
            여러 비지니스 로직이 수행될 영역
         */
    }
}
