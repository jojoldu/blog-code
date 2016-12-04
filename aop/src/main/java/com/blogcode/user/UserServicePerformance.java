package com.blogcode.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-12-03.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Service
@Primary
public class UserServicePerformance implements UserService{

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Override
    public List<User> getUsers() {
        long start = before();
        List<User> users = userService.getUsers();
        after(start);
        return users;
    }

    private long before() {
        return System.currentTimeMillis();
    }

    private void after(long start) {
        long end = System.currentTimeMillis();
        System.out.println("수행 시간 : "+ (end - start));
    }
}
