package com.jojoldu.h2.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 12.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface ItemRepository extends JpaRepository<Item, Long> {
}
