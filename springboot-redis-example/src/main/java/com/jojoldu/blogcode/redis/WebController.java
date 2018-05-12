package com.jojoldu.blogcode.redis;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Created by jojoldu@gmail.com on 2018. 5. 12.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@AllArgsConstructor
@RestController
public class WebController {

    private MemberRedisRepository memberRedisRepository;

    @GetMapping("/visit")
    public Member visit(String id, String name){
        Optional<Member> optional = memberRedisRepository.findById(id);
        if(optional.isPresent()){
            Member member = optional.get();
            member.visit();
            return memberRedisRepository.save(member);
        }

        return memberRedisRepository.save(new Member(id, name));
    }

    @GetMapping("/list")
    public List<Member> getAll(){
        return (List<Member>) memberRedisRepository.findAll();
    }
}
