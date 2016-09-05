package com.example.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-08-28.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계(단일테이블) 선언
@DiscriminatorColumn(name="DTYPE") // 하위 엔티티들을 구분하는 컬럼명 (default가 DTYPE라서 생략가능, 정보전달을 위해 명시함)
public abstract class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idx;

    @Column
    private String content;

    @Column
    private LocalDateTime updateDate;

    @OneToMany(mappedBy="post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Post() {
        this.comments = new ArrayList<>();
    }

    public Post(String content, LocalDateTime updateDate, List<Comment> comments) {
        this.content = content;
        this.updateDate = updateDate;
        this.comments = comments;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
        if (comment.getPost() != this) {
            comment.setPost(this);
        }
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
