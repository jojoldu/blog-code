package com.jojoldu.blogcode.entityql.sql.academy.onetomany;

import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import com.jojoldu.blogcode.entityql.entity.domain.academy.AcademyRepository;
import com.jojoldu.blogcode.entityql.entity.domain.academy.AcademyStatus;
import com.jojoldu.blogcode.entityql.entity.domain.student.Student;
import com.jojoldu.blogcode.entityql.entity.domain.student.StudentRepository;
import com.jojoldu.blogcode.entityql.sql.bulkinsert.academy.onetomany.AcademyAndStudentBulkRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 06/04/2021
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@SpringBootTest
class AcademyAndStudentBulkRepositoryTest {

    @Autowired
    private AcademyAndStudentBulkRepository academyAndStudentBulkRepository;

    @Autowired
    private AcademyRepository academyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @AfterEach
    void after() {
        academyRepository.deleteAll();
    }

    @Test
    void oneToMany가_모두_insert된다() throws Exception {
        //given
        //when
        academyAndStudentBulkRepository.saveAll(IntStream.rangeClosed(1, 10)
                .boxed()
                .map(i -> new Academy(String.valueOf(i), "address"+i, "010-0000-0000", AcademyStatus.ON, new Student(String.valueOf(i), i)))
                .collect(Collectors.toList()));

        //then
        List<Academy> academies = academyRepository.findAll();
        List<Student> students = studentRepository.findAll();

        assertThat(academies).hasSize(10);
        assertThat(students).hasSize(10);

        for (int i = 0, academiesSize = academies.size(); i < academiesSize; i++) {
            Academy academy = academies.get(i);
            Student student = students.get(i);
            assertThat(academy.getName()).isEqualTo(student.getName());
        }
    }
}
