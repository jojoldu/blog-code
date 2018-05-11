package com.jojoldu.blogcode.springbootjpaid;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @After
    public void tearDown() throws Exception {
        bookRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    public void auto_increment_테스트() {
        //given
        bookRepository.save(new Book("book1"));
        memberRepository.save(new Member("member1"));

        //when
        Book book = bookRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        //then
        assertThat(book.getId()).isEqualTo(1L);
        assertThat(member.getId()).isEqualTo(1L);
    }
}
