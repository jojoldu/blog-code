package com.blogcode.after;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jojoldu@gmail.com on 2017-02-04
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class EnumMapper {
    private Map<String, List<EnumValue>> factory = new HashMap<>();

    private List<EnumValue> toEnumValues(Class<? extends EnumType> e){
        /*
            // Java8 문법이 아닐경우 아래와 같습니다.
            List<EnumValue> enumValues = new ArrayList<>();
            for (EnumType enumType : e.getEnumConstants()) {
                enumValues.add(new EnumValue(enumType));
            }
            return enumValues;
         */
        return Arrays
                .stream(e.getEnumConstants())
                .map(EnumValue::new)
                .collect(Collectors.toList());
    }

    public void put(String key, Class<? extends EnumType> e){
        factory.put(key, toEnumValues(e));
    }

    public Map<String, List<EnumValue>> get(String keys){
        /*
            // Java8 문법이 아닐경우 아래와 같습니다.
            Map<String, List<EnumValue>> result = new LinkedHashMap<>();
            for (String key : keys.split(",")) {
                result.put(key, factory.get(key));
            }

            return result;
         */
        return Arrays
                .stream(keys.split(","))
                .collect(Collectors.toMap(Function.identity(), key -> factory.get(key)));
    }
}
