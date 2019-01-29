package com.jojoldu.blogcode.querydsl.domain.pointevent;

/**
 * Created by jojoldu@gmail.com on 2019-01-30
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
public class PointEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PointStatus pointStatus;
    private long pointAmount;

    @Builder
    public PointEvent(PointStatus pointStatus, long pointAmount) {
        this.pointStatus = pointStatus;
        this.pointAmount = pointAmount;
    }
}
