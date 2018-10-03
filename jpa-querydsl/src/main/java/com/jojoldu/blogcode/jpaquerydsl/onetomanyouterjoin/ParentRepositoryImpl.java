package com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin.QChild.child;
import static com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin.QParent.parent;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

/**
 * Created by jojoldu@gmail.com on 2018. 10. 2.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RequiredArgsConstructor
public class ParentRepositoryImpl implements ParentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /**
     * 실패 케이스
     */
//    @Override
//    public List<Family> findFamily() {
//        return queryFactory
//                .select(Projections.fields(Family.class,
//                        parent.name,
//                        parent.children
//                ))
//                .from(parent)
//                .leftJoin(parent.children, child)
//                .fetch();
//    }

    @Override
    public List<Family> findFamily() {
        Map<String, List<Child>> transform = queryFactory
                .from(parent)
                .leftJoin(parent.children, child)
                .transform(groupBy(parent.name).as(list(child)));

        return transform.entrySet().stream()
                .map(entry -> new Family(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

//    @Override
//    public List<Family> findFamily() {
//        List<Parent> parents = queryFactory
//                .selectFrom(parent)
//                .leftJoin(parent.children, child).fetchJoin()
//                .fetch();
//
//        return parents.stream()
//                .map(p -> new Family(p.getName(), p.getChildren()))
//                .collect(Collectors.toList());
//    }
}
