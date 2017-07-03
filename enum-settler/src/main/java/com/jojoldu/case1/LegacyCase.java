package com.jojoldu.case1;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 25.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class LegacyCase {

    public String toTable1Value(String originValue) {
        if("Y".equals(originValue)){
            return "1";
        } else {
            return "0";
        }
    }

    public boolean toTable2Value(String originValue){
        if("Y".equals(originValue)){
            return true;
        } else {
            return false;
        }
    }
}

