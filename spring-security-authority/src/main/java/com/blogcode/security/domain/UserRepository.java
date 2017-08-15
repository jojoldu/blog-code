package com.blogcode.security.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by jojoldu@gmail.com on 2017. 8. 15.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface UserRepository extends JpaRepository<User, Long>{

    @EntityGraph(attributePaths = {"userRoles", "userRoles.userRole"})
    @Query("select distinct u from User u where u.email=:email")
    User findByEmail(@Param("email") String email);
}
