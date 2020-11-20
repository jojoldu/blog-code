package com.jojoldu.blogcode.querydsl.domain.student;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 05/08/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("real")
public class RealStudentQueryRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentQueryRepository studentQueryRepository;

    @Test
    void dirtyChecking_update() throws Exception {
        //given
        String name = "a";
        studentRepository.save(Student.builder()
                .name(name)
                .build());

        String expectedName = "b";

        //when
        studentQueryRepository.updateName1(10_000L, expectedName);
    }

    @Test
    void querydsl_update() throws Exception {
        //given
        String name = "a";
        studentRepository.save(Student.builder()
                .name(name)
                .build());
        String expectedName = "b";

        //when
        studentQueryRepository.updateName2(10_000L, expectedName);
    }

}
