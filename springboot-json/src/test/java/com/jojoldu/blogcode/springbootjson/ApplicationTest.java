package com.jojoldu.blogcode.springbootjson;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void ModelAttribute의_LocalDate는_변환된다() throws Exception {
        //given
        String url = "/get?name=jojoldu&requestDateTime=2018-12-15T10:00:00";

        //when
        ResultActions resultActions = this.mockMvc.perform(get(url));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("get 성공")));

    }

}

