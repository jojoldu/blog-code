package com.blogcode.di.step3;

import com.blogcode.di.step2.view.HtmlView;
import com.blogcode.di.step2.view.JsonView;
import com.blogcode.di.step2.view.XmlView;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class Application {

    public static void main(String[] args) {
        Controller htmlController = new Controller(new HtmlView());
        Controller jsonController = new Controller(new JsonView());
        Controller xmlController = new Controller(new XmlView());
        System.out.println(htmlController.getMember());
        System.out.println(jsonController.getMember());
        System.out.println(xmlController.getMember());
    }
}
