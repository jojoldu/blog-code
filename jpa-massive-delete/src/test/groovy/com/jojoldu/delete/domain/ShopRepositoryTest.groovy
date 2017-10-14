package com.jojoldu.delete.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
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

    @Autowired
    private ItemRepository itemRepository

    private final List<Long> SHOP_ID_LIST = new ArrayList<>()

    def setup() {
        for (long i = 10; i < 20; i++) {
            SHOP_ID_LIST.add(i)
        }
    }

    def cleanup() {
        println "======== Clean All ========="
        itemRepository.deleteAll()
        shopRepository.deleteAll()
    }

    def "SpringDataJPA에서 제공하는 예약어를 통해 삭제한다 - 부모만" () {
        given:
        createShop()

        when:
        shopRepository.deleteAllByIdIn(SHOP_ID_LIST)

        then:
        shopRepository.findAll().size() == 90
    }

    def "@Query로 Id 리스트를 조건으로 삭제한다 - 부모만" () {
        given:
        createShop()

        when:
        shopRepository.deleteAllByIdInQuery(SHOP_ID_LIST)

        then:
        shopRepository.findAll().size() == 90
    }

    def "SpringDataJPA에서 제공하는 예약어를 통해 삭제한다 - 부모&자식" () {
        given:
        createShopAndItem()

        when:
        shopRepository.deleteAllByIdIn(SHOP_ID_LIST)

        then:
        shopRepository.findAll().size() == 90
    }

    def "@Query로 Id 리스트를 조건으로 삭제한다 - 부모&자식" () {
        given:
        createShopAndItem()

        when:
        itemRepository.deleteAllByIdInQuery(SHOP_ID_LIST)
        shopRepository.deleteAllByIdInQuery(SHOP_ID_LIST)

        then:
        shopRepository.findAll().size() == 90
    }

    private void createShop() {
        for (int i = 0; i < 100; i++) {
            shopRepository.save(new Shop("우아한서점" + i, "우아한 동네" + i))
        }

        println "=======End Create Shop======="
    }

    private void createShopAndItem() {
        for (int i = 0; i < 100; i++) {
            Shop shop = new Shop("우아한서점" + i, "우아한 동네" + i)

            for (int j = 0; j < 10; j++) {
                shop.addItem(new Item("IT책" + j, j * 10000))
            }

            shopRepository.save(shop)
        }

        println "=======End Create Shop & Item======="
    }
}
