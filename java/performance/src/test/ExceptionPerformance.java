package test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-10-31.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public class ExceptionPerformance implements Performance {

    private List<String> list;
    private static final int SIZE = 100000;

    @Override
    public void execute() {
        System.out.println("=====예외처리에 대한 성능 비교=====");

        long start = System.currentTimeMillis();
        test_throwException();
        long end = System.currentTimeMillis();
        System.out.println("test_throwException 수행시간 : " + Long.toString(end - start));

        start = System.currentTimeMillis();
        test_tryCatch();
        end = System.currentTimeMillis();
        System.out.println("test_tryCatch 수행시간 : " + Long.toString(end - start));

        start = System.currentTimeMillis();
        test_notNull();
        end = System.currentTimeMillis();
        System.out.println("test_notNull 수행시간 : " + Long.toString(end - start));

    }

    private void test_throwException() {
        list = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            try {
                if (i % 2 != 0) {
                    throw new NullPointerException();
                }

                String s = "test";
                list.add(s);
            } catch (NullPointerException npe) {

            }
        }
    }

    private void test_tryCatch() {
        list = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            String s = null;

            if (i % 2 == 0) {
                s = "test";
            }

            try {
                list.add(s);
            } catch (NullPointerException npe) {

            }
        }
    }

    private void test_notNull() {
        list = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            String s = null;

            if (i % 2 == 0) {
                s = "test";
            }

            if (s != null) {
                list.add(s);
            }
        }
    }

}
