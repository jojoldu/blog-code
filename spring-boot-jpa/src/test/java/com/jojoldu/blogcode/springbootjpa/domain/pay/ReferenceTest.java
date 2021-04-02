package com.jojoldu.blogcode.springbootjpa.domain.pay;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 02/04/2021
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@ExtendWith(MockitoExtension.class)
public class ReferenceTest {

    @Test
    void passByReference() throws Exception {
        //given
        PayDetails details1 = PayDetails.EMPTY;
        PayDetails details2 = PayDetails.EMPTY;

        //then
        System.out.printf("empty=%s\n details1=%s\n details2=%s\n", PayDetails.EMPTY, details1, details2);
        assertThat(details1).isEqualTo(details2);
    }

    @Test
    void passByReference2() throws Exception {
        //given
        PayDetails details1 = new PayDetails();
        PayDetails details2 = details1;

        //then
        System.out.printf("empty=%s\n details1=%s\n details2=%s\n", PayDetails.EMPTY, details1, details2);
        assertThat(details1).isEqualTo(details2);
    }
}
