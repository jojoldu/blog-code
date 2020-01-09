package com.jojoldu.blogcode.springboot.tips.validatemessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class ValidateMessageController {

    @PostMapping("/validate-message")
    public @ResponseBody String xss2 (@Valid @RequestBody ValidateMessageRequestDto requestDto) {
        log.info("requestDto={}", requestDto);

        return "OK";
    }

}
