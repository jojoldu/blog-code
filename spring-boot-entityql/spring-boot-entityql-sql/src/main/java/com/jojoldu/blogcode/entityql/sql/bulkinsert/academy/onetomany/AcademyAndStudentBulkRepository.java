package com.jojoldu.blogcode.entityql.sql.bulkinsert.academy.onetomany;

import com.google.common.collect.Lists;
import com.jojoldu.blogcode.entityql.entity.config.EntityMapper;
import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import com.jojoldu.blogcode.entityql.entity.domain.sql.EAcademy;
import com.jojoldu.blogcode.entityql.entity.domain.sql.EStudent;
import com.jojoldu.blogcode.entityql.entity.domain.student.Student;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLInsertClause;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
@Transactional // 조회는 사용하지 않는다.
public class AcademyAndStudentBulkRepository {
    private static final Integer DEFAULT_CHUNK_SIZE = 1_000; // MySQL 설정에 따라 조정한다.

    private final AcademyMatcherRepository academyMatcherRepository;
    private final SQLQueryFactory sqlQueryFactory;

    public void saveAll(List<Academy> entities) {
        saveAll(entities, DEFAULT_CHUNK_SIZE);
    }

    public void saveAll(List<Academy> entities, int chunkSize) {
        AcademyUniqueMatcher matcher = new AcademyUniqueMatcher(entities);
        List<List<Academy>> subSets = Lists.partition(matcher.getAcademies(), chunkSize);

        int index=1;
        for (List<Academy> subSet : subSets) { // 10만건 save가 필요하면 1천건씩 나눠서 bulk insert

            LocalDateTime now = LocalDateTime.now(); // 1천건 단위로 audit time 갱신
            List<Academy> savedItems = insertAcademies(subSet, now);
            insertStudents(matcher, savedItems, now);
            log.info("{}번째 처리 - {}건", index++, subSet.size());
        }
    }

    // id가 발급된 Academy
    public List<Academy> insertAcademies(List<Academy> academies, LocalDateTime now) {
        SQLInsertClause insert = sqlQueryFactory.insert(EAcademy.qAcademy);

        for (Academy academy : academies) {
            academy.setCurrentTime(now);
            insert.populate(academy, EntityMapper.DEFAULT).addBatch();
        }

        // executeWithKeys: BulkInsert한 결과 중 단일 컬럼에 한해서 반환해준다.
        List<Long> ids = insert.executeWithKeys(EAcademy.qAcademy.id);

        return academyMatcherRepository.findAllByIds(ids);
    }

    void insertStudents(AcademyUniqueMatcher matcher, List<Academy> idAcademies, LocalDateTime now) {
        SQLInsertClause insert = sqlQueryFactory.insert(EStudent.qStudent);

        for (Academy idAcademy : idAcademies) {
            for (Student student : matcher.get(idAcademy, now)) {
                insert.populate(student, EntityMapper.DEFAULT).addBatch();
            }
        }

        // count가 없을때 insert가 실행되면 values가 없는 쿼리가 수행되어 Exception 발생으로 트랜잭션 롤백 된다.
        if(!insert.isEmpty()) {
            insert.execute();
            insert.clear();
        }
    }
}
