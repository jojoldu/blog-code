package com.jojoldu.blogcode.entityql.sql.bulkinsert;

import com.google.common.collect.Lists;
import com.jojoldu.blogcode.entityql.entity.config.EntityMapper;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLInsertClause;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class SimpleBulkRepository<T> {

    private static final Integer DEFAULT_CHUNK_SIZE = 1_000;

    private final SQLQueryFactory sqlQueryFactory;
    private final RelationalPath<?> relationalPath;

    public void saveAll(List<T> entities) {
        saveAll(entities, DEFAULT_CHUNK_SIZE);
    }

    public void saveAll(List<T> entities, int chunkSize) {
        List<List<T>> subSets = Lists.partition(entities, chunkSize);

        int index = 1;
        for (List<T> subSet : subSets) {
            insertItems(subSet);
            log.info("{}번째 처리 - {}건", index++, subSet.size());
        }
    }

    private void insertItems(List<T> subSet) {
        SQLInsertClause insert = sqlQueryFactory.insert(relationalPath);
        for (T item : subSet) {
            insert.populate(item, EntityMapper.DEFAULT).addBatch();
        }

        if(!insert.isEmpty()) {
            insert.execute();
            insert.clear();
        }
    }
}
