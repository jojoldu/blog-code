package myjunit;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 5.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class TestCaseTest extends TestCase {

    public TestCaseTest(String testCaseName) {
        super(testCaseName);
    }

    public void runTest() {
        long sum = 10+10;
        Assert.assertTrue(sum == 20);
    }

    public void runTestMinus() {
        long minus = 100-10;
        Assert.assertTrue(minus == 90);
    }

    public static void main(String[] args) {
        new TestCaseTest("runTest").run();
        new TestCaseTest("runTestMinus").run();
    }
}
