package com.jojoldu.case1;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 24.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class TableStatusTest {

    private TableStatus selectFromOriginTable(){
        return TableStatus.Y;
    }

    @Test
    public void origin테이블에서_조회한_데이터를_다른_2테이블에_등록한다() throws Exception {
        //given
        TableStatus origin = selectFromOriginTable();

        //then
        String table1Value = origin.getTable1Value();
        boolean table2Value = origin.isTable2Value();

        assertThat(origin, is(TableStatus.Y));
        assertThat(table1Value, is("1"));
        assertThat(table2Value, is(true));
    }


}
