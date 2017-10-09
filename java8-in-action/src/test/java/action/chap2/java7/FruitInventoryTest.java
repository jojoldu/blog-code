package action.chap2.java7;

import action.chap2.Apple;
import action.chap2.FruitInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 9.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class FruitInventoryTest {

    public void 익명클래스 () {
        List<Apple> inventory = new ArrayList<>();

        FruitInventory.filterApples(inventory,
                (apple, compare) -> apple.getWeight() > compare.getWeight(),
                Apple.builder()
                        .weight(150)
                        .build());
    }

    public void 스트림API () {
        List<Apple> inventory = new ArrayList<>();
        Apple compare = new Apple();

        List<Apple> weightApples = inventory.stream()
                .filter(apple -> apple.getWeight() > compare.getWeight())
                .collect(Collectors.toList());
    }
}
