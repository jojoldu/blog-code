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
        AffiliateCode sisters = AffiliateCode.WOOWA_SISTERS;
        AffiliateCode children = AffiliateCode.WOOWA_CHILDREN;
        AffiliateCode adults = AffiliateCode.WOOWA_ADULTS;

        //then
        assertThat(sisters.getViewName(), is("우아한자매들"));
        assertThat(sisters.getBizNumber(), is("123-45-6789"));
        assertThat(children.getBizTypeCode(), is("s0099"));
        assertThat(adults.getCompanyCode(), is("w03"));
    }
}
