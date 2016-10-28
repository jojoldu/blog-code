package com.blogcode;

/**
 * Created by jojoldu@gmail.com on 2016-10-26.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
public interface MemberRepository {
    Member findByNameNoCache(String name);
    Member findByNameCache(String name);
    void refresh(String name);
}
