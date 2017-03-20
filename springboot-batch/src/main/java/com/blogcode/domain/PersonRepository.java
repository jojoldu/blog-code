package com.blogcode.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByFirstName(String firstName);
}
