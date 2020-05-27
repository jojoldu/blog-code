package com.jojoldu.blogcode.springboot.tips.setter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Slf4j
@Getter
@ToString
public class RequestSetterDto {
    private String name;
    private Long amount;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;

    private RequestType requestType;

    public RequestSetterDto() {
        log.info(">>>>>>>>> create RequestSetterDto");
    }

    @Builder
    public RequestSetterDto(String name, Long amount) {
        this.name = name;
        this.amount = amount;
    }

    public RequestSetterDto(String name, Long amount, LocalDate date, RequestType requestType) {
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.requestType = requestType;
    }

    @RequiredArgsConstructor
    public enum RequestType {
        GET ("get"),
        POST ("post");

        private final String method;
    }
}
