package com.jojoldu.blogcode.springboot.tips.yearmonth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.blogcode.springboot.tips.config.AppConfig;
import com.jojoldu.blogcode.springboot.tips.setter.RequestDtoSetterController;
import com.jojoldu.blogcode.springboot.tips.setter.RequestSetterDto;
import com.jojoldu.blogcode.springboot.tips.xss.XssRequestDto;
import com.jojoldu.blogcode.springboot.tips.xss.XssRequestDto2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jojoldu@gmail.com on 25/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {YearMonthController.class, AppConfig.class})
public class YearMonthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void get_YearMonth가_치환된다() throws Exception {
        String yearMonth = "2020-08";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("yearMonth", Arrays.asList(yearMonth));

        mvc
                .perform(get("/yearMonth")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(yearMonth)));
    }

    @Test
    public void post_YearMonth가_치환된다() throws Exception {
        String content = "{\"yearMonth\":\"2020-08\"}";
        mvc
                .perform(post("/yearMonth")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("2020-08")));
    }

}
