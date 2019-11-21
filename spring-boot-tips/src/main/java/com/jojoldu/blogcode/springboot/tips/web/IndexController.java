package com.jojoldu.blogcode.springboot.tips.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by jojoldu@gmail.com on 21/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index () {
        return "index";
    }
}
