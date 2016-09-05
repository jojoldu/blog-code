package com.example.domain.post;

import com.example.domain.Comment;
import com.example.domain.Post;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-08-30.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Entity
@DiscriminatorValue("ESSAY")
public class Essay extends Post {

    public Essay() {
    }

    public Essay(String content, LocalDateTime updateDate, List<Comment> comments) {
        super(content, updateDate, comments);
    }
}
