package com.jojoldu.blogcode.querydsl.domain.academy;

import com.jojoldu.blogcode.querydsl.domain.pointevent.PointStatus;
import com.jojoldu.blogcode.querydsl.dto.AcademyTeacher;
import com.jojoldu.blogcode.querydsl.dto.PointCalculateAmount;
import com.jojoldu.blogcode.querydsl.dto.StudentCount;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.academy.QAcademy.academy;
import static com.jojoldu.blogcode.querydsl.domain.pointevent.QPointEvent.pointEvent;
import static com.jojoldu.blogcode.querydsl.domain.student.QStudent.student;
import static com.jojoldu.blogcode.querydsl.domain.teacher.QTeacher.teacher;
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

    @Override
    public List<Academy> findAllByStudentId(long studentId) {
        return queryFactory
                .selectFrom(academy)
                .where(academy.id.in(
                        JPAExpressions
                                .select(student.academy.id)
                                .from(student)
                                .where(student.id.eq(studentId))))
                .fetch();
    }

    @Override
    public List<AcademyTeacher> findAllAcademyTeacher() {
        return queryFactory
                .select(Projections.fields(AcademyTeacher.class,
                        academy.name.as("academyName"),
                        teacher.name.as("teacherName")
                ))
                .from(academy)
                .join(teacher).on(academy.id.eq(teacher.academyId))
                .fetch();
    }

    @Override
    public List<PointCalculateAmount> calculateAmounts() {
        return queryFactory
                .select(Projections.fields(PointCalculateAmount.class,
                        new CaseBuilder()
                                .when(pointEvent.pointStatus.in(PointStatus.USE, PointStatus.USE_CANCEL))
                                .then(pointEvent.pointAmount.multiply(-1))
                                .otherwise(pointEvent.pointAmount).as("pointAmount")
                ))
                .from(pointEvent)
                .fetch();
    }

    @Override
    public List<Academy> findDynamicQuery(String name, String address, String phoneNumber) {

        BooleanBuilder builder = new BooleanBuilder();

        if (!StringUtils.isEmpty(name)) {
            builder.and(academy.name.eq(name));
        }
        if (!StringUtils.isEmpty(address)) {
            builder.and(academy.address.eq(address));
        }
        if (!StringUtils.isEmpty(phoneNumber)) {
            builder.and(academy.phoneNumber.eq(phoneNumber));
        }

        return queryFactory
                .selectFrom(academy)
                .where(builder)
                .fetch();
    }

    @Override
    public List<Academy> findDynamicQueryAdvance(String name, String address, String phoneNumber) {
        return queryFactory
                .selectFrom(academy)
                .where(eqName(name),
                        eqAddress(address),
                        eqPhoneNumber(phoneNumber))
                .fetch();
    }

    private BooleanExpression eqName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return academy.name.eq(name);
    }

    private BooleanExpression eqAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return null;
        }
        return academy.address.eq(address);
    }

    private BooleanExpression eqPhoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return null;
        }
        return academy.phoneNumber.eq(phoneNumber);
    }


}
