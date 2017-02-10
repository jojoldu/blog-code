package com.blogcode.after;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public class EnumValue {
    private String key;
    private String value;

    public EnumValue(EnumModel enumModel) {
        key = enumModel.getKey();
        value = enumModel.getValue();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
