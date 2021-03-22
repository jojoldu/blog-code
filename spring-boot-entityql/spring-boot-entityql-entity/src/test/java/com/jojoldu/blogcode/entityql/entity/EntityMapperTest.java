package com.jojoldu.blogcode.entityql.entity;


import com.jojoldu.blogcode.entityql.entity.config.EntityMapper;
import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import com.jojoldu.blogcode.entityql.entity.domain.sql.EAcademy;
import com.jojoldu.blogcode.entityql.entity.domain.sql.EStudent;
import com.jojoldu.blogcode.entityql.entity.domain.student.Student;
import com.querydsl.core.types.Path;
import com.querydsl.sql.dml.BeanMapper;
import com.querydsl.sql.types.Null;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class EntityMapperTest {

    @Test
    void notSearchPropertyIfUsingBeanMapper() throws Exception {
        String phoneNumber = "010-xxxx-xxxx";
        Academy obj = Academy.builder().phoneNumber(phoneNumber).build();

        BeanMapper mapper = BeanMapper.DEFAULT;
        Map<Path<?>, Object> result = mapper.createMap(EAcademy.qAcademy, obj);

        assertThat(result.containsValue(phoneNumber)).isFalse();
    }

    @Test
    void searchPropertyIfUsingBeanMapper() throws Exception {
        String phoneNumber = "010-xxxx-xxxx";
        Academy obj = Academy.builder().phoneNumber(phoneNumber).build();

        EntityMapper mapper = EntityMapper.DEFAULT;
        Map<Path<?>, Object> result = mapper.createMap(EAcademy.qAcademy, obj);

        assertThat(result).containsValue(phoneNumber);
    }

    @Test
    void nullBindingIfUsingBeanMapper() throws Exception {
        String phoneNumber = "010-xxxx-xxxx";
        Academy obj = Academy.builder().phoneNumber(phoneNumber).build();

        EntityMapper mapper = EntityMapper.WITH_NULL_BINDINGS;
        Map<Path<?>, Object> result = mapper.createMap(EAcademy.qAcademy, obj);

        assertThat(result)
                .containsValue(phoneNumber)
                .containsValue(Null.DEFAULT);
    }
}
