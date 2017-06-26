package com.jojoldu.case1;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 24.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public enum ApiStatus {

    Y("1", true),
    N("0", false);

    private String bapiValue;
    private boolean capiValue;

    ApiStatus(String bapiValue, boolean capiValue) {
        this.bapiValue = bapiValue;
        this.capiValue = capiValue;
    }

    public String getBapiValue() {
        return bapiValue;
    }

    public boolean isCapiValue() {
        return capiValue;
    }

}
