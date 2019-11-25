package com.jojoldu.blogcode.springboot.tips.web;

import com.jojoldu.blogcode.springboot.tips.web.dto.XssRequestDto;
import com.jojoldu.blogcode.springboot.tips.web.dto.XssRequestDto2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
public class XssRequestController {

    @GetMapping("/")
    public String index () {
        return "index";
    }

    @PostMapping("/xss")
    public @ResponseBody XssRequestDto xss (@RequestBody XssRequestDto xssRequestDto) {
        log.info("requestDto={}", xssRequestDto);

        return xssRequestDto;
    }

    @PostMapping("/xss2")
    public @ResponseBody XssRequestDto2 xss2 (@RequestBody XssRequestDto2 xssRequestDto) {
        log.info("requestDto={}", xssRequestDto);

        return xssRequestDto;
    }
}
