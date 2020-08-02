package com.jojoldu.blogcode.querydsl.domain.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by jojoldu@gmail.com on 26/06/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b.name FROM Book b WHERE b.id =:id")
    String getNameById (@Param("id") Long id);
}
