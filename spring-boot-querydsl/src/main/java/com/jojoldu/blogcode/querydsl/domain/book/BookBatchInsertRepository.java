package com.jojoldu.blogcode.querydsl.domain.book;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

@Repository
public class BookBatchInsertRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    public BookBatchInsertRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.parameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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

    public void batchInsert() {
        List<String> list = new ArrayList<>();

        String sql =
                "INSERT INTO store (" +
                "tx_year, " +
                "tx_month, " +
                "sales_type, " +
                "amount_sum, " +
                "count, " +
                "customer_no, " +
                "ceo_name, " +
                "customer_biz_no) " +
                "VALUES(:txYear, :txMonth, :salesType, :amountSum, :count, :customerNo, :ceoName, :customerBizNo)";

        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(list.toArray());

        parameterJdbcTemplate.batchUpdate(sql, batch);
    }


}
