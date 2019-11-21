package com.jojoldu.blogcode.springboot.tips;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.blogcode.springboot.tips.web.XssRequestController;
import com.jojoldu.blogcode.springboot.tips.web.config.AppConfig;
import com.jojoldu.blogcode.springboot.tips.web.config.HtmlCharacterEscapes;
import com.jojoldu.blogcode.springboot.tips.web.dto.XssRequestDto;
import com.jojoldu.blogcode.springboot.tips.web.dto.XssRequestDto2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jojoldu@gmail.com on 19/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {XssRequestController.class, XssTest1.WebMvcConfig.class, AppConfig.class})
@AutoConfigureMockMvc
public class XssTest1 {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 태그가_치환된다() throws Exception {
        String content = "<li>content</li>";
        String expected = "&lt;li&gt;content&lt;/li&gt;";
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
    public void LocalDate가_치환된다() throws Exception {
        String content = "<li>content</li>";
        String expected = "&lt;li&gt;content&lt;/li&gt;";
        String requestBody = objectMapper.writeValueAsString(new XssRequestDto2(content, LocalDate.now()));
        mvc
                .perform(post("/xss2")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(expected)));
    }

    @Configuration
    @EnableWebMvc
    public static class WebMvcConfig implements WebMvcConfigurer {

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.add(htmlEscapingConverter());
        }

        private HttpMessageConverter<?> htmlEscapingConverter() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());

            MappingJackson2HttpMessageConverter htmlEscapingConverter =
                    new MappingJackson2HttpMessageConverter();
            htmlEscapingConverter.setObjectMapper(objectMapper);

            return htmlEscapingConverter;
        }
    }
}
