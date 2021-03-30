package com.jojoldu.blogcode.springbootjpa.domain.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 27.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s " +
            "FROM Store s " +
//            "JOIN FETCH s.products " +
            "JOIN FETCH s.employees")
    List<Store> findAllByFetchJoin ();
}
