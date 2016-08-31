package com.example.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
    private Set<Post> favorites;

    public Member() {
        this.favorites = new LinkedHashSet<>();
    }

    public Member(String email, Set<Post> favorites) {
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

    public Set<Post> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Post> favorites) {
        this.favorites = favorites;
    }
}
