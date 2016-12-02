package com.blogcode.aop;

import com.blogcode.board.Board;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-12-01
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public abstract class BoardPerformance {

    private long before() {
        return System.currentTimeMillis();
    }

    private void after(long start) {
        long end = System.currentTimeMillis();
        System.out.println("수행 시간 : "+ (end - start));
    }

    public List<Board> getBoards() {
        long start = before();
        List<Board> boards = findAll();
        after(start);

        return boards;
    }

    public abstract List<Board> findAll();

}
