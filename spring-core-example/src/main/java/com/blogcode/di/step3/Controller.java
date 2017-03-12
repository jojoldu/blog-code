package com.blogcode.di.step3;

import com.blogcode.di.Member;
import com.blogcode.di.step2.ViewFactory;
import com.blogcode.di.step2.view.View;

/**
 * Created by jojoldu@gmail.com on 2017. 3. 11.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public class Controller {

    private View view;

    public Controller(View view) {
        this.view = view;
    }

    public String getMember() {
        Member member = getMemberFromDb();
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
