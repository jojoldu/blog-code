package com.jojoldu.blogcode.springbootjpa.querydsl.store;

import com.jojoldu.blogcode.springbootjpa.domain.store.Store;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 03/11/2019
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public interface StoreQuerydslRepositoryCustom {
    List<Store> findAllByQuerydsl();
}
