package com.blogcode.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public interface PersonCopyRepository extends JpaRepository<PersonCopy, Long>{

    PersonCopy findByLastName(String lastName);
}
