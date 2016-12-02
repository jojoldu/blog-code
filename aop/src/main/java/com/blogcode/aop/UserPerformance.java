package com.blogcode.aop;

import com.blogcode.user.User;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-12-02
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public abstract class UserPerformance {

    private long before() {
        return System.currentTimeMillis();
    }

    private void after(long start) {
        long end = System.currentTimeMillis();
        System.out.println("수행 시간 : "+ (end - start));
    }

    public List<User> getUsers() {
        long start = before();
        List<User> users = findAll();
        after(start);

        return users;
    }

    public abstract List<User> findAll();
}
