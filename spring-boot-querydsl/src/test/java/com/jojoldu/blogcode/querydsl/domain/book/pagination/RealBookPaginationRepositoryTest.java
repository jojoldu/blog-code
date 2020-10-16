package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 06/09/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "real")
public class RealBookPaginationRepositoryTest {

    @Autowired
    private BookPaginationRepository bookPaginationRepository;

    @Test
    void 기존_페이징_방식() throws Exception {
        //given
        String name = "200";

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationLegacy(name, 10_000, 10);

        //then
        assertThat(books).hasSize(10);
        System.out.println(books.get(0).getBookId());
    }

    @Test
    void nooffset() throws Exception {
        //given
        String name = "200";

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationNoOffset(53291002L, name, 10);

        //then
        assertThat(books).hasSize(10);
    }

    @Test
    void 커버링인덱스() throws Exception {
        //given
        String name = "200";

        //when
        List<BookPaginationDto> books = bookPaginationRepository.paginationCoveringIndex(name, 1, 10);

        //then
        assertThat(books).hasSize(10);
        assertThat(books.get(0).getName()).isEqualTo("a20");
        assertThat(books.get(9).getName()).isEqualTo("a11");
    }
}
