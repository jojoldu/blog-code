package com.bloccode;



import lombok.*;

/**
 * Created by jojoldu@gmail.com on 2016-10-12.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

// lombok을 사용하였다. 참고 : https://blogs.idincu.com/dev/?p=17
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {

    @Getter @Setter
    private long idx;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String email;
}
