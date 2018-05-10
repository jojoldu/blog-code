package com.jojoldu.blogcode.springbootjpaid;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2018. 5. 10.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface BookRepository extends JpaRepository<Book, Long> {
}
