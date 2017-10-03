package com.jojoldu.why.domain;

import com.jojoldu.why.domain.book.Book;
import com.jojoldu.why.domain.book.BookRepository;
import com.jojoldu.why.domain.book.BookService;
import com.jojoldu.why.domain.manager.ManagerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void findAll_전체리스트를_가져온다 () throws Exception {
        //then
        given(bookRepository.findAll())
                .willReturn(Arrays.asList(
                        new Book("토비의스프링", 10000, 1L),
                        new Book("자바ORMJPA", 20000, 1L)));

        //when
        List<Book> books = bookService.findAll();

        //then
        assertThat(books.size(), is(2));
        assertThat(books.get(0).getName(), is("토비의스프링"));
        assertThat(books.get(1).getName(), is("자바ORMJPA"));
    }
}
