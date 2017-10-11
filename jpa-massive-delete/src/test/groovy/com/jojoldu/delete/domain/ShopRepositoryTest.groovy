package com.jojoldu.delete.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * Created by jojoldu@gmail.com on 2017. 10. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@SpringBootTest
class ShopRepositoryTest extends Specification {

    @Autowired
    private ShopRepository shopRepository

    private final List<Long> SHOP_ID_LIST = new ArrayList<>()

    def setup() {
        for(int i=0;i<1000;i++){
            Shop shop = new Shop("우아한서점"+i, "우아한 동네"+i)

            for(int j=0;j<10;j++){
                shop.addItem(new Item("IT책"+j, j*10000))
            }

            shopRepository.save(shop)
        }

        for(long i=100;i<200;i++){
            SHOP_ID_LIST.add(i)
        }
    }

    def cleanup() {
        shopRepository.deleteAll()
    }

    def "SpringDataJPA에서 제공하는 예약어를 통해 삭제한다" () {
        when:
        shopRepository.deleteAllByIdIn(SHOP_ID_LIST)

        then:
        shopRepository.findAll().size() == 900
    }

    def "@Query로 Id 리스트를 조건으로 삭제한다" () {
        when:
        shopRepository.deleteAllByIdInQuery(SHOP_ID_LIST)

        then:
        shopRepository.findAll().size() == 900
    }
}
