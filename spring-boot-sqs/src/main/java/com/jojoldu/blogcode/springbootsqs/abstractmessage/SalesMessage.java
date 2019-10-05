package com.jojoldu.blogcode.springbootsqs.abstractmessage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ToString
@Getter
public class SalesMessage extends AbstractMessage{
    private String customerNo;
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Builder
    public SalesMessage(String customerNo, String email, LocalDate startDate, LocalDate endDate) {
        this.customerNo = customerNo;
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
