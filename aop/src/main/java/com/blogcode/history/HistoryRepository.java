package com.blogcode.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jojoldu@gmail.com on 2016-12-08.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Repository
public interface HistoryRepository extends JpaRepository<History, Long>{
}
