package com.blogcode.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-12-01
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Service
public class UserServiceImpl {

    @Autowired
    private UserRepository repository;

    public List<User> getUsers() {
        return repository.findAll();
    }
}
