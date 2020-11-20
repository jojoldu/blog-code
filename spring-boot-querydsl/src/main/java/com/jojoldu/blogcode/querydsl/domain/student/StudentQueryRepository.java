package com.jojoldu.blogcode.querydsl.domain.student;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.student.QStudent.student;


/**
 * Created by jojoldu@gmail.com on 30/07/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RequiredArgsConstructor
@Repository
public class StudentQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Transactional
    public void updateName1 (long studentId, String name) {

        List<Student> students = queryFactory
                .selectFrom(student)
                .where(student.id.loe(studentId))
                .fetch();

        for (Student student : students) {
            student.updateName(name);
        }
    }

    @Transactional
    public void updateName2 (long studentId, String name) {

        queryFactory.update(student)
                .where(student.id.loe(studentId))
                .set(student.name, name)
                .execute();


    }
}
