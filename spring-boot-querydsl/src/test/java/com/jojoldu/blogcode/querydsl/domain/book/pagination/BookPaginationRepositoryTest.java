package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.jojoldu.blogcode.querydsl.domain.book.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 06/09/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BookPaginationRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookPaginationRepository bookPaginationRepository;

    @AfterEach
    void after() {
        bookRepository.deleteAllInBatch();
    }

    @Test
    void 기존_페이징_방식() throws Exception {
        //given
        String prefixName = "a";

        for (int i = 1; i <= 30; i++) {
            bookRepository.save(Book.builder()
                    .name(prefixName +i)
                    .bookNo(i)
                    .build());
        }

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationLegacy(prefixName, 2);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getBookNo()).isEqualTo(20);
        assertThat(books.get(9).getBookNo()).isEqualTo(11);
    }

    @Test
    void nooffsetBuilder_방식1() throws Exception {
        //given
        String prefixName = "a";

        for (int i = 1; i <= 30; i++) {
            bookRepository.save(Book.builder()
                    .name(prefixName +i)
                    .bookNo(i)
                    .build());
        }

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationNoOffsetBuilder(null, prefixName);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getBookNo()).isEqualTo(30);
        assertThat(books.get(9).getBookNo()).isEqualTo(21);
    }

    @Test
    void nooffsetBuilder_방식2() throws Exception {
        //given
        String prefixName = "a";

        for (int i = 1; i <= 30; i++) {
            bookRepository.save(Book.builder()
                    .name(prefixName +i)
                    .bookNo(i)
                    .build());
        }

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationNoOffsetBuilder(20L, prefixName);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getBookNo()).isEqualTo(20);
        assertThat(books.get(9).getBookNo()).isEqualTo(11);
    }
}
