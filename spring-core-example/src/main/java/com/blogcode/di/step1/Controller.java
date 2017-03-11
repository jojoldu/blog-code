package com.blogcode.di.step1;

import com.blogcode.di.Member;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class Controller {

    public String getMember(String type) {
        Member member = getMemberFromDb();

        if("html".equals(type)){
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

        } else if("json".equals(type)){
            StringBuilder sb = new StringBuilder();

            sb.append("{ \"id\":\"").append(member.getId()).append("\",");
            sb.append(" \"name\":\"").append(member.getName()).append("\",");
            sb.append(" \"email\":\"").append(member.getEmail()).append("\"}");

            return sb.toString();

        } else if("xml".equals(type)){
            StringBuilder sb = new StringBuilder();
            sb.append("<xml>");
            sb.append("<id>").append(member.getId()).append("</id>");
            sb.append("<name>").append(member.getName()).append("</name>");
            sb.append("<email>").append(member.getEmail()).append("</email>");
            sb.append("</xml>");

            return sb.toString();
        } else {
            throw new NotMatchTypeException(type);
        }
    }

    private Member getMemberFromDb() {
        return new Member(1L, "이동욱", "jojoldu@gmail.com");
    }


    public static class NotMatchTypeException extends RuntimeException{
        public NotMatchTypeException(String message) {
            super(message+ " 타입을 찾을 수 없습니다");
        }
    }

}
