package com.blogcode.domain.member.dto;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class ValidTestDto {

    @NotEmpty
    private String notEmpty;

    @NotBlank
    private String notBlank;

    @NotNull
    private String notNull;

    public ValidTestDto() {}

    public ValidTestDto(String notEmpty, String notBlank, String notNull) {
        this.notEmpty = notEmpty;
        this.notBlank = notBlank;
        this.notNull = notNull;
    }

    public String getNotEmpty() {
        return notEmpty;
    }

    public String getNotBlank() {
        return notBlank;
    }

    public String getNotNull() {
        return notNull;
    }
}
