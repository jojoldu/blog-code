package com.jojoldu.blogcode.springboot.tips;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.blogcode.springboot.tips.web.XssRequestController;
import com.jojoldu.blogcode.springboot.tips.web.config.AppConfig;
import com.jojoldu.blogcode.springboot.tips.web.config.HtmlCharacterEscapes;
import com.jojoldu.blogcode.springboot.tips.web.dto.XssRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
@Import(value = {AppConfig.class})
public class XssTest2 {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 태그가_치환된다() throws Exception {
        String content = "<li>content</li>";
        String expected = "\"&lt;li&gt;content&lt;/li&gt;\"";
        String requestBody = objectMapper.writeValueAsString(new XssRequestDto(content));
        mvc
                .perform(post("/xss")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(expected)));
    }

    @Test
    public void index페이지_호출() throws Exception {
        mvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Spring Boot Tips")));
    }

    @Configuration
    public static class WebMvcConfig {

        @Bean
        public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());

            return new MappingJackson2HttpMessageConverter(objectMapper);
        }
    }
}
