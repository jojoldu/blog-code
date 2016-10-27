package com.blogcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by jojoldu@gmail.com on 2016-10-27.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@org.springframework.stereotype.Repository
public class MemberRepositoryImpl implements MemberRepository {

    private static Logger logger = LoggerFactory.getLogger(MemberRepositoryImpl.class);

    @Override
    public Member findByNameNoCache(String name) {
        slowQuery(2000);
        return new Member(0, name+"@gmail.com", name);
    }

    @Override
    @Cacheable(value="findMemberCache", key="#name")
    public Member findByNameCache(String name) {
        slowQuery(2000);
        return new Member(0, name+ "@gmail.com", name);
    }

    @Override
    @CacheEvict(value = "findMemberCache", key="#name")
    public void refresh(String name) {
        logger.info(name + "의 Cache Clear!");
    }

    // 빅쿼리를 돌린다는 가정
    private void slowQuery(long seconds) {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
