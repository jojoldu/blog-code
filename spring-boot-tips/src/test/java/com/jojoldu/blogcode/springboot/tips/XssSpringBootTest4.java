package com.jojoldu.blogcode.springboot.tips;

import com.jojoldu.blogcode.springboot.tips.xss.XssRequestDto;
import com.jojoldu.blogcode.springboot.tips.xss.XssRequestDto2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by jojoldu@gmail.com on 25/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = "test.type=4")
public class XssSpringBootTest4 {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 태그_치환() {
        String content = "<li>content</li>";
        String expected = "&lt;li&gt;content&lt;/li&gt;";
        ResponseEntity<XssRequestDto> response = restTemplate.postForEntity(
                "/xss",
                new XssRequestDto(content),
                XssRequestDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent()).isEqualTo(expected);
    }

    @Test
    public void application_form_전송() {
        String content = "<li>content</li>";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("content", content);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange("/form",
                HttpMethod.POST,
                entity,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(content);
    }

    @Test
    public void LocalDate가_치환된다() throws Exception {
        String content = "<li>content</li>";
        String expected = "&lt;li&gt;content&lt;/li&gt;";
        LocalDate requestDate = LocalDate.of(2019,12,29);
        ResponseEntity<XssRequestDto2> response = restTemplate.postForEntity(
                "/xss2",
                new XssRequestDto2(content, requestDate),
                XssRequestDto2.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent()).isEqualTo(expected);
        assertThat(response.getBody().getRequestDate()).isEqualTo(requestDate);
    }
}
