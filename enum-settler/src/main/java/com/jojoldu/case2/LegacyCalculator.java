package com.jojoldu.case2;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 26.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class LegacyCalculator {

    public static long calculate(String code, long originValue){

        if("CALC_A".equals(code)){
            return originValue;
        } else if("CALC_B".equals(code)){
            return originValue * 10;
        } else if("CALC_C".equals(code)){
            return originValue * 3;
        } else {
            return 0;
        }
    }

}
