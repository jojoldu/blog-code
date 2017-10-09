package action.chap3;

import action.chap2.Apple;

import java.util.Comparator;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 10.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class LambdaSample {

    public void sample7() {
        Comparator<Apple> weightComparator = new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return Integer.compare(o1.getWeight(), o2.getWeight());
            }
        };
    }

    public void sample8() {
        Comparator<Apple> weightComparator = (o1, o2) -> Integer.compare(o1.getWeight(), o2.getWeight());
    }
}
