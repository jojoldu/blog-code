package com.blogcode.aspect;

import com.blogcode.history.History;
import com.blogcode.history.HistoryRepository;
import com.blogcode.user.User;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by jojoldu@gmail.com on 2016-12-08.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Aspect
@Component // @Bean과 동일하게 Spring Bean 등록 어노테이션
public class UserHistory {

    @Autowired
    private HistoryRepository historyRepository;

    @Pointcut("execution(* com.blogcode.user.UserService.update(*)) && args(user)")
    public void updateUser(User user){}

    @AfterReturning("updateUser(user)")
    public void saveHistory(User user){
        historyRepository.save(new History(user.getIdx()));
    }
}
