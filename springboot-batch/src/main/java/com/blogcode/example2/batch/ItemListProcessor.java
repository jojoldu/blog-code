package com.blogcode.example2.batch;

import com.blogcode.example2.domain.Sales;
import com.blogcode.example2.domain.Tax;
import org.springframework.batch.item.ItemProcessor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 4. 10.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class ItemListProcessor implements ItemProcessor<Sales, List<Tax>>{

    @Override
    public List<Tax> process(Sales item) throws Exception {

        return Arrays.asList(
                new Tax(item.getTxAmount(), item.getOwnerNo()),
                new Tax((long)(item.getTxAmount()/1.1), item.getOwnerNo()),
                new Tax(item.getTxAmount()/11, item.getOwnerNo())
        );
    }
}
