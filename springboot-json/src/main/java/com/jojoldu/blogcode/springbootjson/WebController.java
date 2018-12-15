package com.jojoldu.blogcode.springbootjson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created by jojoldu@gmail.com on 2018-12-14
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class WebController {

    @GetMapping("/get")
    public String get(GetModel getModel) {
        log.info("get 요청 데이터 = {}", getModel);

        return "get 성공";
    }



    @PostMapping("/post")
    public String post(@RequestBody JsonModel jsonModel) {
        log.info("get 요청 데이터 = {}", jsonModel);

        return "post 성공";
    }

    @GetMapping("/response")
    public ResponseModel responseModel(GetModel getModel) {
        return new ResponseModel(getModel.getName(), getModel.getRequestDateTime());
    }
}
