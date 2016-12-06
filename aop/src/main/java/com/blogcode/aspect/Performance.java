package com.blogcode.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by jojoldu@gmail.com on 2016-12-05.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Aspect
public class Performance {

    @Around("execution(* com.blogcode.board.BoardService.getBoards(..))")
    public Object calculatePerformanceTime(ProceedingJoinPoint proceedingJoinPoint) {
        Object result = null;
        try {
            long start = System.currentTimeMillis();
            result = proceedingJoinPoint.proceed();
            long end = System.currentTimeMillis();

            System.out.println("수행 시간 : "+ (end - start));
        } catch (Throwable throwable) {
            System.out.println("exception! ");
        }
        return result;
    }

    @Before("this(com.blogcode.user.UserService)")
    public void printThis(){
        System.out.println("this : UserService");
    }

    @Before("this(com.blogcode.user.UserServiceImpl)")
    public void printThisImpl() {
        System.out.println("this : UserServiceImpl ");
    }

    @Before("target(com.blogcode.user.UserService)")
    public void printTarget(){ System.out.println("target : UserService"); }

    @Before("target(com.blogcode.user.UserServiceImpl)")
    public void printTargetImpl() {
        System.out.println("target : UserServiceImpl ");
    }
}
