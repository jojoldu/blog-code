package com.blogcode.step;

import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 21.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> implements InitializingBean {

    @Override
    protected void doReadPage() {

    }

    @Override
    protected void doJumpToPage(int itemIndex) {

    }



}
