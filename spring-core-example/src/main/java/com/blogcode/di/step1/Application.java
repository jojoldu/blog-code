package com.blogcode.di.step1;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class Application {

    public static void main(String[] args) {
        Controller controller = new Controller();
        System.out.println(controller.getMember("html"));
        System.out.println(controller.getMember("json"));
        System.out.println(controller.getMember("xml"));
    }
}
