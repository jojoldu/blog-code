package myjunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 5.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class Assert {
    private static final Logger logger = LoggerFactory.getLogger(Assert.class);

    private Assert() {} // 인스턴스 생성을 막기 위해 기본생성자 private 선언

    public static void assertTrue(boolean condition) {
        if(!condition){
            throw new AssertionFailedError();
        }

        logger.info("Test Passed");
    }

    public static void assertEquals(Object o1, Object o2){
        if(!o1.equals(o2)){
            throw new AssertionFailedError();
        }
    }
}
