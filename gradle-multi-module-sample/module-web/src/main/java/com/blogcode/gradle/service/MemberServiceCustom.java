package com.blogcode.gradle.service;

import com.blogcode.gradle.domain.Member;
import com.blogcode.gradle.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 13.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Service
@Transactional
public class MemberServiceCustom {

    private MemberRepository memberRepository;

    public MemberServiceCustom(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public Long singup (Member member) {
        return memberRepository.save(member).getId();
    }
}
