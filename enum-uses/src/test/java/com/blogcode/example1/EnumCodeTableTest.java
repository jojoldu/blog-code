package com.blogcode.example1;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 7.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class EnumCodeTableTest {

    @Test
    public void 코드테이블_대신_enum() {
        //given
        Affiliate.Code sisters = Affiliate.Code.WOOWA_SISTERS;
        Affiliate.Code children = Affiliate.Code.WOOWA_CHILDREN;
        Affiliate.Code adults = Affiliate.Code.WOOWA_ADULTS;
        Affiliate.Code adultsValueOf = Affiliate.Code.valueOf("WOOWA_ADULTS");

        //then
        assertThat(sisters.getViewName(), is("우아한자매들"));
        assertThat(children.getBizTypeCode(), is("s0099"));
        assertThat(adults.getCompanyCode(), is("w03"));
        assertThat(adultsValueOf.getViewName(), is("우아한어른들"));
    }
}
