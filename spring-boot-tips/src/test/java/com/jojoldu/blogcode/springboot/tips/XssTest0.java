package com.jojoldu.blogcode.springboot.tips;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.blogcode.springboot.tips.web.XssRequestController;
import com.jojoldu.blogcode.springboot.tips.web.dto.XssRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jojoldu@gmail.com on 19/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {XssRequestController.class})
public class XssTest0 {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 태그가_치환되지않는다() throws Exception {
        String content = "<li>content</li>";
        String expected = "\"&lt;li&gt;content&lt;/li&gt;\"";
        String requestBody = objectMapper.writeValueAsString(new XssRequestDto(content));
        mvc
                .perform(post("/xss")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    public void index페이지_호출() throws Exception {
        mvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Spring Boot Tips")));
    }
}
