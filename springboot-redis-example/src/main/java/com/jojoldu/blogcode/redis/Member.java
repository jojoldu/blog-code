package com.jojoldu.blogcode.redis;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

/**
 * Created by jojoldu@gmail.com on 2018. 5. 12.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@RedisHash("member")
public class Member {

    @Id
    private String id;

    private String name;
    private LocalDateTime createDateTime;
    private Long visitCount;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.createDateTime = LocalDateTime.now();
        this.visitCount = 1L;
    }

    public void visit(){
        visitCount++;
    }

}
