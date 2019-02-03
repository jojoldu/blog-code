package com.jojoldu.blogcode.querydsl.domain.pointevent;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2019-02-01
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface PointEventRepository extends JpaRepository<PointEvent, Long>, PointEventRepositoryCustom {
}
