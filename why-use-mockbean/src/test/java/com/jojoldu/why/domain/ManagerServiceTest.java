package com.jojoldu.why.domain;

import com.jojoldu.why.domain.book.Book;
import com.jojoldu.why.domain.book.BookRepository;
import com.jojoldu.why.domain.manager.Manager;
import com.jojoldu.why.domain.manager.ManagerRepository;
import com.jojoldu.why.domain.manager.ManagerService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManagerServiceTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerService managerService;

    @After
    public void cleanup() {
        bookRepository.deleteAll();
        managerRepository.deleteAll();
    }

    @Test
    public void findAllHasBook_Manager가관리하는_모든Book을가져온다 () throws Exception {
        //given
        bookRepository.save(new Book("이펙티브자바", 10000, 1L));
        bookRepository.save(new Book("클린코드", 30000, 1L));
        managerRepository.save(new Manager("jojoldu"));

        //when
        List<Book> books = managerService.findAllHasBook(1L);

        //then
        assertThat(books.size(), is(2));
        assertThat(books.get(0).getName(), is("이펙티브자바"));
        assertThat(books.get(1).getName(), is("클린코드"));
    }
}
