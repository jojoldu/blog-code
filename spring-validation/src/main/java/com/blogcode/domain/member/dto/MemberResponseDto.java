package com.blogcode.domain.member.dto;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class MemberResponseDto {
    private Long id;
    private String name;
    private String job;
    private String email;

    public MemberResponseDto() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getEmail() {
        return email;
    }
}
