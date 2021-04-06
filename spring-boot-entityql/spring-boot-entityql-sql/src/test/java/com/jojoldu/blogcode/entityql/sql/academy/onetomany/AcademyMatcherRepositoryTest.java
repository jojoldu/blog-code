package com.jojoldu.blogcode.entityql.sql.academy.onetomany;

import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import com.jojoldu.blogcode.entityql.entity.domain.academy.AcademyRepository;
import com.jojoldu.blogcode.entityql.entity.domain.academy.AcademyStatus;
import com.jojoldu.blogcode.entityql.sql.bulkinsert.academy.onetomany.AcademyMatcherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 07/04/2021
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AcademyMatcherRepositoryTest {

    @Autowired
    private AcademyRepository academyRepository;

    @Autowired
    private AcademyMatcherRepository academyMatcherRepository;

    @AfterEach
    void after() {
        academyRepository.deleteAll();
    }

    @Test
    void Entity가_아닌_Dto로_조회된다() throws Exception {
        //given
        String matchKey = "matchKey";
        Academy saved = academyRepository.save(new Academy(matchKey, "name", "address", "010-0000-0000", AcademyStatus.ON));
        Long savedId = saved.getId();

        //when
        List<Academy> result = academyMatcherRepository.findAllByIds(Arrays.asList(savedId));

        //then
        assertThat(result.get(0).getId()).isEqualTo(savedId);
        assertThat(result.get(0).getMatchKey()).isEqualTo(matchKey);
        assertThat(result.get(0).getName()).isNull();
    }
}
