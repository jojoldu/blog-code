package com.jojoldu.blogcode.querydsl.domain.book.pagination;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jojoldu@gmail.com on 29/10/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@ExtendWith(MockitoExtension.class)
public class ExchangePageRequestUnitTest {
    BookPaginationRepositorySupport repository;

    @BeforeEach
    void setUp() {
        repository = new BookPaginationRepositorySupport(null);
    }

    @ParameterizedTest
    @CsvSource({
            "10, 100, 10",
            "10, 101, 10",
            "10, 91, 10",
            "10, 90, 9",
            "10, 79, 8"})
    void repository_exchange_page_request(int pageNo, long totalCount, int expectedPageNo) throws Exception {
        //given
        Pageable pageRequest = PageRequest.of(pageNo, 10);

        //when
        Pageable result = repository.exchangePageRequest(pageRequest, totalCount);

        //then
        assertThat(result.getPageNumber()).isEqualTo(expectedPageNo);
    }

    @ParameterizedTest
    @CsvSource({
            "10, 100, 10",
            "10, 101, 10",
            "10, 91, 10",
            "10, 90, 9",
            "10, 79, 8"})
    void dto_exchange_page_request(int pageNo, long totalCount, int expectedPageNo) throws Exception {
        //given
        Pageable pageRequest = PageRequest.of(pageNo, 10);

        //when
        Pageable result = new FixedPageRequest(pageRequest, totalCount);

        //then
        assertThat(result.getPageNumber()).isEqualTo(expectedPageNo);
    }
}
