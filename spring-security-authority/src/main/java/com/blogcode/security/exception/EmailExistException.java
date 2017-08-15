package com.blogcode.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by jojoldu@gmail.com on 2017. 8. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailExistException extends RuntimeException{

    private static final String PREFIX_MESSAGE = "This email already exists: ";

    public EmailExistException(String email) {
        super(PREFIX_MESSAGE + email);
    }
}
