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
    }

    @Test
    public void test_Post와Member관계정의() throws Exception {
        Member member2 = new Member("test@gmail.com", new ArrayList<>());
        Post savedPost = postRepository.save(post);
        member.addPost(savedPost);
        member2.addPost(savedPost);

        Member savedMember = memberRepository.save(member);
        Member savedMember2 = memberRepository.save(member2);

        assertThat(savedMember.getFavorites().get(0).getContent(), is("content")); // 1번 사용자의 1번 포스트가 post인지 확인
        assertThat(savedMember2.getFavorites().get(0).getContent(), is("content")); // 2번 사용자의 1번 포스트가 post인지 확인
    }

    @Test
    public void test_oneToMany에서Set과List차이() throws Exception {
        Post savedPost = postRepository.save(post);
        member.addPost(savedPost);
        member.addPost(savedPost);

        Member savedMember = memberRepository.save(member);

        assertThat(savedMember.getFavorites().size(), is(1)); // favorites가 List 타입이라 중복없이 받아 동일한 Post 2개가 들어가있는것을 확인할 수 있다.
    }

}
