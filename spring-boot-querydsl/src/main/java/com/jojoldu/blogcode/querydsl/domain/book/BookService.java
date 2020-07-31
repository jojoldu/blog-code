package com.jojoldu.blogcode.querydsl.domain.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jojoldu@gmail.com on 31/07/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookQueryRepository bookQueryRepository;

    @Transactional
    public void update (Long bookId, String bookName) {
        Book book = bookQueryRepository.getBookById(bookId);
        book.changeName(bookName);
    }
}
