package com.jojoldu.blogcode.entityql.sql.bulkinsert.academy.onetomany;

import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.jojoldu.blogcode.entityql.entity.domain.academy.QAcademy.academy;
import static com.querydsl.core.types.Projections.fields;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class AcademyMatcherRepository {

    private final JPAQueryFactory queryFactory;

    public List<Academy> findAllByIds(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)) {
            throw new IllegalArgumentException("조회할 id가 없습니다.");
        }

        return queryFactory
                .select(fields(Academy.class, // 이렇게 할 경우 Entity가 아닌 Dto로 조회됨
                        academy.id,
                        academy.matchKey
                ))
                .from(academy)
                .where(academy.id.in(ids))
                .fetch();
    }
}
