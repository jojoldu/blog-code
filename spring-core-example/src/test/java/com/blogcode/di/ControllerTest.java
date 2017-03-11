package com.blogcode.di;

import com.blogcode.di.step1.Controller;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class ControllerTest {

    @Test
    public void step1테스트() {

        //given
        Controller controller = new Controller();

        //do
        assertThat(controller.getMember("html"), is("html : id: 1 name: 이동욱 email: jojoldu@gmail.com"));
        assertThat(controller.getMember("json"), is("json : id: 1 name: 이동욱 email: jojoldu@gmail.com"));
        assertThat(controller.getMember("xml"), is("xml : id: 1 name: 이동욱 email: jojoldu@gmail.com"));
    }
}
