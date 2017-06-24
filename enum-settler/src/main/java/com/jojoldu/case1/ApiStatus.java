package com.jojoldu.case1;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 24.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public enum ApiStatus {
    Y("1", true),
    N("0", false);

    private String bValue;
    private boolean cValue;

    ApiStatus(String bValue, boolean cValue) {
        this.bValue = bValue;
        this.cValue = cValue;
    }

    public String getbValue() {
        return bValue;
    }

    public boolean iscValue() {
        return cValue;
    }
}
