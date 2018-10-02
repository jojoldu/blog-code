package com.jojoldu.blogcode.jpaquerydsl.onetomanyouterjoin;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2018. 10. 2.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface ParentRepositoryCustom {

    List<Family> findFamily();
}
