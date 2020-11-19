package com.jojoldu.blogcode.querydsl.domain.ad;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 11/08/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AdItemQueryRepositoryTest {
    @Autowired private AdItemRepository adItemRepository;
    @Autowired private AdBondRepository adBondRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ShopRepository shopRepository;
    @Autowired private AdItemQueryRepository queryRepository;

    @AfterEach
    void after() {
        adItemRepository.deleteAllInBatch();
        adBondRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
        shopRepository.deleteAllInBatch();
    }

    @Test
    void ad_item을_통해_ad_bond를_생성한다1() throws Exception {
        //given
        Shop shop = shopRepository.save(new Shop("no", "name"));
        Customer customer = customerRepository.save(new Customer("no", "name", "bizNo", "ceo", shop));
        LocalDate startDate = LocalDate.of(2020,8,11);
        LocalDate endDate = LocalDate.of(2020,8,12);

        String orderType1 = "a";
        String orderType2 = "b";
        adItemRepository.save(new AdItem(orderType1, startDate, "1", 1000L, customer));
        adItemRepository.save(new AdItem(orderType2, endDate, "2", 2000L, customer));

        //when
        List<AdBond> adBonds = queryRepository.createAdBond(startDate, endDate, Arrays.asList(orderType1, orderType2));

        //then
        assertThat(adBonds).hasSize(2);
    }

    @Test
    void ad_item을_통해_ad_bond를_생성한다2() throws Exception {
        //given
        Shop shop = shopRepository.save(new Shop("no", "name"));
        Customer customer = customerRepository.save(new Customer("no", "name", "bizNo", "ceo", shop));
        LocalDate startDate = LocalDate.of(2020,8,11);
        LocalDate endDate = LocalDate.of(2020,8,12);

        String orderType1 = "a";
        String orderType2 = "b";
        adItemRepository.save(new AdItem(orderType1, startDate, "1", 1000L, customer));
        adItemRepository.save(new AdItem(orderType2, endDate, "2", 2000L, customer));

        //when
        List<AdBondDto> adBondDtos = queryRepository.createAdBondDto(startDate, endDate, Arrays.asList(orderType1, orderType2));

        //then
        assertThat(adBondDtos).hasSize(2);
    }

    @Test
    void distinct_ad_bond를_생성한다1() throws Exception {
        //given
        Shop shop = shopRepository.save(new Shop("no", "name"));
        Customer customer = customerRepository.save(new Customer("no", "name", "bizNo", "ceo", shop));
        LocalDate startDate = LocalDate.of(2020,8,11);
        LocalDate endDate = LocalDate.of(2020,8,12);

        String orderType1 = "a";
        String orderType2 = "b";
        adItemRepository.save(new AdItem(orderType1, startDate, "1", 1000L, customer));
        adItemRepository.save(new AdItem(orderType2, endDate, "2", 2000L, customer));

        //when
        List<AdBond> adBonds = queryRepository.distinctAdBond(startDate, endDate, Arrays.asList(orderType1, orderType2));

        //then
        assertThat(adBonds).hasSize(2);
    }
}
