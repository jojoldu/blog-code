package com.jojoldu.blogcode.querydsl.domain.ad;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
public class AdItemServiceTest {
    @Autowired private AdItemRepository adItemRepository;
    @Autowired private AdBondRepository adBondRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private ShopRepository shopRepository;
    @Autowired private AdItemService adItemService;

    @AfterEach
    void after() {
        adItemRepository.deleteAllInBatch();
        adBondRepository.deleteAllInBatch();
        shopRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
    }

    @Test
    void test_createAdBond2() throws Exception {
        //given
        Shop shop = shopRepository.save(new Shop("no", "name"));
        String expectedNo = "no";
        Customer customer = customerRepository.save(new Customer(expectedNo, "name", "bizNo", "ceo", shop));
        LocalDate startDate = LocalDate.of(2020,8,11);
        LocalDate endDate = LocalDate.of(2020,8,12);

        String orderType1 = "a";
        String orderType2 = "b";
        adItemRepository.save(new AdItem(orderType1, startDate, "1", 1000L, customer));
        adItemRepository.save(new AdItem(orderType2, endDate, "2", 2000L, customer));

        //when
        adItemService.createAdBond3(startDate, endDate, Arrays.asList(orderType1, orderType2));

        //then
        List<AdBond> adBonds = adBondRepository.findAll();
        assertThat(adBonds).hasSize(2);
        assertThat(adBonds.get(0).getCustomer().getCustomerNo()).isEqualTo(expectedNo);
        assertThat(adBonds.get(1).getCustomer().getCustomerNo()).isEqualTo(expectedNo);

    }

    @Test
    void test_createAdBond3() throws Exception {
        //given
        String expectedNo = "no";
        Customer customer = customerRepository.save(new Customer(expectedNo, "name", "bizNo", "ceo"));
        shopRepository.save(new Shop(expectedNo, "name", customer));
        LocalDate startDate = LocalDate.of(2020,8,11);
        LocalDate endDate = LocalDate.of(2020,8,12);

        String orderType1 = "a";
        String orderType2 = "b";
        adItemRepository.save(new AdItem(orderType1, startDate, "1", 1000L, customer));
        adItemRepository.save(new AdItem(orderType2, endDate, "2", 2000L, customer));

        //when
        adItemService.createAdBond3(startDate, endDate, Arrays.asList(orderType1, orderType2));

        //then
        List<AdBond> adBonds = adBondRepository.findAll();
        AdBond adBond1 = adBonds.get(0);
        AdBond adBond2 = adBonds.get(1);

        assertThat(adBonds).hasSize(2);
        assertThat(adBond1.getCustomer().getCustomerNo()).isEqualTo(expectedNo);
        assertThat(adBond2.getCustomer().getCustomerNo()).isEqualTo(expectedNo);

        assertThat(adBond1.getCustomer().getShop().getShopNo()).isEqualTo(expectedNo);
        assertThat(adBond2.getCustomer().getShop().getShopNo()).isEqualTo(expectedNo);
    }
}
