package com.jojoldu.case2;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 26.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class CalculatorTypeTest {

    private String selectCode(){
        return "CALC_B";
    }

    private CalculatorType selectType() {
        return CalculatorType.CALC_B;
    }





    @Test
    public void 코드에_따라_서로다른_계산하기_기존방식 () throws Exception {
        String code = selectCode();
        long originValue = 10000L;
        long result = LegacyCalculator.calculate(code, originValue);

        assertThat(result, is(10000L));
    }




    @Test
    public void 코드에_따라_서로다른_계산하기_Enum () throws Exception {
        CalculatorType code = selectType();
        long originValue = 10000L;
        long result = code.calculate(originValue);

        assertThat(result, is(10000L));
    }




}
