package com.jojoldu.blogcode.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 05/03/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeRepositoryTest {

    @Autowired
    private TradeRepository tradeRepository;

    @Test
    void Trade_등록_조회() throws Exception {
        //given
        long amount = 1000L;
        String tradeName = "tradeName";

        //when
        tradeRepository.save(new Trade(amount, tradeName));
        Trade trade = tradeRepository.findAll().get(0);

        //then
        assertThat(trade.getAmount()).isEqualTo(amount);
        assertThat(trade.getTradeName()).isEqualTo(tradeName);
    }
}
