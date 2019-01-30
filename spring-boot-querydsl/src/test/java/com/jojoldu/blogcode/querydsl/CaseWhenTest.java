package com.jojoldu.blogcode.querydsl;

import com.jojoldu.blogcode.querydsl.domain.academy.AcademyRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by jojoldu@gmail.com on 2019-01-31
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CaseWhenTest {
    
    @Autowired
    private AcademyRepository academyRepository;

    @After
    public void tearDown() throws Exception {
        academyRepository.deleteAll();
    }

    @Test
    public void 조건에따라_값이_변경된다() {
        //given

        //when

        //then

    }
}
