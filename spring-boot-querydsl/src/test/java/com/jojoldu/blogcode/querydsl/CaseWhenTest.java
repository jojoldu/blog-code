package com.jojoldu.blogcode.querydsl;

import com.jojoldu.blogcode.querydsl.domain.academy.AcademyRepository;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointEvent;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointEventRepository;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * Created by jojoldu@gmail.com on 2019-01-31
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CaseWhenTest {

    @Autowired
    private PointEventRepository pointEventRepository;

    @After
    public void tearDown() throws Exception {
        pointEventRepository.deleteAll();
    }

    @Test
    public void 조건에따라_값이_변경된다() {
        //given
        pointEventRepository.saveAll(
                Arrays.asList(
                        new PointEvent(PointStatus.EARN, 100),
                        new PointEvent(PointStatus.USE, 500),
                        new PointEvent(PointStatus.USE_CANCEL, -900)
                ));
        //when


        //then

    }
}
