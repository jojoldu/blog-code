package com.blogcode.di.step2.view;

import com.blogcode.di.Member;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class JsonView implements View{

    @SuppressWarnings("Duplicates")
    public String template(Member member) {
        StringBuilder sb = new StringBuilder();

        sb.append("{ \"id\":\"").append(member.getId()).append("\",");
        sb.append(" \"name\":\"").append(member.getName()).append("\",");
        sb.append(" \"email\":\"").append(member.getEmail()).append("\"}");

        return sb.toString();
    }
}
