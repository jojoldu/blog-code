package com.example.repository;

import com.example.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2016-09-03.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
