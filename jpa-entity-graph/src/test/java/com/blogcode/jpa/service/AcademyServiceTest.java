package com.blogcode.jpa.service;

import com.blogcode.jpa.domain.Academy;
import com.blogcode.jpa.domain.AcademyRepository;
import com.blogcode.jpa.domain.Subject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 21.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class AcademyServiceTest {

    @Autowired
    private AcademyRepository academyRepository;

    @Autowired
    private AcademyService academyService;

    @After
    public void cleanAll() {
        academyRepository.deleteAll();
    }

    @Before
    public void setup() {
        List<Academy> academies = new ArrayList<>();

        for(int i=0;i<10;i++){
            Academy academy = Academy.builder()
                    .name("강남스쿨"+i)
                    .build();

            academy.addSubject(Subject.builder().name("자바웹개발" + i).build());
            academy.addSubject(Subject.builder().name("파이썬자동화" + i).build());
            academies.add(academy);
        }

        academyRepository.save(academies);
    }

    @Test
    public void Academy여러개를_조회시_Subject가_N1_쿼리가발생한다() throws Exception {
        //given
        List<String> subjectNames = academyService.findAllSubjectNames();

        //then
        assertThat(subjectNames.size(), is(10));
    }


    @Test
    public void Academy여러개를_joinFetch로_가져온다() throws Exception {
        //given
        List<Academy> academies = academyRepository.findAllJoinFetch();
        List<String> subjectNames = academyService.findAllSubjectNamesByJoinFetch();

        //then
        assertThat(academies.size(), is(20)); // 20개가 조회!?
        assertThat(subjectNames.size(), is(20)); // 20개가 조회!?
    }

    @Test
    public void Academy여러개를_EntityGraph로_가져온다() throws Exception {
        //given
        List<Academy> academies = academyRepository.findAllEntityGraph();
        List<String> subjectNames = academyService.findAllSubjectNamesByEntityGraph();

        //then
        assertThat(academies.size(), is(20));
        assertThat(subjectNames.size(), is(20));
    }

    @Test
    public void Academy여러개를_distinct해서_가져온다 () throws Exception {
        //given
        List<Academy> academies = academyRepository.findAllJoinFetchDistinct();

        //then
        assertThat(academies.size(), is(10));
    }
}
