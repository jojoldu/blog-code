package com.blogcode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 17.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void saveNoTransactional(List<Member> members){
        save(members);
    }

    @Transactional
    public void saveTransactional(List<Member> members){
        save(members);
    }

    private void save(List<Member> members) {
        int idx=0;
        for(Member member : members) {
            if(idx == 5){
                throw new RuntimeException();
            }else {
                memberRepository.save(member);
            }

            idx++;
        }
    }


}
