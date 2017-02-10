package com.blogcode;

import com.blogcode.after.EnumContract;
import com.blogcode.after.EnumMapper;
import com.blogcode.after.EnumModel;
import com.blogcode.after.EnumValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 10.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RestController
public class ApiController {

    @GetMapping("/enum")
    public Map<String, Object> getEnum() {
        Map<String, Object> enums = new LinkedHashMap<>();

        Class commissionType = EnumContract.CommissionType.class;
        Class commissionCutting = EnumContract.CommissionCutting.class;

        enums.put("commissionType", commissionType.getEnumConstants());
        enums.put("commissionCutting", commissionCutting.getEnumConstants());
        return enums;
    }

    @GetMapping("/value")
    public Map<String, List<EnumValue>> getEnumValue() {
        Map<String, List<EnumValue>> enumValues = new LinkedHashMap<>();

        enumValues.put("commissionType", toEnumValues(EnumContract.CommissionType.class));
        enumValues.put("commissionCutting", toEnumValues(EnumContract.CommissionCutting.class));

        return enumValues;
    }

    private List<EnumValue> toEnumValues(Class<? extends EnumModel> e){
        /*
            // Java8이 아닐경우
            List<EnumValue> enumValues = new ArrayList<>();
            for (EnumModel enumType : e.getEnumConstants()) {
                enumValues.add(new EnumValue(enumType));
            }
            return enumValues;
         */
        return Arrays
                .stream(e.getEnumConstants())
                .map(EnumValue::new)
                .collect(Collectors.toList());
    }


    private EnumMapper enumMapper;

    public ApiController(EnumMapper enumMapper) {
        this.enumMapper = enumMapper;
    }

    @GetMapping("/mapper")
    public Map<String, List<EnumValue>> getMapper() {
        return enumMapper.getAll();
    }
}
