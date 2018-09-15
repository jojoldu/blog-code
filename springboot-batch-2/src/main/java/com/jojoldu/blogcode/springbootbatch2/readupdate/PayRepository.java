package com.jojoldu.blogcode.springbootbatch2.readupdate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2018. 9. 15.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface PayRepository extends JpaRepository<Pay, Long> {

    @Query("SELECT p FROM Pay p WHERE p.successStatus = true")
    List<Pay> findAllSuccess();
}
