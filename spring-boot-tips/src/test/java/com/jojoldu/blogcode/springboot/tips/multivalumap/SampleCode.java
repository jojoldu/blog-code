package com.jojoldu.blogcode.springboot.tips.multivalumap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.blogcode.springboot.tips.multivaluemap.MultiValueMapConverter;
import com.jojoldu.blogcode.springboot.tips.multivaluemap.MultiValueMapTestDto1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.jojoldu.blogcode.springboot.tips.LocalDateTimeUtils.toStringDateTime;

/**
 * Created by jojoldu@gmail.com on 22/02/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@RequiredArgsConstructor
public class SampleCode {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public ResponseEntity<String> requestPost(String url, MultiValueMapTestDto1 requestDto) {
        URI uri = UriComponentsBuilder.fromUriString(url)
                .build().encode()
                .toUri();

        HttpHeaders authHeaders = new HttpHeaders();
        HttpEntity<MultiValueMapTestDto1> httpEntity = new HttpEntity<>(requestDto, authHeaders);

        return restTemplate.exchange(
                uri,
                HttpMethod.POST,
                httpEntity,
                String.class);
    }

    public ResponseEntity<String> requestGetLegacy(String url, MultiValueMapTestDto1 requestDto) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("name", requestDto.getName());
        queryParams.add("amount", String.valueOf(requestDto.getAmount()));
        queryParams.add("checked", String.valueOf(requestDto.isChecked()));
        queryParams.add("dateTime", toStringDateTime(requestDto.getDateTime()));
        queryParams.add("status", requestDto.getStatus().name());

        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParams(queryParams)
                .build().encode()
                .toUri();

        HttpHeaders authHeaders = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(authHeaders);

        return restTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                String.class);
    }

    public ResponseEntity<String> requestGetNew(String url, MultiValueMapTestDto1 requestDto) {
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParams(MultiValueMapConverter.convert(objectMapper, requestDto))
                .build().encode()
                .toUri();

        HttpHeaders authHeaders = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(authHeaders);
        return restTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                String.class);
    }

}
