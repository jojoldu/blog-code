package myjunit;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 5.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class TestCaseTest {

    public static void main(String[] args) {
        new TestCaseTest().runTest();
    }

    public void runTest () {
        long sum = 10+10;
        Assert.assertTrue(sum == 20);
    }
}
