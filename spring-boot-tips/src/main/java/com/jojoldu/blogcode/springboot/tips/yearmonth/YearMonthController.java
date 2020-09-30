package com.jojoldu.blogcode.springboot.tips.yearmonth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.YearMonth;

@Slf4j
@RestController
public class YearMonthController {

    @GetMapping("/yearMonth")
    public YearMonth getYearMonth (YearMonthRequestDto requestDto) {
        log.info("requestDto={}", requestDto);
        return requestDto.getYearMonth();
    }

    @PostMapping("/yearMonth")
    public YearMonth postYearMonth (@Valid @RequestBody YearMonthRequestDto requestDto) {
        log.info("requestDto={}", requestDto);
        return requestDto.getYearMonth();
    }
}
