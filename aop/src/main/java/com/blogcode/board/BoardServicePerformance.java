package com.blogcode.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-12-03.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Service
public class BoardServicePerformance implements BoardService{

    @Autowired
    @Qualifier("boardServiceImpl")
    private BoardService boardService;

    @Override
    public List<Board> getBoards() {
        long start = before();
        List<Board> boards = boardService.getBoards();
        after(start);

        return boards;
    }

    private long before() {
        return System.currentTimeMillis();
    }

    private void after(long start) {
        long end = System.currentTimeMillis();
        System.out.println("수행 시간 : "+ (end - start));
    }
}
