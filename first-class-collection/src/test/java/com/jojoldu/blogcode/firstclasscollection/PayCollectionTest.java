package com.jojoldu.blogcode.firstclasscollection;

import com.jojoldu.blogcode.firstclasscollection.sample1.Pay;
import com.jojoldu.blogcode.firstclasscollection.sample1.PayGroups;
import com.jojoldu.blogcode.firstclasscollection.sample1.PayType;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.jojoldu.blogcode.firstclasscollection.sample1.PayType.KAKAO_PAY;
import static com.jojoldu.blogcode.firstclasscollection.sample1.PayType.NAVER_PAY;
import static com.jojoldu.blogcode.firstclasscollection.sample1.PayType.TOSS;
import static org.assertj.core.api.Assertions.assertThat;

public class PayCollectionTest {

    @Test
    public void 로직이_밖에_있는_상태() {
        //given
        List<Pay> pays = Arrays.asList(
                new Pay(NAVER_PAY, 1000),
                new Pay(NAVER_PAY, 1500),
                new Pay(KAKAO_PAY, 2000),
                new Pay(TOSS, 3000L));
        //when
        Long naverPaySum = pays.stream()
                .filter(pay -> pay.getPayType().equals(NAVER_PAY))
                .mapToLong(Pay::getAmount)
                .sum();

        //then
        assertThat(naverPaySum).isEqualTo(2500);
    }

    @Test
    public void 로직과_값이_한곳에() {
        //given
        List<Pay> pays = Arrays.asList(
                new Pay(NAVER_PAY, 1000),
                new Pay(NAVER_PAY, 1500),
                new Pay(KAKAO_PAY, 2000),
                new Pay(TOSS, 3000L));

        PayGroups payGroups = new PayGroups(pays);

        //when
        Long naverPaySum = payGroups.getNaverPaySum();

        //then
        assertThat(naverPaySum).isEqualTo(2500);

    }
}
