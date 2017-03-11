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
        assertThat(controller.getMember("html"), is("<html><body><table><th>id</th><td>1</td><th>name</th><td>이동욱</td><th>email</th><td>jojoldu@gmail.com</td></table></body></html>"));
        assertThat(controller.getMember("json"), is("{ \"id\":\"1\", \"name\":\"이동욱\", \"email\":\"jojoldu@gmail.com\"}"));
        assertThat(controller.getMember("xml"), is("<xml><id>1</id><name>이동욱</name><email>jojoldu@gmail.com</email></xml>"));
    }

    @Test
    public void step2테스트() {

        //given
        com.blogcode.di.step2.Controller controller = new com.blogcode.di.step2.Controller();

        //do
        assertThat(controller.getMember("html"), is("<html><body><table><th>id</th><td>1</td><th>name</th><td>이동욱</td><th>email</th><td>jojoldu@gmail.com</td></table></body></html>"));
        assertThat(controller.getMember("json"), is("{ \"id\":\"1\", \"name\":\"이동욱\", \"email\":\"jojoldu@gmail.com\"}"));
        assertThat(controller.getMember("xml"), is("<xml><id>1</id><name>이동욱</name><email>jojoldu@gmail.com</email></xml>"));
    }
}
