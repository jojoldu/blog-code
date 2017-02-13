package com.blogcode.service;

import com.blogcode.domain.Member;
import org.springframework.stereotype.Service;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 14.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Service
public class MemberServiceCustom {

    public Member generate(){
        return new Member("jojoldu", "jojoldu@gmail.com");
    }
}
