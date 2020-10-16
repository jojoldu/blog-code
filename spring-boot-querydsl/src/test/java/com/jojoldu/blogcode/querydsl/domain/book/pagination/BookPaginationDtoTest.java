package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 16/10/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@ExtendWith(MockitoExtension.class)
public class BookPaginationDtoTest {

    @Test
    void id_역순_정렬() throws Exception {
        //given
        List<BookPaginationDto> list = Arrays.asList(
            new BookPaginationDto(1L),
            new BookPaginationDto(2L)
        );
        //when
        List<BookPaginationDto> sorted = list.stream().sorted().collect(Collectors.toList());

        //then
        assertThat(sorted.get(0).getBookId()).isEqualTo(2L);
        assertThat(sorted.get(1).getBookId()).isEqualTo(1L);
    }
}
