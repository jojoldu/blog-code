package com.jojoldu.blogcode.springboot.tips.multivalumap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jojoldu.blogcode.springboot.tips.multivaluemap.MultiValueMapController;
import com.jojoldu.blogcode.springboot.tips.multivaluemap.MultiValueMapConverter;
import com.jojoldu.blogcode.springboot.tips.multivaluemap.MultiValueMapTestDto1;
import com.jojoldu.blogcode.springboot.tips.multivaluemap.MultiValueMapTestDto1.Status;
import com.jojoldu.blogcode.springboot.tips.multivaluemap.MultiValueMapTestDto2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.jojoldu.blogcode.springboot.tips.LocalDateTimeUtils.toStringDateTime;
import static com.jojoldu.blogcode.springboot.tips.multivaluemap.MultiValueMapTestDto1.Status.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 22/02/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public class MultiValueMapConverterTest {

    ObjectMapper objectMapper;
    MultiValueMap<String, String> result;

    @BeforeEach
    void setUp() {
        objectMapper = new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .timeZone("Asia/Seoul")
                .build();
    }

    @AfterEach
    void after() {
        result = null;
    }

    @Test
    @DisplayName("그냥 변환하면 실패한다.")
    void fail_test() throws Exception {
        //given
        MultiValueMapTestDto1 dto = MultiValueMapTestDto1.builder()
                .name("name")
                .build();
        //when
        MultiValueMap<String, String> params = objectMapper.convertValue(dto, new TypeReference<MultiValueMap<String, String>>() {});

        //then
    }

    @Test
    @DisplayName("String/long/boolean/LocalDateTime/enum 모두 변환된다")
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
        result = MultiValueMapConverter.convert(objectMapper, dto);

        //then
        assertThat(result.size()).isEqualTo(5);
        assertValue("name", expectedName);
        assertValue("amount", String.valueOf(expectedAmount));
        assertValue("checked", String.valueOf(expectedChecked));
        assertValue("dateTime", toStringDateTime(expectedDateTime));
        assertValue("status", expectedStatus.name());
    }

    @Test
    @DisplayName("@JsonProperty로 지정된 필드명으로 변환된다")
    void test2() throws Exception {
        //given
        String expectedName = "name";
        int expectedAmount = 1000;
        boolean expectedChecked = true;
        LocalDateTime expectedDateTime = LocalDateTime.of(2020, 2, 22, 19, 35, 1);
        MultiValueMapTestDto2.Status expectedStatus = MultiValueMapTestDto2.Status.SUCCESS;

        MultiValueMapTestDto2 dto = MultiValueMapTestDto2.builder()
                .name(expectedName)
                .amount(expectedAmount)
                .checked(expectedChecked)
                .dateTime(expectedDateTime)
                .status(expectedStatus)
                .build();
        //when
        result = MultiValueMapConverter.convert(objectMapper, dto);

        //then
        assertThat(result.size()).isEqualTo(5);
        assertValue("na", expectedName);
        assertValue("date_time", toStringDateTime(expectedDateTime));
        assertValue("st", expectedStatus.name());
    }

    @Test
    @DisplayName("Collection 필드 변환")
    void test3() throws Exception {
        //given
        MultiValueMapController.Request dto = new MultiValueMapController.Request();
        dto.setName(Arrays.asList("a", "b", "c"));

        //when
        result = MultiValueMapConverter.convert(objectMapper, dto);

        //then
        assertThat(result.size()).isEqualTo(1);
    }

    private void assertValue(String name, String expectedName) {
        assertThat(getValue(result, name)).isEqualTo(expectedName);
    }

    private String getValue(MultiValueMap<String, String> result, String name) {
        return result.get(name).get(0);
    }

}
