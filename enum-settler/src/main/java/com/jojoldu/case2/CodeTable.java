package com.jojoldu.case2;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 26.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class CodeTable {


    private long value;


    private String code;


    @Column
    @Enumerated(EnumType.STRING)
    private CalculatorType calculatorType;


    public CodeTable() {}

    public CodeTable(String code) {
        this.code = code;
    }

    public CodeTable(CalculatorType calculatorType) {
        this.calculatorType = calculatorType;
    }

    public String getCode() {
        return code;
    }

    public CalculatorType getCalculatorType() {
        return calculatorType;
    }
}
