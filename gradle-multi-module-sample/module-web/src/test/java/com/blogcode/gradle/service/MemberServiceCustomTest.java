package com.blogcode.gradle.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 13.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceCustomTest {

    @Autowired
    private MemberServiceCustom memberServiceCustom;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void signup() {

    }
}
