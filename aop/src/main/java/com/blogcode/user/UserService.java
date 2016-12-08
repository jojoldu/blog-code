package com.blogcode.user;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-12-03.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public interface UserService {
    List<User> getUsers();

    void update(User user) throws Exception;
}
