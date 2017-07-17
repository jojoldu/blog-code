package com.blogcode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 17.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private List<Member> members;

    @Before
    public void before() {
        members = new ArrayList<>();

        for(int i=0; i<10;i++){
            members.add(new Member("name_"+i));
        }
    }

    @Test
    public void 트랜잭션_없는경우 () throws Exception {
        before();
        memberService.saveNoTransactional(members);
        List<Member> resultMembers = memberRepository.findAll();

        assertThat(resultMembers.size(), is(5));
    }
}
