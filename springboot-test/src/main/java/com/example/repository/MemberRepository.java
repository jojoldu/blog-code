package com.example.repository;

import com.example.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2016-08-30.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public interface MemberRepository extends JpaRepository<Member, Long>{
    Member findByEmail(String email);
}
