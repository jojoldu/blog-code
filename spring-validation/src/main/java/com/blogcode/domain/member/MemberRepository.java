package com.blogcode.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public interface MemberRepository extends JpaRepository<Member, Long>{
}
