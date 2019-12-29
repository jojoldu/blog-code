package com.jojoldu.blogcode.springboot.tips;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.blogcode.springboot.tips.setter.RequestSetterDto;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ObjectMapperTest {

    @Test
    public void setter가없어도_JSON변환이된다() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        String name = "jojoldu";
        String content = objectMapper.writeValueAsString(new RequestSetterDto(name, 1000L));

        //when
        RequestSetterDto dto = objectMapper.readValue(content, RequestSetterDto.class);

        //then
        assertThat(name, is(dto.getName()));
    }
}
