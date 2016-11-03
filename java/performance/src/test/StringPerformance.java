package test;

/**
 * Created by jojoldu@gmail.com on 2016-11-01.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public class StringPerformance implements Performance {

    private static final int SIZE = 100000;

    @Override
    public void execute() {
        System.out.println("=====문자일 합치기에 대한 성능 비교=====");

        long start = System.currentTimeMillis();
        test_singleLineSum();
        long end = System.currentTimeMillis();
        System.out.println("test_singleLineSum 수행시간 : " + Long.toString(end - start));

        start = System.currentTimeMillis();
        test_multiLineSum();
        end = System.currentTimeMillis();
        System.out.println("test_multiLineSum 수행시간 : " + Long.toString(end - start));
    }

    public void test_singleLineSum() {
        String s = null;

        for(int i=0;i<SIZE;i++){
            s = "jojoldu" + "." + "blog code";
        }
    }

    public void test_multiLineSum() {
        String s = null;

        for(int i=0;i<SIZE;i++){
            s = "jojoldu";
            s +=  ".";
            s += "blog code";
        }
    }

}
