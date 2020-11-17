package com.jojoldu.blogcode.querydsl.domain.book;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

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
    void test_jpaRepository_exist() throws Exception {
        //given
        String name = "a";
        bookRepository.save(Book.builder()
                .name(name)
                .build());
        //when
        boolean exist = bookRepository.existsByName(name);

        //then
        assertThat(exist).isTrue();
    }

    @Test
    void test_jpaRepository_exist2() throws Exception {
        //given
        Long bookId = bookRepository.save(Book.builder()
                .name("a")
                .build())
                .getId();

        //when
        boolean exist = bookRepository.exist(bookId);

        //then
        assertThat(exist).isTrue();
    }

    @Test
    void test_querydsl_exist() throws Exception {
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

    @Test
    void test_querydsl_exist_zero_count() throws Exception {
        //given
        //when
        Boolean exist = bookQueryRepository.exist(1L);

        //then
        assertThat(exist).isFalse();
    }

    @Test
    void 상수값을_사용한다() throws Exception {
        //given
        int bookNo = 1;
        for (int i = 1; i <= 10; i++) {
            bookRepository.save(Book.builder()
                    .name("a"+i)
                    .bookNo(bookNo)
                    .build());
        }

        //when
        List<BookPageDto> bookPages = bookQueryRepository.getBookPage(bookNo, 0);

        //then
        assertThat(bookPages).hasSize(10);
        assertThat(bookPages.get(0).getPageNo()).isEqualTo(0);
        assertThat(bookPages.get(0).getBookNo()).isEqualTo(bookNo);
    }
}
