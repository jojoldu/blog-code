package com.blogcode.board;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-12-01
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public abstract class BoardServicePerformance {
    public List<Board> getBoards() {
        long start = System.currentTimeMillis();
        List<Board> boards = repository.findAll();
        long end = System.currentTimeMillis();

        System.out.println("수행 시간 : "+ (end - start));

        return boards;
    }

    public Board getBoardByIdx(long idx){
        long start = System.currentTimeMillis();
        Board board = repository.findOne(idx);
        long end = System.currentTimeMillis();

        System.out.println("수행 시간 : "+ (end - start));

        return board;
    }
}
