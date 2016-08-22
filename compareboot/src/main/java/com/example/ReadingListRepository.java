package com.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-08-22.
 */
public interface ReadingListRepository extends JpaRepository<Book, Long>{
    List<Book> findByReader(Reader reader);
}
