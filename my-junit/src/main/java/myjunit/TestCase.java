package myjunit;

import myjunit.assertion.AssertionFailedError;
import myjunit.result.TestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 5.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public abstract class TestCase {

    private static final Logger logger = LoggerFactory.getLogger(TestCase.class);

    protected String testCaseName;

    public TestCase(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public TestResult run(){
        TestResult testResult = createTestResult();
        run(testResult);

        return testResult;
    }

    public void run(TestResult testResult){
        testResult.startTest();
        before();
        try{
            runTestCase();
        } catch (InvocationTargetException ite) {
            if(isAssertionFailed(ite)){
                testResult.addFailure(this);
            } else {
                testResult.addError(this, ite);
            }
        } catch (Exception e) {
            testResult.addError(this, e);
        } finally {
            after();
        }
    }

    private boolean isAssertionFailed(InvocationTargetException ite) {
        return ite.getTargetException() instanceof AssertionFailedError;
    }

    private TestResult createTestResult() {
        return new TestResult();
    }

    protected void before() {}

    private void runTestCase() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        logger.info("{} execute ", testCaseName); // 테스트 케이스들 구별을 위해 name 출력 코드
        Method method = this.getClass().getMethod(testCaseName, null);
        method.invoke(this, null);
    }

    protected void after() {}

    public String getTestCaseName() {
        return testCaseName;
    }
}
