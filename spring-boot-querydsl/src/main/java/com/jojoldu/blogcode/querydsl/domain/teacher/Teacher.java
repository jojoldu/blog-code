package com.jojoldu.blogcode.querydsl.domain.teacher;

/**
 * Created by jojoldu@gmail.com on 2019-01-27
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String subject;

    @Column
    private Long academyId; // 연관관계 없음

    @Builder
    public Teacher(String name, String subject, Long academyId) {
        this.name = name;
        this.subject = subject;
        this.academyId = academyId;
    }
}
