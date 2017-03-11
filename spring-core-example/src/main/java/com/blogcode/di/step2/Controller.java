package com.blogcode.di.step2;

import com.blogcode.di.Member;
import com.blogcode.di.step2.view.View;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class Controller {

    public String getMember(String type) {
        Member member = getMemberFromDb();
        View view = ViewFactory.newInstance(type);
        return view.template(member);
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
