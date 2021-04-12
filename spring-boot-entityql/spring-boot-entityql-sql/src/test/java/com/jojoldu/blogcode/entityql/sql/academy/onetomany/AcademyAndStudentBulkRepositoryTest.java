package com.jojoldu.blogcode.entityql.sql.academy.onetomany;

import com.jojoldu.blogcode.entityql.entity.domain.academy.Academy;
import com.jojoldu.blogcode.entityql.entity.domain.academy.AcademyRepository;
import com.jojoldu.blogcode.entityql.entity.domain.academy.AcademyStatus;
import com.jojoldu.blogcode.entityql.entity.domain.student.Student;
import com.jojoldu.blogcode.entityql.entity.domain.student.StudentRepository;
import com.jojoldu.blogcode.entityql.sql.bulkinsert.academy.onetomany.AcademyAndStudentBulkRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
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
    void oneToMany_단건_insert() throws Exception {
        //given
        String name = "1";
        Academy academy = new Academy(name, "address", "010-0000-0000", AcademyStatus.ON, new Student(name, 1));

        //when
        academyAndStudentBulkRepository.saveAll(Arrays.asList(academy));

        //then
        List<Academy> academies = academyRepository.findAll();
        List<Student> students = studentRepository.findAll();

        assertThat(academies).hasSize(1);
        assertThat(students).hasSize(1);

        Academy savedAcademy = academies.get(0);
        Student savedStudent = students.get(0);
        assertThat(savedAcademy.getName()).isEqualTo(name);
        assertThat(savedStudent.getName()).isEqualTo(name);
    }

    @Test
    void EmptyList_에러가_발생하지않는다() throws Exception {
        //when
        academyAndStudentBulkRepository.saveAll(new ArrayList<>());

        //then
        List<Academy> academies = academyRepository.findAll();
        List<Student> students = studentRepository.findAll();

        assertThat(academies).hasSize(0);
        assertThat(students).hasSize(0);
    }

    @Test
    void academy만있고_student는없어도_정상등록된다() throws Exception {
        //given
        String name = "1";
        Academy academy = new Academy(name, "address", "010-0000-0000", AcademyStatus.ON);

        //when
        academyAndStudentBulkRepository.saveAll(Arrays.asList(academy));

        //then
        List<Academy> academies = academyRepository.findAll();
        List<Student> students = studentRepository.findAll();

        assertThat(academies).hasSize(1);
        assertThat(students).isEmpty();

        Academy savedAcademy = academies.get(0);
        assertThat(savedAcademy.getName()).isEqualTo(name);
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
