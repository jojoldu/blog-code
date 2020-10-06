package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.jojoldu.blogcode.querydsl.domain.book.BookBatchInsertRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 04/10/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "real")
public class RealBookInsertTest {

    @Autowired
    private BookBatchInsertRepository repository;

    @Test
    void 테스트용_데이터_insert() throws Exception {
        //given
        for (int i = 0; i < 10_000; i++) {
            List<Book> books = new ArrayList<>();
            for (int j = 0; j < 10000; j++) {
                books.add(Book.builder()
                        .name(i+"_"+j)
                        .bookNo(j)
                        .build());
            }
            log.info("{} 번째 Insert", i);
            repository.batchInsert(books);
        }
    }
}
