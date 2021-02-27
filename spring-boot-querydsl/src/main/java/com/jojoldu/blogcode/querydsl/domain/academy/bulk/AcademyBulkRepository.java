package com.jojoldu.blogcode.querydsl.domain.academy.bulk;

import com.google.common.collect.Lists;
import com.jojoldu.blogcode.querydsl.config.EntityMapper;
import com.jojoldu.blogcode.querydsl.domain.academy.Academy;
//import com.jojoldu.blogcode.querydsl.domain.sql.EAcademy;
//import com.jojoldu.blogcode.querydsl.domain.sql.EStudent;
import com.jojoldu.blogcode.querydsl.domain.student.Student;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLInsertClause;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Repository
@Transactional // 조회는 사용하지 않는다.
public class AcademyBulkRepository {
    private static final Integer DEFAULT_CHUNK_SIZE = 1_000;

    private final AcademyBulkMatcherRepository txItemCollectorRepository;
    private final SQLQueryFactory sqlQueryFactory;

//    public void saveAll(List<Academy> entities) {
//        saveAll(entities, DEFAULT_CHUNK_SIZE);
//    }
//    /**
//     * 컨셉은 다음과 같다.
//     * Academy을 BulkInsert 하면 Student과 이 어떤 Academy과 매칭되야할지 알 수가 없다.
//     * 그래서 Academy을 BulkInsert하기전에 유니크키를 발급하여 임시 컬럼에 보관, save후 반환된 Academy의 ID를 가지고 Student과 을 BulkInsert 한다.
//     *
//     * 로직)
//     *
//     * 1. Academy을 save하기 전에 UUID를 발급한다 (해당 UUID는 **트랜잭션 내에서만** 유니크하면 된다)
//     * 2. 유니크 키를 가진 Academy을 **복사하여** 별도의 컬렉션 (AcademyUniqueMatcher)에 저장한다. (복사본이 아니면 레퍼런스 관계가 된다)
//     * 3. Academy의 details와 additions을 clear하여 Academy만 BulkInsert 한다.
//     * 4. BulkInsert하고 나온 Academy의 유니크키와 컬렉션에 담긴 유니크키를 비교하여 Student 을 찾는다
//     * 5. Student과 Academy을 연관시킨 후, Bulk Insert한다.
//     */
//
//    public void saveAll(List<Academy> entities, int chunkSize) {
//        AcademyUniqueMatcher matcher = new AcademyUniqueMatcher(entities);
//        List<List<Academy>> subSets = Lists.partition(matcher.getAcademies(), chunkSize);
//
//        int index=1;
//        for (List<Academy> subSet : subSets) {
//
//            LocalDateTime now = LocalDateTime.now();
//            List<Academy> savedItems = insertAcademies(subSet, now);
//            insertChildren(matcher, savedItems, now);
//            log.info("{}번째 처리 - {}건", index++, subSet.size());
//        }
//    }
//
//    // id가 발급된 Academy
//    public List<Academy> insertAcademies(List<Academy> subSet, LocalDateTime now) {
//        SQLInsertClause insert = sqlQueryFactory.insert(EAcademy.qAcademy);
//        for (Academy txItem : subSet) {
//            txItem.setCurrentTime(now);
//            insert.populate(txItem, EntityMapper.DEFAULT).addBatch();
//        }
//        // executeWithKeys: BulkInsert한 결과 중 단일 컬럼에 한해서 반환해준다.
//        List<Long> ids = insert.executeWithKeys(EAcademy.qAcademy.id);
//
//        insert.clear();
//
//        return txItemCollectorRepository.findAllByIds(ids).stream()
//                .map(AcademyUniqueKeyDto::toEntity)
//                .collect(toList());
//    }
//
//    void insertChildren(AcademyUniqueMatcher matcher, List<Academy> savedItems, LocalDateTime now) {
//        SQLInsertClause insert = sqlQueryFactory.insert(EStudent.qStudent);
//
//        int detailCount = 0;
//
//        for (Academy savedAcademy : savedItems) {
//            Academy txItemHasChild = matcher.get(savedAcademy.getMatchKey())
//                    .orElseThrow(() -> new IllegalStateException(notFoundMessage(savedAcademy)));
//
//            detailCount += insertStudents(insert, now, savedAcademy, txItemHasChild.getStudents());
//        }
//
//        if(detailCount > 0) {
//            insert.execute();
//            insert.clear();
//        }
//    }
//
//    private int insertStudents(SQLInsertClause studentInsert, LocalDateTime now, Academy savedAcademy, List<Student> students) {
//        for (Student student : students) {
//            student.setCurrentTime(now);
//            student.setAcademy(savedAcademy);
//            studentInsert.populate(student, EntityMapper.DEFAULT).addBatch();
//        }
//
//        return students.size();
//    }
//
//    private String notFoundMessage(Academy entity) {
//        return format("존재하지 않는 matchKey입니다. serviceAmountDesc=%s, id=%d",
//                entity.getMatchKey(),
//                entity.getId());
//    }


}
