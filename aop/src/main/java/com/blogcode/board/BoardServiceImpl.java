package com.blogcode.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-11-30.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Service
public class BoardServiceImpl implements BoardService{

    @Autowired
    private BoardRepository repository;

    @Override
    public List<Board> getBoards() {
        return repository.findAll();
    }
}
