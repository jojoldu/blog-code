package com.jojoldu.blogcode.springboot.tips;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.blogcode.springboot.tips.web.config.HtmlCharacterEscapes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 25/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(name = "test.type", havingValue = "3")
public class WebMvcConfig3 {

    private final ObjectMapper objectMapper;

    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }
}