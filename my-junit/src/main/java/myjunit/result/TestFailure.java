package myjunit.result;

import myjunit.TestCase;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 7.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class TestFailure {
    private TestCase testCase;

    public TestFailure(TestCase testCase) {
        this.testCase = testCase;
    }

    public String getTestCaseName() {
        return testCase.getTestCaseName();
    }
}
