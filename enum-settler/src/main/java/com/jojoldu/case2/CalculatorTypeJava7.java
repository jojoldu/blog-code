package com.jojoldu.case2;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 30.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public enum CalculatorTypeJava7 {

    CALC_A {
        long calculate(long value){ return value;}
    },
    CALC_B {
        long calculate(long value){ return value * 10;}
    },
    CALC_C {
        long calculate(long value){ return value * 3;}
    },
    CALC_ETC {
        long calculate(long value){ return 0L;}
    };

    abstract long calculate(long value);
}
