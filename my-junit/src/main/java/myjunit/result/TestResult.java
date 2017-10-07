package myjunit.result;

import myjunit.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 6.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class TestResult {
    private static final Logger logger = LoggerFactory.getLogger(TestResult.class);
    private int runTestCount;
    private List<TestFailure> failures;
    private List<TestError> errors;

    public TestResult() {
        this.runTestCount = 0;
        this.failures = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    /**
     synchronized: 하나의 TestResult 인스턴스를 여러 테스트 케이스에서 사용하게 될 경우
     쓰레드 동기화 문제가 발생하므로 여기선 synchronized로 간단하게 해결합니다.
     (테스트 케이스에서만 사용하므로 실시간 성능 이슈를 고려하지 않아도 되기 때문입니다)
     */
    public synchronized void startTest() {
        this.runTestCount++;
    }

    public synchronized void addFailure(TestCase testCase) {
        this.failures.add(new TestFailure(testCase));
    }

    public synchronized void addError(TestCase testCase, Exception e){
        this.errors.add(new TestError(testCase, e));
    }

    /**
     * 차후 본인이 원하는 방식으로 레포팅 방식 변경
     */
    public void printCount(){
        logger.info("Total Test Count: {}", runTestCount);
        logger.info("Total Test Success Count: {}", runTestCount - failures.size() - errors.size());
        logger.info("Total Test Failure Count: {}", failures.size());
        logger.info("Total Test Error Count: {}", errors.size());
    }
}
