package com.jojoldu.blogcode.springboot.tips;

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
import org.springframework.test.context.junit4.SpringRunner;

import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by jojoldu@gmail.com on 25/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class XssSpringBootTest99 {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void LocalDate가_치환된다() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //language=JSON
        String requestBody = "{\"content\": \"content\", \"requestDate\": \"2019-12-29\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<XssRequestDto2> response = restTemplate.exchange(
                "/xss2",
                HttpMethod.POST,
                entity,
                XssRequestDto2.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getRequestDate()).isEqualTo(of(2019,12,29));
    }
}
