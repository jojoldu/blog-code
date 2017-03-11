package com.blogcode.di.step2.view;

import com.blogcode.di.Member;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class XmlView implements View{

    @SuppressWarnings("Duplicates")
    public String template(Member member) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("<id>").append(member.getId()).append("</id>");
        sb.append("<name>").append(member.getName()).append("</name>");
        sb.append("<email>").append(member.getEmail()).append("</email>");
        sb.append("</xml>");

        return sb.toString();
    }
}
