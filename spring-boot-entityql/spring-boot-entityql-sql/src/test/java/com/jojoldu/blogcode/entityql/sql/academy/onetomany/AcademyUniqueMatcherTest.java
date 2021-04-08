package com.jojoldu.blogcode.entityql.sql.academy.onetomany;

import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import com.jojoldu.blogcode.entityql.entity.domain.academy.AcademyStatus;
import com.jojoldu.blogcode.entityql.entity.domain.student.Student;
import com.jojoldu.blogcode.entityql.sql.bulkinsert.academy.onetomany.AcademyUniqueMatcher;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
                .mapToObj(i -> new Academy())
                .collect(Collectors.toList());

        //when
        AcademyUniqueMatcher matcher = new AcademyUniqueMatcher(academies);

        //then
        assertThat(matcher.getMap()).hasSize(size);
        assertThat(matcher.getAcademies().get(0)).isNotNull();
    }

    @Test
    void academyId로_student를_찾는다() throws Exception {
        //given
        List<Academy> academies = IntStream.rangeClosed(1, 100)
                .boxed()
                .map(i -> new Academy(String.valueOf(i), "address"+i, "010-0000-0000", AcademyStatus.ON, new Student(String.valueOf(i), i)))
                .collect(Collectors.toList());

        AcademyUniqueMatcher matcher = new AcademyUniqueMatcher(academies);

        for (Academy academy : academies) {
            List<Student> students = matcher.get(academy, LocalDateTime.now());
            assertThat(students.size()).isEqualTo(1);

            Student student = students.get(0);
            assertThat(student.getAcademy().getName()).isEqualTo(academy.getName());
            assertThat(student.getName()).isEqualTo(academy.getName());
        }

    }
}
