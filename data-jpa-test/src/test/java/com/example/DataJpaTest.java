package com.example;

import com.example.domain.Member;
import com.example.domain.Post;
import com.example.repository.MemberRepository;
import com.example.repository.PostRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2016-08-28.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
public class DataJpaTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    private Member member;
    private Post post;

    @Before
    public void setup() throws Exception {
        /*
            굳이 객체생성을 @Before 메소드에 포함시키는 이유는 여러 테스트들이 독립적으로 member와 post값을 가지게 하기 위해서이다.
         */
        member = new Member("jojoldu@gmail.com", new ArrayList<>());
        post = new Post("content", LocalDateTime.now());
        member.addPost(post);
    }

    @Test
    public void test_ManyToOne없이호출() throws Exception {
        Member member2 = new Member("test@gmail.com", new ArrayList<>());
        member2.addPost(post);

        memberRepository.save(member);
        memberRepository.save(member2);

        assertThat(member.getPosts().get(0).getContent(), is("content")); // 1번 사용자의 1번 포스트가 post인지 확인
        assertThat(member2.getPosts().get(0).getContent(), is("content")); // 2번 사용자의 1번 포스트가 post인지 확인
    }
}
