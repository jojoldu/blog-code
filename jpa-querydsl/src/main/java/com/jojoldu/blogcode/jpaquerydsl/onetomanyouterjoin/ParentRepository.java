package com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2018. 10. 2.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface ParentRepository extends JpaRepository<Parent, Long>, ParentRepositoryCustom {
}
