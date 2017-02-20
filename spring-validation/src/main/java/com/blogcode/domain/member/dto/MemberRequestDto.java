package com.blogcode.domain.member.dto;

import com.blogcode.domain.member.Member;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class MemberRequestDto {

    private Long id;

    @NotBlank(message = "이름을 작성해주세요.")
    private String name;

    @NotBlank(message = "직업을 작성해주세요.")
    private String job;

    @NotBlank(message = "메일을 작성해주세요.")
    @Email(message = "메일의 양식을 지켜주세요.")
    private String email;

    public MemberRequestDto() {}

    public Member toEntity(){
        return new Member(id, name, job, email);
    }

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
