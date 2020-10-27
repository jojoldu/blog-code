package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.jojoldu.blogcode.querydsl.domain.book.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.querydsl.QSort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.jojoldu.blogcode.querydsl.domain.book.QBook.book;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 06/09/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BookPaginationRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookPaginationRepository bookPaginationRepository;

    @Autowired
    private BookPaginationRepositorySupport bookPaginationRepositorySupport;

    private String prefixName = "a";

    @BeforeEach
    void setUp() {
        for (int i = 1; i <= 30; i++) {
            bookRepository.save(Book.builder()
                    .name(prefixName +i)
                    .bookNo(i)
                    .build());
        }
    }

    @AfterEach
    void after() {
        bookRepository.deleteAllInBatch();
    }

    @Test
    void 기존_페이징_방식() throws Exception {
        //when
        /**
         * pageNo는 0부터 시작
         */
        List<BookPaginationDto> books = bookPaginationRepository.paginationLegacy(prefixName, 1, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a20");
        assertThat(books.get(9).getName()).isEqualTo("a11");
    }

    @Test
    void nooffsetBuilder() throws Exception {

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationNoOffsetBuilder(null, prefixName, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a30");
        assertThat(books.get(9).getName()).isEqualTo("a21");
    }

    @Test
    void nooffset_첫페이지() throws Exception {

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationNoOffset(null, prefixName, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a30");
        assertThat(books.get(9).getName()).isEqualTo("a21");
    }

    @Test
    void nooffset_두번째페이지() throws Exception {

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationNoOffset(21L, prefixName, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a20");
        assertThat(books.get(9).getName()).isEqualTo("a11");
    }

    @Test
    void 커버링인덱스() throws Exception {

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationCoveringIndex(prefixName, 1, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a20");
        assertThat(books.get(9).getName()).isEqualTo("a11");
    }

    @Test
    void 커버링인덱스_jdbcTemplate() throws Exception {

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationCoveringIndexSql(prefixName, 1, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a20");
        assertThat(books.get(9).getName()).isEqualTo("a11");
    }

    @Test
    void count_쿼리() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Page<BookPaginationDto> page = bookPaginationRepositorySupport.paginationCount(pageRequest, prefixName);
        List<BookPaginationDto> books = page.getContent();

        //then
        assertThat(page.getTotalPages()).isEqualTo(30);
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a20");
        assertThat(books.get(9).getName()).isEqualTo("a11");
    }

    @Test
    void 검색버튼사용시_10개_페이지_건수가_리턴된다() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 10);
        boolean useSearchBtn = true;
        Page<BookPaginationDto> page = bookPaginationRepositorySupport.paginationCountSearchBtn(useSearchBtn, pageRequest, prefixName);

        //then
        assertThat(page.getTotalElements()).isEqualTo(100); // 10 (pageCount) * 10 (pageSize)
    }

    @Test
    void 페이지버튼사용시_실제_페이지_건수가_리턴된다() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 10);
        boolean useSearchBtn = false;
        Page<BookPaginationDto> page = bookPaginationRepositorySupport.paginationCountSearchBtn(useSearchBtn, pageRequest, prefixName);

        //then
        assertThat(page.getTotalElements()).isEqualTo(30);
    }

    @Test
    void cache된_pageCount를_사용한다() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Long cachedCount = 100L;
        Page<BookPaginationDto> page = bookPaginationRepositorySupport.paginationCountCache(cachedCount, pageRequest, prefixName);

        //then
        assertThat(page.getTotalElements()).isEqualTo(cachedCount);
    }

    @Test
    void cache가_없으면_실제값을_사용한다() throws Exception {
        PageRequest pageRequest = PageRequest.of(1, 10);
        Long cachedCount = null;
        Page<BookPaginationDto> page = bookPaginationRepositorySupport.paginationCountCache(cachedCount, pageRequest, prefixName);

        //then
        assertThat(page.getTotalElements()).isEqualTo(30);
    }
}
