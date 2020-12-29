package com.jojoldu.blogcode.querydsl.entityql;

import com.jojoldu.blogcode.querydsl.config.EntityMapper;
import com.jojoldu.blogcode.querydsl.domain.academy.Academy;
import com.jojoldu.blogcode.querydsl.domain.academy.AcademyRepository;
import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.jojoldu.blogcode.querydsl.domain.book.BookRepository;
import com.jojoldu.blogcode.querydsl.domain.book.BookType;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointEvent;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointEventRepository;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointStatus;
import com.jojoldu.blogcode.querydsl.domain.student.Student;
import com.jojoldu.blogcode.querydsl.domain.student.StudentRepository;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.BeanMapper;
import com.querydsl.sql.dml.SQLInsertClause;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.sql.EBook.qBook;
import static com.jojoldu.blogcode.querydsl.domain.sql.EPointEvent.qPointEvent;
import static com.jojoldu.blogcode.querydsl.domain.sql.EStudent.qStudent;
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

    @Autowired
    private AcademyRepository academyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @AfterEach
    void after() {
        pointEventRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
        academyRepository.deleteAll();
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

    @Test
    void sqlOneToManyInsert() throws Exception {
        //given
        String academyName = "name1";
        Academy academy = academyRepository.save(Academy.builder()
                .address("address1")
                .name(academyName)
                .build());

        Student student1 = new Student("student1", 1);
        Student student2 = new Student("student2", 2);
        academy.addStudent(Arrays.asList(student1, student2));

        //when
        SQLInsertClause insert = sqlQueryFactory.insert(qStudent);
        insert.populate(student1, EntityMapper.DEFAULT).addBatch();
        insert.populate(student2, EntityMapper.DEFAULT).addBatch();
        insert.execute();

        //then
        List<Student> students = studentRepository.findAll();
        assertThat(students).hasSize(2);
        assertThat(students.get(0).getAcademy().getId()).isEqualTo(academy.getId());
        assertThat(students.get(1).getAcademy().getId()).isEqualTo(academy.getId());
    }

    @Test
    void superClass() throws Exception {
        //given
        Book entity = Book.builder()
                .bookNo(1)
                .bookType(BookType.IT)
                .build();
        entity.setCurrentTime(LocalDateTime.now());

        //when
        sqlQueryFactory.insert(qBook)
                .populate(entity, EntityMapper.DEFAULT)
                .execute();

        //then
        List<Book> results = bookRepository.findAll();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getBookType()).isEqualTo(BookType.IT);
        assertThat(results.get(0).getCreatedDate()).isNotNull();

    }
}
