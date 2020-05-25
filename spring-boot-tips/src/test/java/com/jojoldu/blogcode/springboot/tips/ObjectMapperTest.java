package com.jojoldu.blogcode.springboot.tips;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jojoldu.blogcode.springboot.tips.setter.RequestSetterDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ObjectMapperTest {

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .timeZone("Asia/Seoul")
                .build();
    }

    @Test
    public void setter가없어도_JSON변환이된다() throws Exception {
        //given
        String name = "jojoldu";
        String content = objectMapper.writeValueAsString(new RequestSetterDto(name, 1000L));

        //when
        RequestSetterDto dto = objectMapper.readValue(content, RequestSetterDto.class);

        //then
        assertThat(name).isEqualTo(dto.getName());
    }

    @Test
    void 없는날짜도_변환이_가능하다() throws Exception {
        //given
        String content = "{\"date\":\"2020-02-31\"}";

        //when
        RequestSetterDto dto = objectMapper.readValue(content, RequestSetterDto.class);

        //then
        assertThat(dto.getDate()).isEqualTo(LocalDate.of(2020,2,29));
    }

    @Test
    void 문자열_없는날짜도_변환이_가능하다() throws Exception {
        //when
        LocalDate date = LocalDate.parse("2020-02-31", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        //then
        assertThat(date).isEqualTo(LocalDate.of(2020,2,29));
    }
}
