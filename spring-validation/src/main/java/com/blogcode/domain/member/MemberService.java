package com.blogcode.domain.member;

import com.blogcode.domain.exception.ValidCustomException;
import com.blogcode.domain.member.dto.MemberRequestDto;
import com.blogcode.domain.member.dto.MemberResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long save(MemberRequestDto memberRequestDto){
        verifyDuplicateEmail(memberRequestDto.getEmail());
        return memberRepository.save(memberRequestDto.toEntity()).getId();
    }

    private void verifyDuplicateEmail(String email){
        if(memberRepository.findByEmail(email).isPresent()){
            throw ValidCustomException.DUPLICATE_EMAIL;
        }
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll() {
        return memberRepository
                .findAll()
                .stream()
                .map(MemberResponseDto::new)
                .collect(Collectors.toList());
    }

}
