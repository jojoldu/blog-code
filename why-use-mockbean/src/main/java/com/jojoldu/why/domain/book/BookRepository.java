package com.jojoldu.why.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface BookRepository extends JpaRepository<Book, Long>{

    Stream<Book> findAllByManagerId(Long managerId);
}
