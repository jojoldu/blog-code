package myjunit;

import myjunit.result.TestResult;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 7.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface Test {
    void run(TestResult result);
}
