package com.example;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2016-08-26.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public interface MemberRepository extends JpaRepository<Member, Long>{
}
