package com.jojoldu.blogcode.springboot.tips.web;

import com.jojoldu.blogcode.springboot.tips.web.dto.XssRequestDto;
import com.jojoldu.blogcode.springboot.tips.web.dto.XssRequestDto2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class XssRequestController {

    @PostMapping("/xss")
    public XssRequestDto xss (@RequestBody XssRequestDto xssRequestDto) {
        log.info("requestDto={}", xssRequestDto);

        return xssRequestDto;
    }

    @PostMapping("/xss2")
    public XssRequestDto2 xss2 (@RequestBody XssRequestDto2 xssRequestDto) {
        log.info("requestDto={}", xssRequestDto);

        return xssRequestDto;
    }
}
