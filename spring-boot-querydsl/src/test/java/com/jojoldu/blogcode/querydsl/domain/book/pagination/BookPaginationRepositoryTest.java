package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.jojoldu.blogcode.querydsl.domain.book.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        /**
         * pageNo는 0부터 시작
         */
        List<BookPaginationDto> books = bookPaginationRepository.paginationLegacy(prefixName, 1, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a20");
        assertThat(books.get(9).getName()).isEqualTo("a11");
    }

    @Test
    void nooffsetBuilder() throws Exception {
        //given
        String prefixName = "a";

        for (int i = 1; i <= 30; i++) {
            bookRepository.save(Book.builder()
                    .name(prefixName +i)
                    .bookNo(i)
                    .build());
        }

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationNoOffsetBuilder(null, prefixName, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a30");
        assertThat(books.get(9).getName()).isEqualTo("a21");
    }

    @Test
    void nooffset_첫페이지() throws Exception {
        //given
        String prefixName = "a";

        for (int i = 1; i <= 30; i++) {
            bookRepository.save(Book.builder()
                    .name(prefixName +i)
                    .bookNo(i)
                    .build());
        }

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationNoOffset(null, prefixName, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a30");
        assertThat(books.get(9).getName()).isEqualTo("a21");
    }

    @Test
    void nooffset_두번째페이지() throws Exception {
        //given
        String prefixName = "a";

        for (int i = 1; i <= 30; i++) {
            bookRepository.save(Book.builder()
                    .name(prefixName +i)
                    .bookNo(i)
                    .build());
        }

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationNoOffset(21L, prefixName, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a20");
        assertThat(books.get(9).getName()).isEqualTo("a11");
    }

    @Test
    void 커버링인덱스() throws Exception {
        //given
        String prefixName = "a";

        for (int i = 1; i <= 30; i++) {
            bookRepository.save(Book.builder()
                    .name(prefixName +i)
                    .bookNo(i)
                    .build());
        }

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationCoveringIndex(prefixName, 1, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a20");
        assertThat(books.get(9).getName()).isEqualTo("a11");
    }

    @Test
    void 커버링인덱스_jdbcTemplate() throws Exception {
        //given
        String prefixName = "a";

        for (int i = 1; i <= 30; i++) {
            bookRepository.save(Book.builder()
                    .name(prefixName +i)
                    .bookNo(i)
                    .build());
        }

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationCoveringIndexSql(prefixName, 1, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a20");
        assertThat(books.get(9).getName()).isEqualTo("a11");
    }
}
