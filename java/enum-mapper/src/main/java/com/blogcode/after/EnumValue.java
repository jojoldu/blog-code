package com.blogcode.after;

/**
 * Created by jojoldu on 2017. 2. 6..
 */

public class EnumValue {
    private EnumType enumType;

    public EnumValue(EnumType enumType) {
        this.enumType = enumType;
    }

    public String getKey() {
        return enumType.getKey();
    }

    public String getValue() {
        return enumType.getValue();
    }
}
