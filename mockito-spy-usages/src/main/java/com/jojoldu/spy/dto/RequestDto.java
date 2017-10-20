package com.jojoldu.spy.dto;

import com.jojoldu.spy.exception.EmptyFieldException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@NoArgsConstructor
public class RequestDto {

    private String name;
    private String serviceNo;

    public void verifyEmptyField(){
        if(StringUtils.isEmpty(this.name) || StringUtils.isEmpty(this.serviceNo)){
            throw new EmptyFieldException();
        }
    }
}
