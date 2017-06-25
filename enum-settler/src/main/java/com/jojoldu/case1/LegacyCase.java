package com.jojoldu.case1;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 25.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class LegacyCase {

    public String toBApiValue(String AApiValue) {
        if("Y".equals(AApiValue)){
            return "1";
        } else {
            return "0";
        }
    }

    public boolean toCApiValue(String AApiValue){
        if("Y".equals(AApiValue)){
            return true;
        } else {
            return false;
        }
    }
}
