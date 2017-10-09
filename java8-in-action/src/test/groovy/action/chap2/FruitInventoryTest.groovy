package action.chap2

import action.chap2.java7.ApplePredicate
import action.chap2.java7.AppleWeightPredicate
import spock.lang.Specification

/**
 * Created by jojoldu@gmail.com on 2017. 10. 8.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

class FruitInventoryTest extends Specification {

    def "녹색사과와 빨간사과만 가져온다" () {
        List<Apple> inventory = new ArrayList<>()

        List<Apple> greenApples = FruitInventory.filterApplesByColor(inventory, "green")
        List<Apple> redApples = FruitInventory.filterApplesByColor(inventory, "red")
    }

    def "java7이하에서 Predicate로 필터한다" () {
        List<Apple> inventory = new ArrayList<>()

        Apple compare = Apple.builder()
                .weight(150)
                .build()
        List<Apple> weightApples = FruitInventory.filterApples(inventory, new AppleWeightPredicate(), compare)

    }

    def "java7이하에서 Predicate로 필터한다 - 익명클래스" () {
        List<Apple> inventory = new ArrayList<>()

        List<Apple> weightApples = FruitInventory.filterApples(inventory, new ApplePredicate() {
            @Override
            boolean test(Apple apple, Apple compare) {
                return apple.getWeight() > compare.getWeight()
            }
        }, Apple.builder()
                .weight(150)
                .build())
    }

}
