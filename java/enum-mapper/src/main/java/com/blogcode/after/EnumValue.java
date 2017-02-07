package com.blogcode.after;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
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
