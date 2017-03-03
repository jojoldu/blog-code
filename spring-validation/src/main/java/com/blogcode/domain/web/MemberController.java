package com.blogcode.domain.web;

import com.blogcode.domain.member.MemberService;
import com.blogcode.domain.member.dto.MemberRequestDto;
import com.blogcode.domain.member.dto.MemberResponseDto;
import com.blogcode.domain.member.dto.ValidTestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RestController
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/member")
    public Long saveMember(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        return memberService.save(memberRequestDto);
    }

    @GetMapping("/members")
    public List<MemberResponseDto> findAll(){
        return memberService.findAll();
    }

    @PostMapping("/test")
    public ValidTestDto validTest(@Valid ValidTestDto validTestDto){
        return validTestDto;
    }

}
