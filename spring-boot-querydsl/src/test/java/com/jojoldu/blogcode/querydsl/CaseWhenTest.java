package com.jojoldu.blogcode.querydsl;

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
        long usePointAmount = 500;
        long useCancelPointAmount = -900;
        pointEventRepository.saveAll(
                Arrays.asList(
                        new PointEvent(PointStatus.EARN, 100),
                        new PointEvent(PointStatus.USE, usePointAmount),
                        new PointEvent(PointStatus.USE_CANCEL, useCancelPointAmount)
                ));
        //when
        List<PointCalculateAmount> result = pointEventRepository.calculateAmounts();
        result.sort(Comparator.comparingLong(PointCalculateAmount::getPointAmount));

        //then
        assertThat(result.get(0).getPointStatus(), is(PointStatus.USE));
        assertThat(result.get(0).getPointAmount(), is(-usePointAmount)); // 500원이 -500원으로

        assertThat(result.get(1).getPointStatus(), is(PointStatus.EARN));
        assertThat(result.get(1).getPointAmount(), is(100L));

        assertThat(result.get(2).getPointStatus(), is(PointStatus.USE_CANCEL));
        assertThat(result.get(2).getPointAmount(), is(-useCancelPointAmount)); // -900 원이 900원으로

    }
}
