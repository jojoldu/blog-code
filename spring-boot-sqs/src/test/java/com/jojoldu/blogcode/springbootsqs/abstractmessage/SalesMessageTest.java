package com.jojoldu.blogcode.springbootsqs.abstractmessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesMessageTest {

    ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .modules(new JavaTimeModule())
            .timeZone("Asia/Seoul")
            .build();

    @Test
    public void Dto_직렬화() throws Exception {
        //given
        SalesMessage child = SalesMessage.builder()
                .customerNo("customerNo")
                .email("test@woowahan.com")
                .startDate(LocalDate.of(2019,7,17))
                .endDate(LocalDate.of(2019,7,17))
                .build();
        String message = objectMapper.writeValueAsString(child);

        //when
        AbstractMessage parent = objectMapper.readValue(message, AbstractMessage.class);

        //then
        SalesMessage castingChild = (SalesMessage) parent;
        assertThat(parent.getMessageType()).isEqualTo(MessageType.ADDITIONAL_TAX);
        assertThat(castingChild.getCustomerNo()).isEqualTo(child.getCustomerNo());
        assertThat(castingChild.getStartDate()).isEqualTo(child.getStartDate());
    }
}
