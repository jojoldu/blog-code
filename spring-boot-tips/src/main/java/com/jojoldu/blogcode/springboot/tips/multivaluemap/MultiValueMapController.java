package com.jojoldu.blogcode.springboot.tips.multivaluemap;

import com.jojoldu.blogcode.springboot.tips.setter.RequestSetterDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jojoldu@gmail.com on 22/02/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Slf4j
@RequiredArgsConstructor
@RestController
public class MultiValueMapController {

    @GetMapping("/mvm")
    public String get (Request request) {
        log.info("request body= {}", request.toString());
        return request.toString();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        private List<String> name;

        @Override
        public String toString() {
            return String.join(", ", name);
        }
    }
}
