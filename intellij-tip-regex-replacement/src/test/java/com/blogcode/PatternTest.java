package com.blogcode;

import org.junit.Test;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class PatternTest {

    @Test
    public void 텍스트교체() throws Exception {
        PayAmount payAmount1 = PayAmount.Builder.builder().originAmount(1000L).supplyAmount(1000L).build();
        PayAmount payAmount2 = PayAmount.Builder.builder().originAmount(2000L).supplyAmount(2000L).build();
        PayAmount payAmount3 = PayAmount.Builder.builder().originAmount(3000L).supplyAmount(3000L).build();
        PayAmount payAmount4 = PayAmount.Builder.builder().originAmount(4000L).supplyAmount(4000L).build();
    }

    @Test
    public void 텍스트교체2() throws Exception {
        PayAmount payAmount1 = PayAmount.Builder.builder().originAmount(9000L).supplyAmount(9000L).build();
        PayAmount payAmount2 = PayAmount.Builder.builder().originAmount(99000L).supplyAmount(99000L).build();
        PayAmount payAmount3 = PayAmount.Builder.builder().originAmount(91000L).supplyAmount(91000L).build();
        PayAmount payAmount4 = PayAmount.Builder.builder().originAmount(98000L).supplyAmount(98000L).build();
    }
}
