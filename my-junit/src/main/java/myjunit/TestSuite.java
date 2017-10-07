package myjunit;

import myjunit.result.TestResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 7.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class TestSuite implements Test {
    private List<TestCase> testCases = new ArrayList<>();

    @Override
    public void run(TestResult result) {
        for (TestCase testCase : this.testCases) {
            testCase.run(result);
        }
    }

    public void addTestCase(TestCase testCase) {
        this.testCases.add(testCase);
    }
}
