package com.blogcode.domain.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jojoldu@gmail.com on 2017. 2. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String job;

    @Column(nullable = false)
    private String email;

    public Member() {}

    public Member(Long id, String name, String job, String email) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.email = email;
    }

    public void update(String name, String job){
        this.name = name;
        this.job = job;
    }

    public Long getId() {
        return id;
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
