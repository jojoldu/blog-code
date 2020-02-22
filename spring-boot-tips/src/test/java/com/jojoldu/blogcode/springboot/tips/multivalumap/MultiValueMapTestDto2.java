package com.jojoldu.blogcode.springboot.tips.multivalumap;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Created by jojoldu@gmail.com on 22/02/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Getter
@NoArgsConstructor
public class MultiValueMapTestDto2 {

    @JsonProperty("na") // json 필드명
    private String name;
    private long amount;
    private boolean checked;

    @JsonProperty("date_time") // json 필드명
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime dateTime;

    @JsonProperty("st") // json 필드명
    private Status status;

    @Builder
    public MultiValueMapTestDto2(String name, long amount, boolean checked, LocalDateTime dateTime, Status status) {
        this.name = name;
        this.amount = amount;
        this.checked = checked;
        this.dateTime = dateTime;
        this.status = status;
    }

    public enum Status {
        SUCCESS, FAIL;
    }
}
