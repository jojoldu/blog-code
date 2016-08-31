package com.example.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-08-30.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idx;

    @Column
    private String email;

    @OneToMany()
    @JoinTable(name="MEMBER_POST",
            joinColumns=@JoinColumn(name="MEMBER_IDX"),
            inverseJoinColumns=@JoinColumn(name="POST_IDX"))
    @OrderBy("idx DESC")
    private List<Post> favorites;

    public Member() {
        this.favorites = new ArrayList<>();
    }

    public Member(String email, List<Post> favorites) {
        this.email = email;
        this.favorites = favorites;
    }

    public void addPost(Post post){
        this.favorites.add(post);
    }

    public long getIdx() {
        return idx;
    }

    public void setIdx(long idx) {
        this.idx = idx;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Post> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Post> favorites) {
        this.favorites = favorites;
    }
}
