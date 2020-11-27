package com.jojoldu.blogcode.querydsl.domain.order;

import javax.persistence.AttributeConverter;
import java.time.Period;

/**
 * Created by jojoldu@gmail.com on 27/11/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public class PeriodStringConverter implements AttributeConverter<Period, String> {

    @Override
    public String convertToDatabaseColumn(Period attribute) {
        return attribute.toString();
    }

    @Override
    public Period convertToEntityAttribute(String dbData) {
        return Period.parse( dbData );
    }
}
