package action.chap2.java7;

import action.chap2.Apple;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 9.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class AppleColorPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple, Apple compare) {
        return apple.getColor().equals(compare.getColor());
    }
}
