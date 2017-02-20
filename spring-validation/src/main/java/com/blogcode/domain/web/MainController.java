package com.blogcode.domain.web;

import com.blogcode.domain.member.MemberService;
import com.blogcode.domain.member.dto.MemberRequestDto;
import com.blogcode.domain.member.dto.ValidTestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Controller
public class MainController {

    private MemberService memberService;

    public MainController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/member")
    @ResponseBody
    public Long saveMember(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        return memberService.save(memberRequestDto);
    }

    @PostMapping("/test")
    @ResponseBody
    public ValidTestDto validTest(@Valid ValidTestDto validTestDto){
        return validTestDto;
    }

}
