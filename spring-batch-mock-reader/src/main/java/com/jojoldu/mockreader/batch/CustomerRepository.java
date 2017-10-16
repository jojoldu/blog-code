package com.jojoldu.mockreader.batch;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2017. 10. 16.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
