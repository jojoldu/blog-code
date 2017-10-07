package myjunit.result;

import myjunit.TestCase;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 7.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class TestError {
    private TestCase testCase;
    private Exception exception;

    public TestError(TestCase testCase, Exception exception) {
        this.testCase = testCase;
        this.exception = exception;
    }

    public String getTestCaseName() {
        return testCase.getTestCaseName();
    }

    public Exception getException() {
        return exception;
    }
}
