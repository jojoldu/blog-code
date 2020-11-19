package com.jojoldu.blogcode.querydsl.entityql;


import com.jojoldu.blogcode.querydsl.config.EntityMapper;
import com.jojoldu.blogcode.querydsl.domain.book.Book;
import com.jojoldu.blogcode.querydsl.domain.book.store.BookStore;
import com.jojoldu.blogcode.querydsl.domain.sql.EBook;
import com.jojoldu.blogcode.querydsl.domain.sql.EBookStore;
import com.querydsl.core.types.Path;
import com.querydsl.sql.dml.BeanMapper;
import com.querydsl.sql.types.Null;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EntityMapperTest {

    @Test
    public void notSearchPropertyIfUsingBeanMapper() throws Exception {
        int expectedId = 1;
        Book obj = Book.builder().bookNo(expectedId).build();

        BeanMapper mapper = BeanMapper.DEFAULT;
        Map<Path<?>, Object> result = mapper.createMap(EBook.qBook, obj);

        assertThat(result.containsValue(expectedId)).isFalse();
    }

    @Test
    public void searchPropertyIfUsingBeanMapper() throws Exception {
        int expectedId = 1;
        Book obj = Book.builder().bookNo(expectedId).build();

        EntityMapper mapper = EntityMapper.DEFAULT;
        Map<Path<?>, Object> result = mapper.createMap(EBook.qBook, obj);

        assertThat(result).containsValue(expectedId);
    }

    @Test
    public void nullBindingIfUsingBeanMapper() throws Exception {
        int expectedId = 1;
        Book obj = Book.builder().bookNo(expectedId).build();

        EntityMapper mapper = EntityMapper.WITH_NULL_BINDINGS;
        Map<Path<?>, Object> result = mapper.createMap(EBook.qBook, obj);

        assertThat(result).containsValue(expectedId);
        assertThat(result).containsValue(Null.DEFAULT);
    }

    @Test
    public void embeddableColumn() throws Exception {
        String name = "name";
        String location = "location";
        String zipCode = "zipCode";
        BookStore obj = BookStore.builder()
                .name(name)
                .location(location)
                .zipCode(zipCode)
                .build();

        EntityMapper mapper = EntityMapper.WITH_NULL_BINDINGS;
        Map<Path<?>, Object> result = mapper.createMap(EBookStore.qBookStore, obj);

        assertThat(result).containsValue(name);
        assertThat(result).containsValue(location);
        assertThat(result).containsValue(zipCode);
    }
}
