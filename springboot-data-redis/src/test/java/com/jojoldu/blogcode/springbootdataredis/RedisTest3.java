package com.jojoldu.blogcode.springbootdataredis;

import com.jojoldu.blogcode.springbootdataredis.domain.Point;
import com.jojoldu.blogcode.springbootdataredis.domain.PointRedisRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 2018. 5. 26.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisTest3 {
    
    @Autowired
    private PointRedisRepository pointRedisRepository;

    @After
    public void tearDown() throws Exception {
        pointRedisRepository.deleteAll();
    }


    @Test
    public void 수정기능() {
        //given
        String id = "jojoldu";
        LocalDateTime refreshTime = LocalDateTime.of(2018, 5, 26, 0, 0);
        pointRedisRepository.save(Point.builder()
                .id(id)
                .amount(1000L)
                .refreshTime(refreshTime)
                .build());

        //when
        Point savedPoint = pointRedisRepository.findById(id).get();
        savedPoint.refresh(2000L, LocalDateTime.of(2018,6,1,0,0));
        pointRedisRepository.save(savedPoint);

        //then
        Point refreshPoint = pointRedisRepository.findById(id).get();
        assertThat(refreshPoint.getAmount()).isEqualTo(2000L);
    }
}
