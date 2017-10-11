package com.jojoldu.delete.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface ShopRepository extends JpaRepository<Shop, Long>{

    long deleteAllByIdIn(List<Long> ids);
}
