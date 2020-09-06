package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.jojoldu.blogcode.querydsl.domain.book.BookPageDto;
import com.jojoldu.blogcode.querydsl.domain.book.BookRepository;
import com.jojoldu.blogcode.querydsl.domain.book.BookRepositorySupport;
import com.jojoldu.blogcode.querydsl.domain.book.BookType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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
public class BookLegacyPaginationTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookRepositorySupport bookRepositorySupport;

    @AfterEach
    void after() {
        bookRepository.deleteAllInBatch();
    }

    @Test
    void 상수값을_사용한다() throws Exception {
        //given
        BookType bookType = BookType.IT;
        for (int i = 1; i <= 30; i++) {
            bookRepository.save(Book.builder()
                    .name("a"+i)
                    .bookNo(i)
                    .bookType(bookType)
                    .build());
        }
        PageRequest request = PageRequest.of(0, 10);

        //when
        List<Book> books = bookRepositorySupport.getBooks(request, bookType)
                .getContent();

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getBookNo()).isEqualTo(30);
        assertThat(books.get(0).getBookType()).isEqualTo(bookType);
    }
}
