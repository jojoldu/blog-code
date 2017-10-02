package com.jojoldu.springmockspybean.why.book;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
