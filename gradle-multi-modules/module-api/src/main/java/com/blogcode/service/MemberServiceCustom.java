package com.blogcode.service;

import com.blogcode.domain.Member;
import com.blogcode.repository.MemberRepository;
import org.springframework.stereotype.Service;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 14.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Service
public class MemberServiceCustom {

    private MemberRepository memberRepository;

    public MemberServiceCustom(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long signup (Member member) {
        return memberRepository.save(member).getId();
    }
}
