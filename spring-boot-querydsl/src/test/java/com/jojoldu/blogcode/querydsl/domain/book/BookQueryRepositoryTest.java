package com.jojoldu.blogcode.querydsl.domain.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 05/08/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BookQueryRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookQueryRepository bookQueryRepository;

    @AfterEach
    void after() {
        bookRepository.deleteAllInBatch();
    }

    @Test
    void test_exist() throws Exception {
        //given
        Long bookId = bookRepository.save(Book.builder()
                .name("a")
                .build())
                .getId();

        //when
        Boolean exist = bookQueryRepository.exist(bookId);

        //then
        assertThat(exist).isTrue();

    }
}
