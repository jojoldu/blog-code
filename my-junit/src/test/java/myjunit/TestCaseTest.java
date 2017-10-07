package myjunit;

import myjunit.assertion.Assert;
import myjunit.result.TestResult;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 5.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class TestCaseTest extends TestCase {

    public TestCaseTest(String testCaseName) {
        super(testCaseName);
    }

    private static long base;

    @Override
    public void before() {
        base = 10;
    }

    public void runTest() {
        long sum = 10 + base;
        Assert.assertTrue(sum == 30); // 틀린 케이스
    }

    public void runTestMinus() {
        long minus = 100 - base;
        Assert.assertTrue(minus == 90);
    }

    public static void main(String[] args) {
        TestResult testResult = new TestResult();
        new TestCaseTest("runTest").run(testResult);
        new TestCaseTest("runTestMinus").run(testResult);
        testResult.printCount();
    }
}
