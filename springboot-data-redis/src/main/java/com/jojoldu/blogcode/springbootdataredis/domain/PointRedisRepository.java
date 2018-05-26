package com.jojoldu.blogcode.springbootdataredis.domain;

import org.springframework.data.repository.CrudRepository;

public interface PointRedisRepository extends CrudRepository<Point, String> {
}
