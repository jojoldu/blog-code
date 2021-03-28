package com.jojoldu.blogcode.entityql.sql.academy;

import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import com.jojoldu.blogcode.entityql.entity.domain.academy.AcademyRepository;
import com.jojoldu.blogcode.entityql.entity.domain.academy.AcademyStatus;
import com.jojoldu.blogcode.entityql.sql.bulkinsert.academy.AcademyBulkRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by jojoldu@gmail.com on 03/03/2021
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@SpringBootTest
class AcademyBulkRepositoryTest {

    @Autowired
    private AcademyBulkRepository academyBulkRepository;

    @Autowired
    private AcademyRepository academyRepository;

    List<Academy> academies;

    @BeforeEach
    void setUp() {
        academies = IntStream.rangeClosed(1, 10_000)
                .boxed()
                .map(i -> new Academy("name"+i, "address"+i, "010-0000-0000", AcademyStatus.ON))
                .collect(Collectors.toList());
    }

    @AfterEach
    void after() {
        academyRepository.deleteAll();
    }

    @Test
    void jpa_saveAll_test() throws Exception {
        academyRepository.saveAll(academies);
    }

    @Test
    void entytlql_bulk_test() throws Exception {
        academyBulkRepository.saveAll(academies);
    }

    @Test
    void 데이터가_없을경우_insert쿼리가_실행되지않는다() throws Exception {
        academyBulkRepository.saveAll(new ArrayList<>());
    }
}
