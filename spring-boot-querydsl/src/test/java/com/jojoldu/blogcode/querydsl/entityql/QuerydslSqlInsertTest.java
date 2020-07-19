package com.jojoldu.blogcode.querydsl.entityql;

import com.jojoldu.blogcode.querydsl.config.EntityMapper;
import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.jojoldu.blogcode.querydsl.domain.book.BookRepository;
import com.jojoldu.blogcode.querydsl.domain.book.BookType;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointEvent;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointEventRepository;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointStatus;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.BeanMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.sql.EBook.qBook;
import static com.jojoldu.blogcode.querydsl.domain.sql.EPointEvent.qPointEvent;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 19/07/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class QuerydslSqlInsertTest {

    @Autowired
    private SQLQueryFactory sqlQueryFactory;

    @Autowired
    private PointEventRepository pointEventRepository;

    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void after() {
        pointEventRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
    }

    @Test
    void sqlInsert() throws Exception {
        //given
        long pointAmount = 1000L;
        PointStatus pointStatus = PointStatus.EARN;

        PointEvent entity = PointEvent.builder()
                .pointAmount(pointAmount)
                .pointStatus(pointStatus)
                .build();

        //when
        sqlQueryFactory.insert(qPointEvent)
                .set(qPointEvent.pointAmount, entity.getPointAmount())
                .set(qPointEvent.pointStatus, entity.getPointStatus())
                .execute();

        //then
        List<PointEvent> results = pointEventRepository.findAll();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(1L);
        assertThat(results.get(0).getPointAmount()).isEqualTo(pointAmount);
        assertThat(results.get(0).getPointStatus()).isEqualTo(pointStatus);
    }

    @Test
    void sqlPopulateInsert() throws Exception {
        //given
        Book entity = Book.builder()
                .bookNo(1)
                .bookType(BookType.IT)
                .build();

        //when
        sqlQueryFactory.insert(qBook)
                .populate(entity, EntityMapper.DEFAULT) // BeanMapper.DEFAULT error.
                .execute();

        //then
        List<Book> results = bookRepository.findAll();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getId()).isEqualTo(1L);
        assertThat(results.get(0).getBookType()).isEqualTo(BookType.IT);
    }
}
