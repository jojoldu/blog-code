package com.jojoldu.blogcode.entityql.sql.bulkinsert.academy;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.academy.QAcademy.academy;
import static com.querydsl.core.types.Projections.fields;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class AcademyBulkMatcherRepository {

    private final JPAQueryFactory queryFactory;

    public List<AcademyUniqueKeyDto> findAllByIds(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("조회할 id가 없습니다.");
        }
        return queryFactory
                .select(fields(AcademyUniqueKeyDto.class,
                        academy.id,
                        academy.matchKey
                ))
                .from(academy)
                .where(academy.id.in(ids))
                .fetch();
    }

}
