package com.jojoldu.blogcode.querydsl.repository;

import com.jojoldu.blogcode.querydsl.domain.academy.Academy;
import com.jojoldu.blogcode.querydsl.domain.academy.AcademyRepository;
import com.jojoldu.blogcode.querydsl.domain.teacher.Teacher;
import com.jojoldu.blogcode.querydsl.domain.teacher.TeacherRepository;
import com.jojoldu.blogcode.querydsl.dto.AcademyTeacher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2019-01-27
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotRelationJoinTest {

    @Autowired
    private AcademyRepository academyRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void 관계없을때_조인_맺기() {
        //given
        String academyName = "name";
        academyRepository.saveAll(Arrays.asList(
                new Academy(academyName, "", ""),
                new Academy("not target", "", "")
        ));

        String teacherName = "teacher";
        teacherRepository.save(new Teacher(teacherName, "Java", 1L));

        //when
        List<AcademyTeacher> academyTeachers = academyRepository.findAllAcademyTeacher();

        //then
        assertThat(academyTeachers.size(), is(1));
        assertThat(academyTeachers.get(0).getAcademyName(), is(academyName));
        assertThat(academyTeachers.get(0).getTeacherName(), is(teacherName));
    }
}
