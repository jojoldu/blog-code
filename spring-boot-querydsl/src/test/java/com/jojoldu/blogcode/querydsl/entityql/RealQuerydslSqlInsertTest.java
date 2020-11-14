package com.jojoldu.blogcode.querydsl.entityql;

import com.jojoldu.blogcode.querydsl.config.EntityMapper;
import com.jojoldu.blogcode.querydsl.domain.academy.Academy;
import com.jojoldu.blogcode.querydsl.domain.academy.AcademyRepository;
import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.jojoldu.blogcode.querydsl.domain.book.BookRepository;
import com.jojoldu.blogcode.querydsl.domain.book.BookType;
import com.jojoldu.blogcode.querydsl.domain.sql.EBook;
import com.jojoldu.blogcode.querydsl.domain.student.Student;
import com.jojoldu.blogcode.querydsl.domain.student.StudentRepository;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLInsertClause;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jojoldu.blogcode.querydsl.domain.sql.EAcademy.qAcademy;
import static com.jojoldu.blogcode.querydsl.domain.sql.EStudent.qStudent;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 19/07/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("real")
public class RealQuerydslSqlInsertTest {

    @Autowired
    private SQLQueryFactory sqlQueryFactory;

    @Autowired
    private AcademyRepository academyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void one_jpaSaveAll() throws Exception {
        //when
        for (int i = 1; i <= 10; i++) {
            List<Academy> academies = new ArrayList<>();

            for (int j = 1; j <= 1_000; j++) {
                academies.add(new Academy("address", "name"));
            }

            // 1000개씩 save (Academy 1개당 Student 10개)
            academyRepository.saveAll(academies);
            log.info(i+"번째 saveAll");
        }
    }

    @Test
    void one_sqlPopulateInsert() throws Exception {
        for (int i = 1; i <= 10; i++) {
            SQLInsertClause insert = sqlQueryFactory.insert(qAcademy);
            for (int j = 1; j <= 1_000; j++) {
                insert.populate(new Academy("address", "name"),EntityMapper.DEFAULT).addBatch();
            }
            insert.execute();
            log.info(i+"번째 insert");
        }
    }

    @Test
    void oneToMany_jpaSaveAll() throws Exception {
        //when
        for (int i = 1; i <= 10; i++) {
            List<Academy> academies = new ArrayList<>();

            for (int j = 1; j <= 1_000; j++) {
                Academy academy = new Academy("address", "name");
                academy.addStudent(IntStream.rangeClosed(1, 10)
                        .mapToObj(v -> new Student("student", v))
                        .collect(Collectors.toList())
                );
                academies.add(academy);
            }

            // 1000개씩 save (Academy 1개당 Student 10개)
            academyRepository.saveAll(academies);
            log.info(i+"번째 saveAll");
        }
    }

    @Test
    void oneToMany_sqlPopulateInsert() throws Exception {
        for (int i = 1; i <= 10; i++) {
            SQLInsertClause insert = sqlQueryFactory.insert(qStudent);
            for (int j = 1; j <= 1_000; j++) {
                Academy academy = academyRepository.save(new Academy("address", "name"));
                IntStream.rangeClosed(1, 10)
                        .forEach(n ->
                                insert.populate(new Student("student", n, academy), EntityMapper.DEFAULT)
                                        .addBatch()
                        );
            }
            insert.execute();
            log.info(i+"번째 insert");
        }
    }
}
