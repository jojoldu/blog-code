package com.blogcode.repository;

import com.blogcode.model.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 12.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void save() {
        memberRepository.save(new Member("jojoldu", "jojoldu@gmail.com"));
        Member saved = memberRepository.findAll().get(0);
        assertThat(saved.getName(), is("jojoldu"));
    }
}
