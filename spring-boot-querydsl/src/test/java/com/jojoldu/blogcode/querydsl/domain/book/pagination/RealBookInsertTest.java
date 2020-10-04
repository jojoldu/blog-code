package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.Book;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    void 테스트용_데이터_insert() throws Exception {
        //given
        for (int i = 0; i < 1000; i++) {
            List<Book> books = new ArrayList<>();
            for (int j = 0; j < 1000; j++) {
                books.add(Book.builder()
                        .name(i+"_"+j)
                        .bookNo(j)
                        .build());
            }
            log.info("{} 번째 Insert", i);
            batchInsert(books);
        }
    }

    public int[] batchInsert(List<Book> books) {
        return this.jdbcTemplate.batchUpdate(
                "insert into book (name, book_no) values(?,?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, books.get(i).getName());
                        ps.setInt(2, books.get(i).getBookNo());
                    }
                    public int getBatchSize() {
                        return books.size();
                    }
                });
    }
}
