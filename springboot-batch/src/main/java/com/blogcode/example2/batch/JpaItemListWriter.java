package com.blogcode.example2.batch;

import org.springframework.batch.item.database.JpaItemWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 10.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class JpaItemListWriter<T> extends JpaItemWriter<List<T>>{
    private JpaItemWriter<T> jpaItemWriter;

    public JpaItemListWriter(JpaItemWriter<T> jpaItemWriter) {
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(List<? extends List<T>> items) {
        List<T> totalList = new ArrayList<>();

        for(List<T> list : items){
            totalList.addAll(list);
        }

        jpaItemWriter.write(totalList);
    }
}
