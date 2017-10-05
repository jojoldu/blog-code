package myjunit;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 5.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public abstract class TestCase {

    public void run(){
        before();
        runTest();
        after();
    }

    protected void runTest() {}

    protected void before() {}

    protected void after() {}
}
