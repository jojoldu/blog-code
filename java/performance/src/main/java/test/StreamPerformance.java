package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-11-02.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class StreamPerformance implements Performance{

    private static final String[] arr = {"spring", "jpa", "java", "hibernate", "gradle", "freemarker"};
    private static final List<String> list = new ArrayList<>();
    @Override
    public void execute() {
        // list의 크기를 충분히 늘려주기 위해 중복 추가
        for(int i=0;i<100000;i++){
            list.addAll(Arrays.asList(arr));
        }

        System.out.println("===== Stream과 반복문 비교 =====");

        long start = System.currentTimeMillis();
        test_multiFilter();
        long end = System.currentTimeMillis();
        System.out.println("test_multiFilter 수행시간 : " + Long.toString(end - start));

        start = System.currentTimeMillis();
        test_singleFilter();
        end = System.currentTimeMillis();
        System.out.println("test_singleFilter 수행시간 : " + Long.toString(end - start));

        start = System.currentTimeMillis();
        test_for();
        end = System.currentTimeMillis();
        System.out.println("test_for 수행시간 : " + Long.toString(end - start));
    }

    private int test_multiFilter() {
        int count = 0;
        list.stream()
                .filter(str -> str.contains("a"))
                .filter(str -> str.contains("j"))
                .filter(str -> str.contains("p"))
                .count();

        return count;
    }

    private int test_singleFilter() {
        int count = 0;
        list.stream()
                .filter(str -> str.contains("a") && str.contains("j") && str.contains("p"))
                .count();

        return count;
    }

    private int test_for() {
        int count = 0;

        for(String str : list){
            if(str.contains("a") && str.contains("j") && str.contains("p")) {
                count ++;
            }
        }

        return count;
    }



}
