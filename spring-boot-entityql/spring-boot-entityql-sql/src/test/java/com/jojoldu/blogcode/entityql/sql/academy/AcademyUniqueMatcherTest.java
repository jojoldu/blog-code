package com.jojoldu.blogcode.entityql.sql.academy;

import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import com.jojoldu.blogcode.entityql.sql.bulkinsert.academy.AcademyUniqueMatcher;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class AcademyUniqueMatcherTest {

    @Test
    void Academy마다_유니크키가_생성된다() throws Exception {
        //given
        int size = 1_000;
        List<Academy> academies = IntStream.rangeClosed(1, size)
                .mapToObj(i -> Academy.builder().build())
                .collect(Collectors.toList());

        //when
        AcademyUniqueMatcher matcher = new AcademyUniqueMatcher(academies);

        //then
        assertThat(matcher.getMap()).hasSize(size);
        assertThat(matcher.getAcademies().get(0)).isNotNull();
    }
}
