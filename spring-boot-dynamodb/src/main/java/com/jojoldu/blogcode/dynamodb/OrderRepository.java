package com.jojoldu.blogcode.dynamodb;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jojoldu@gmail.com on 07/03/2020
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@EnableScan
public interface OrderRepository extends CrudRepository<Order, String> {
}
