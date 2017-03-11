package com.blogcode.di.step2;

import com.blogcode.di.Member;
import com.blogcode.di.step2.view.HtmlView;
import com.blogcode.di.step2.view.JsonView;
import com.blogcode.di.step2.view.View;
import com.blogcode.di.step2.view.XmlView;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class ViewFactory {
    private String type;

    private ViewFactory(String type) {
        this.type = type;
    }

    public static View newInstance(String type){
        if("html".equals(type)){
            return new HtmlView();

        } else if("json".equals(type)){
            return new JsonView();

        } else if("xml".equals(type)){
            return new XmlView();
        } else {
            throw new Controller.NotMatchTypeException(type);
        }
    }

}
