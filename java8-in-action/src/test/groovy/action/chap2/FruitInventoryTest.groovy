package action.chap2

import spock.lang.Specification

/**
 * Created by jojoldu@gmail.com on 2017. 10. 8.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

class FruitInventoryTest extends Specification {

    def "녹색사과와 빨간사과만 가져온다" () {
        List<Apple> inventory = new ArrayList<>();

        List<Apple> greenApples = FruitInventory.filterApplesByColor(inventory, "green")
        List<Apple> redApples = FruitInventory.filterApplesByColor(inventory, "red")


    }
}
