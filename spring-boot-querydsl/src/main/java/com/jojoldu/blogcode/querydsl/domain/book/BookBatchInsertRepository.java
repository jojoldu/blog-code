package com.jojoldu.blogcode.querydsl.domain.book;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 04/10/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Repository
public class BookBatchInsertRepository {
    private final JdbcTemplate jdbcTemplate;

    public BookBatchInsertRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public int batchInsert(List<Book> books) {
        int[] inserts = this.jdbcTemplate.batchUpdate(
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

        return inserts.length;
    }


}
