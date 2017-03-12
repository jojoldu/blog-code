package com.blogcode.di.step3.view;

import com.blogcode.di.Member;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class HtmlView implements View {

    @SuppressWarnings("Duplicates")
    public String template(Member member) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><body><table>");
        sb.append("<th>").append("id").append("</th>");
        sb.append("<td>").append(member.getId()).append("</td>");

        sb.append("<th>").append("name").append("</th>");
        sb.append("<td>").append(member.getName()).append("</td>");

        sb.append("<th>").append("email").append("</th>");
        sb.append("<td>").append(member.getEmail()).append("</td>");

        sb.append("</table></body></html>");

        return sb.toString();
    }
}
