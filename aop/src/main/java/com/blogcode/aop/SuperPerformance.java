package com.blogcode.aop;

import com.blogcode.user.User;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-12-02
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public abstract class SuperPerformance<T> {
    private long before() {
        return System.currentTimeMillis();
    }

    private void after(long start) {
        long end = System.currentTimeMillis();
        System.out.println("수행 시간 : "+ (end - start));
    }

    public List<T> getDataAll() {
        long start = before();
        List<T> datas = findAll();
        after(start);

        return datas;
    }

    public abstract List<T> findAll();
}
