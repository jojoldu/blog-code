package myjunit;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 5.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class Assert {
    private Assert() {}

    public static void assertTrue(boolean condition) {
        if(!condition){
            throw new AssertionFailedError();
        }
    }

    public static void assertEquals(Object o1, Object o2){
        if(!o1.equals(o2)){
            throw new AssertionFailedError();
        }
    }
}
