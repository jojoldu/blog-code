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
            return "html : " + member.toString();
        } else if("json".equals(type)){
            return "json : " + member.toString();
        } else if("xml".equals(type)){
            return "xml : " + member.toString();
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
