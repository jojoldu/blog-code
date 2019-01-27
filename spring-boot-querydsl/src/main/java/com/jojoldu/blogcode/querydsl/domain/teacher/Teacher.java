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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String subject;

    private Long academyId; // 연관관계 없음

    @Builder
    public Teacher(String name, String subject, Long academyId) {
        this.name = name;
        this.subject = subject;
        this.academyId = academyId;
    }
}
