package com.blogcode.security.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by jojoldu@gmail.com on 2017. 8. 15.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface UserRoleRepository extends JpaRepository<UserRole, Long>{

    @Query("select ur from UserRole ur where ur.role='ROLE_USER'")
    UserRole findDefaultRole();
}
