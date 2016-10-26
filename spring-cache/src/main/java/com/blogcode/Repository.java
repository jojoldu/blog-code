package com.blogcode;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2016-10-26.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Member, Long>{
}
