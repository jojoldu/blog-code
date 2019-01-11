package com.jojoldu.blogcode.querydsl.domain.academy;

import com.jojoldu.blogcode.querydsl.dto.StudentCount;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.academy.QAcademy.academy;
import static com.jojoldu.blogcode.querydsl.domain.student.QStudent.student;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.jpa.JPAExpressions.select;

/**
 * Created by jojoldu@gmail.com on 2018-12-29
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RequiredArgsConstructor
public class AcademyRepositoryImpl implements AcademyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Academy> findByName(String name) {
        return queryFactory.selectFrom(academy)
                .where(academy.name.eq(name))
                .fetch();
    }

    @Override
    public List<StudentCount> findAllStudentCount() {
        return queryFactory
                .select(Projections.fields(StudentCount.class,
                        academy.name.as("academyName"),
                        ExpressionUtils.as(
                                JPAExpressions.select(count(student.id))
                                        .from(student)
                                        .where(student.academy.eq(academy)),
                                "studentCount")
                ))
                .from(academy)
                .fetch();
    }
}
