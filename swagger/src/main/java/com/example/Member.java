package com.example;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;

/**
 * Created by jojoldu@gmail.com on 2016-08-28.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idx;

    @Column
    private String id;

    @Column
    @Email
    private String email;

    public long getIdx() {
        return idx;
    }

    public void setIdx(long idx) {
        this.idx = idx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
