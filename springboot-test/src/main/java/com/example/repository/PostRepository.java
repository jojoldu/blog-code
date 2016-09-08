package com.example.repository;

import com.example.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by jojoldu@gmail.com on 2016-08-30.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public interface PostRepository<T extends Post> extends JpaRepository<T, Long>{
    Optional<T> findByIdx(long idx);
}
