package com.jojoldu.blogcode.querydsl.domain.pointevent;

import com.jojoldu.blogcode.querydsl.domain.academy.Academy;
import com.jojoldu.blogcode.querydsl.dto.AcademyTeacher;
import com.jojoldu.blogcode.querydsl.dto.PointCalculateAmount;
import com.jojoldu.blogcode.querydsl.dto.StudentCount;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
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

/**
 * Created by jojoldu@gmail.com on 2018-12-29
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RequiredArgsConstructor
public class PointEventRepositoryImpl implements PointEventRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PointCalculateAmount> calculateAmounts() {
        return queryFactory
                .select(Projections.fields(PointCalculateAmount.class,
                        new CaseBuilder()
                                .when(pointEvent.pointStatus.in(PointStatus.USE, PointStatus.USE_CANCEL))
                                .then(pointEvent.pointAmount.multiply(-1))
                                .otherwise(pointEvent.pointAmount).as("pointAmount"),
                        pointEvent.pointStatus
                ))
                .from(pointEvent)
                .fetch();
    }
}
