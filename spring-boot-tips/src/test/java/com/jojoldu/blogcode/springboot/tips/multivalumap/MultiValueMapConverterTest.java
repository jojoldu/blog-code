package com.jojoldu.blogcode.springboot.tips.multivalumap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jojoldu.blogcode.springboot.tips.multivaluemap.MultiValueMapConverter;
import com.jojoldu.blogcode.springboot.tips.multivalumap.MultiValueMapTestDto1.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.jojoldu.blogcode.springboot.tips.multivalumap.MultiValueMapTestDto1.Status.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 22/02/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public class MultiValueMapConverterTest {

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .timeZone("Asia/Seoul")
                .build();
    }

    private String parse(LocalDateTime localDateTime) {
        return DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(localDateTime);
    }
    @Test
    @DisplayName("String/long/boolean/LocalDateTime/enum이 모두 변환된다")
    void test1() throws Exception {
        //given
        String expectedName = "name";
        int expectedAmount = 1000;
        boolean expectedChecked = true;
        LocalDateTime expectedDateTime = LocalDateTime.of(2020, 2, 22, 19, 35, 1);
        Status expectedStatus = SUCCESS;

        MultiValueMapTestDto1 dto = MultiValueMapTestDto1.builder()
                .name(expectedName)
                .amount(expectedAmount)
                .checked(expectedChecked)
                .dateTime(expectedDateTime)
                .status(expectedStatus)
                .build();
        //when
        MultiValueMap<String, String> result = MultiValueMapConverter.convert(objectMapper, dto);

        //then
        assertValue("name", expectedName, result);
        assertValue("amount", String.valueOf(expectedAmount), result);
        assertValue("checked", String.valueOf(expectedChecked), result);
        assertValue("dateTime", parse(expectedDateTime), result);
        assertValue("status", expectedStatus.name(), result);
    }

    private void assertValue(String name, String expectedName, MultiValueMap<String, String> result) {
        assertThat(getValue(result, name)).isEqualTo(expectedName);
    }

    private String getValue(MultiValueMap<String, String> result, String name) {
        return result.get(name).get(0);
    }
}
