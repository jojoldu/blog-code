package com.jojoldu.blogcode.springboot.tips.xss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class XssRequestController {

    @GetMapping("/")
    public String index () {
        return "index";
    }

    @PostMapping("/xss")
    public @ResponseBody XssRequestDto xss (@RequestBody XssRequestDto requestDto) {
        log.info("requestDto={}", requestDto);

        return requestDto;
    }

    @PostMapping("/xss2")
    public @ResponseBody XssRequestDto2 xss2 (@RequestBody XssRequestDto2 requestDto) {
        log.info("requestDto={}", requestDto);

        return requestDto;
    }

    @PostMapping("/form")
    public @ResponseBody String form (XssRequestDto requestDto) {
        log.info("requestDto={}", requestDto);
        return requestDto.getContent();
    }
}
