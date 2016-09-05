package com.example;

import com.example.controller.HomeController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Created by jojoldu@gmail.com on 2016-09-03.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest(HomeController.class)
public class WebMvcTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void test_샘플() throws Exception {
        this.mvc.perform(get("/hello").accept(MediaType.TEXT_PLAIN)) // /hello 라는 url로 text/plain 타입을 요청
                .andExpect(status().isOk()) // 위 요청에 따라 결과가 status는 200이며
                .andExpect(content().string("Hello World"));  // response body에 "Hello World" 가 있는지 검증
    }

}
