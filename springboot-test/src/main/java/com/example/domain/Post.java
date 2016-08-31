package com.example.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by jojoldu@gmail.com on 2016-08-28.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idx;

    @Column
    private String content;

    @Column
    private LocalDateTime updateDate;

    public Post() {
    }

    public Post(String content, LocalDateTime updateDate) {
        this.content = content;
        this.updateDate = updateDate;
    }

    public long getIdx() {
        return idx;
    }

    public void setIdx(long idx) {
        this.idx = idx;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
