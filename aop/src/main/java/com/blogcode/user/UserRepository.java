package com.blogcode.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jojoldu@gmail.com on 2016-12-01
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
}
