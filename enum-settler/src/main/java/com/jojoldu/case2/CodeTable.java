package com.jojoldu.case2;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 26.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class CodeTable {


    private long value;


    private String code;


    private Calculator calculator;


    public CodeTable() {}

    public CodeTable(String code) {
        this.code = code;
    }

    public CodeTable(Calculator calculator) {
        this.calculator = calculator;
    }

    public String getCode() {
        return code;
    }

    public Calculator getCalculator() {
        return calculator;
    }
}
