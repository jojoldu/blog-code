package com.jojoldu.blogcode.entityql.sql;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Slf4j
class UuidTest {

    /**
     *
     * 1000개 생성하는데 24ms (0.024초)
     */
    @Test
    void uuid_생성속도() throws Exception {
        //given
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < 1_000; i++) {
            UUID.randomUUID().toString();
        }

       log.info("1천개 생성 속도: {}ms", System.currentTimeMillis() - t0);
    }
}
