package com.example;

import com.example.domain.Comment;
import com.example.domain.Member;
import com.example.domain.Post;
import com.example.repository.CommentRepository;
import com.example.repository.MemberRepository;
import com.example.repository.PostRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

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

    //Repository == dao
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Member member;
    private Post post;
    private Comment comment;

    @Before
    public void setup() throws Exception {
        /*
            굳이 객체생성을 @Before 메소드에 포함시키는 이유는 여러 테스트들이 독립적으로 member와 post값을 가지게 하기 위해서이다.
         */
        member = new Member("jojoldu@gmail.com", new ArrayList<>(), new LinkedHashSet<>());
        post = new Post("content", LocalDateTime.now(), new ArrayList<>());
        comment = new Comment("댓글", LocalDateTime.now());
    }

    @Test
    public void test_Post와Comment관계정의() throws Exception {
        Post savedPost = postRepository.save(post);
        savedPost.addComment(comment); // 글에 댓글 추가
        comment.setPost(savedPost); // 댓글에 글 추가



        Post firstPost = postRepository.findOne(1L);
        Comment firstComment = commentRepository.findOne(1L);

        assertThat(savedPost.getContent(), is("content"));
        assertThat(savedPost.getComments().get(0).getContent(), is(firstComment.getContent()));

    }

    @Test
    public void test_Member와Comment관계정의() throws Exception {
        Post savedPost = postRepository.save(post);
        Member savedMember = memberRepository.save(member);

        savedPost.addComment(comment);
        savedMember.addComment(comment);

        comment.setPostAndMember(savedPost, savedMember);

        commentRepository.save(comment);

        Post afterPost = postRepository.findOne(1L);
        Member afterMember = memberRepository.findOne(1L);

        assertThat(afterPost.getComments().get(0).getContent(), is("댓글"));
        assertThat(afterMember.getComments().get(0).getContent(), is("댓글"));
        assertThat(commentRepository.findAll().size(), is(1)); // savedPost와 savedMember에 각각 addComment를 했지만 결국 comment는 1개가 들어간것을 확인
    }

    @Test
    public void test_Post와Member관계정의() throws Exception {
        Member member2 = new Member("test@gmail.com", new ArrayList<>(), new LinkedHashSet<>());
        Post savedPost = postRepository.save(post);
        member.addPost(savedPost);
        member2.addPost(savedPost);

        Member savedMember = memberRepository.save(member);
        Member savedMember2 = memberRepository.save(member2);

        assertThat(savedMember.getFavorites().stream().findFirst().orElse(new Post()).getContent(), is("content")); // 1번 사용자의 1번 글이 post인지 확인
        assertThat(savedMember2.getFavorites().stream().findFirst().orElse(new Post()).getContent(), is("content")); // 2번 사용자의 1번 글이 post인지 확인
    }

    @Test
    public void test_oneToMany에서Set과List차이() throws Exception {
        Post savedPost = postRepository.save(post);
        member.addPost(savedPost);
        member.addPost(savedPost);

        Member savedMember = memberRepository.save(member);

        assertThat(savedMember.getFavorites().size(), is(1)); // 2개의 Post를 넣었지만 결국 중복된게 제거되서 1개만 등록된것을 확인할수 있다.
    }

}
