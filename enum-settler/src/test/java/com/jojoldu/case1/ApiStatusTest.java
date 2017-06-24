package com.jojoldu.case1;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 24.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class ApiStatusTest {

    private ApiStatus requestApi(){
        return ApiStatus.Y;
    }

    @Test
    public void aAPI에서_받은데이터를_bAPI와_cAPI에_각각_전달한다 () throws Exception {
        //given
        ApiStatus aResult = requestApi();

        //then
        String bResult = aResult.getbValue();
        boolean cResult = aResult.iscValue();

        assertThat(aResult, is(ApiStatus.Y));
        assertThat(bResult, is("1"));
        assertThat(cResult, is(true));


    }


}
