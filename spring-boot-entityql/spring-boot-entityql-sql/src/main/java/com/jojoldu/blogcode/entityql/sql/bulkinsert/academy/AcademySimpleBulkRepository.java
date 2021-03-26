package com.jojoldu.blogcode.entityql.sql.bulkinsert.academy;

import com.jojoldu.blogcode.entityql.entity.domain.sql.EAcademy;
import com.jojoldu.blogcode.entityql.sql.bulkinsert.SimpleBulkRepository;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jojoldu@gmail.com on 26/03/2021
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Repository
@Transactional
public class AcademySimpleBulkRepository extends SimpleBulkRepository {

    public AcademySimpleBulkRepository(SQLQueryFactory sqlQueryFactory) {
        super(sqlQueryFactory, EAcademy.qAcademy);
    }
}
