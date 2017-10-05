package myjunit;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 5.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public abstract class TestCase {

    protected String methodName;

    public TestCase(String methodName) {
        this.methodName = methodName;
    }

    public void run(){
        before();
        runTest();
        after();
    }

    protected void before() {
    }

    protected void runTest() {
    }

    protected void after() {
    }
}
