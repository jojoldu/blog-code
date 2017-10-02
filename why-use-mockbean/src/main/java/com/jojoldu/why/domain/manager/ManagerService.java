package com.jojoldu.why.domain.manager;

import com.jojoldu.why.domain.book.Book;
import com.jojoldu.why.domain.book.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Service
public class ManagerService {

    private ManagerRepository managerRepository;
    private BookRepository bookRepository;

    public ManagerService(ManagerRepository managerRepository, BookRepository bookRepository) {
        this.managerRepository = managerRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<Book> findAllHasBook(Long managerId){
        Manager manager = Optional.ofNullable(managerRepository.findOne(managerId))
                .orElseThrow(()-> new RuntimeException("존재하지 않는 Manager Id 입니다"));

        return bookRepository.findAllByManagerId(manager.getId())
                .collect(Collectors.toList());
    }
}
