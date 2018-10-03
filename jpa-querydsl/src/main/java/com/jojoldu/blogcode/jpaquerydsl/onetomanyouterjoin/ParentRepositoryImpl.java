package com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin.QChild.child;
import static com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin.QParent.parent;

/**
 * Created by jojoldu@gmail.com on 2018. 10. 2.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RequiredArgsConstructor
public class ParentRepositoryImpl implements ParentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * @return
     */
    @Override
    public List<Family> findFamily() {
        return queryFactory
                .select(Projections.fields(Family.class,
                        parent.name,
                        parent.children
                ))
                .from(parent)
                .leftJoin(parent.children, child)
                .fetch();
    }

//    @Override
//    public List<Family> findFamily() {
//        List<Parent> parents = queryFactory
//                .selectFrom(parent)
//                .leftJoin(parent.children, child)
//                .fetch();
//
//        return parents.stream()
//                .map(p -> new Family(p.getName(), p.getChildren()))
//                .collect(Collectors.toList());
//    }
}
