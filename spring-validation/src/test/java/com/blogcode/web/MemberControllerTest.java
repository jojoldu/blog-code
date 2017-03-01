package com.blogcode.web;

import com.blogcode.domain.member.dto.ValidTestDto;
import com.blogcode.domain.web.MemberController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberControllerTest {

    @Autowired
    private MemberController memberController;

    @Test
    public void NotEmpty와NotBlank와NotNull비교() {
        ValidTestDto 빈문자열 = new ValidTestDto("","","");
        assertThat(memberController.validTest(빈문자열).getNotBlank(), is(""));

        ValidTestDto 스페이스 = new ValidTestDto(" ", " ", " ");
        memberController.validTest(스페이스);
        assertThat(memberController.validTest(스페이스).getNotBlank(), is(" "));

        ValidTestDto 널 = new ValidTestDto(null, null, null);
        assertNull(memberController.validTest(널).getNotBlank());
    }
}
