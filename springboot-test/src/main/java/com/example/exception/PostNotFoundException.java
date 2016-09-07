package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by jojoldu@gmail.com on 2016-09-06.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 NOT_FOUND status
public class PostNotFoundException extends RuntimeException{ // 직접 생성한 Exception

    public PostNotFoundException(long idx) {
        super("could not find post '" + idx + "'.");
    }
}
