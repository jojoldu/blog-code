package com.jojoldu.blogcode.querydsl;

import com.jojoldu.blogcode.querydsl.domain.academy.AcademyRepository;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointEvent;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointEventRepository;
import com.jojoldu.blogcode.querydsl.domain.pointevent.PointStatus;
import com.jojoldu.blogcode.querydsl.dto.PointCalculateAmount;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 20/02/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupByTunningTest {
    @Autowired
    private PointEventRepository pointEventRepository;

    @After
    public void tearDown() throws Exception {
        pointEventRepository.deleteAll();
    }

    @Test
    public void pointStatus기준으로_groupBy_결과건수_반환한다() {
        //given
        long usePointAmount = 500;
        long useCancelPointAmount = -900;
        pointEventRepository.saveAll(
                Arrays.asList(
                        new PointEvent(PointStatus.EARN, 100),
                        new PointEvent(PointStatus.USE, usePointAmount),
                        new PointEvent(PointStatus.USE_CANCEL, useCancelPointAmount)
                ));
        //when
        List<Integer> result = pointEventRepository.getGroupOne();

        //then
        assertThat(result.size(), is(3));
    }
}
