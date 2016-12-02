package com.blogcode.board;

import com.blogcode.aop.BoardPerformance;
import com.blogcode.aop.SuperPerformance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-11-30.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Service
public class BoardService extends SuperPerformance<Board> {

    @Autowired
    private BoardRepository repository;

    @Override
    public List<Board> findAll() {
        return repository.findAll();
    }

}
