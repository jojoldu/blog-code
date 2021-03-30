package com.jojoldu.blogcode.springbootjpa.domain.pay;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 30/03/2021
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@ExtendWith(MockitoExtension.class)
public class PayDetailsTest {

    @Test
    void PayDetails_EMPTY는_같은객체다() throws Exception {
        //given
        Pay pay1 = new Pay();
        Pay pay2 = new Pay();

        //then
        System.out.printf("pay1=%s, pay2=%s%n", pay1.getPayDetails(), pay2.getPayDetails());
        assertThat(pay1.getPayDetails()).isEqualTo(pay2.getPayDetails());
    }

    @Test
    void PayEvents는_매번_새로생성된다() throws Exception {
        //given
        Pay pay1 = new Pay();
        Pay pay2 = new Pay();

        //then
        System.out.printf("pay1=%s, pay2=%s%n", pay1.getPayEvents(), pay2.getPayEvents());
        assertThat(pay1.getPayEvents()).isNotEqualTo(pay2.getPayEvents());
    }
}
