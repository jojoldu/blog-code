package com.jojoldu.blogcode.redis;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by jojoldu@gmail.com on 2018. 5. 12.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface MemberRedisRepository extends CrudRepository<Member, String> {
}
