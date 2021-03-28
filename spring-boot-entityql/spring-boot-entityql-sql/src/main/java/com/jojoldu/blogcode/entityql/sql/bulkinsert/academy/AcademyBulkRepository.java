package com.jojoldu.blogcode.entityql.sql.bulkinsert.academy;

import com.google.common.collect.Lists;
import com.jojoldu.blogcode.entityql.entity.config.EntityMapper;
import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import com.jojoldu.blogcode.entityql.entity.domain.sql.EAcademy;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLInsertClause;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 26/03/2021
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Slf4j
@RequiredArgsConstructor
@Repository
@Transactional
public class AcademyBulkRepository {
    private static final Integer DEFAULT_CHUNK_SIZE = 1_000;

    private final SQLQueryFactory sqlQueryFactory;

    public void saveAll(List<Academy> entities) {
        saveAll(entities, DEFAULT_CHUNK_SIZE);
    }

    public void saveAll(List<Academy> entities, int chunkSize) {
        SQLInsertClause insert = sqlQueryFactory.insert(EAcademy.qAcademy);
        // MySQL의 max_allowed_packet을 고려하여 1천건씩 끊어서 처리한다.
        List<List<Academy>> subSets = Lists.partition(entities, chunkSize);

        int index = 1;
        for (List<Academy> subSet : subSets) {
            LocalDateTime now = LocalDateTime.now();
            for (Academy entity : subSet) {
                entity.setCurrentTime(now); // audit가 지원 안되니 직접 구현한다.
                insert.populate(entity, EntityMapper.DEFAULT).addBatch();
            }

            insert.execute();
            insert.clear(); // clear하지 않으면 앞의 데이터가 그대로 저장되어있다.
            log.info("Academy {}번째 처리 - {}건", index++, subSet.size());
        }
    }
}
