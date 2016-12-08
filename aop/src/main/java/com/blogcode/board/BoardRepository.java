package com.blogcode.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jojoldu@gmail.com on 2016-11-30.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

}
