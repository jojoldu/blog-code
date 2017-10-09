package action.chap2;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 8.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@NoArgsConstructor
public class Apple {
    private String color;
    private int weight;

    @Builder
    public Apple(String color, int weight) {
        this.color = color;
        this.weight = weight;
    }
}

