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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "point_event")
public class PointEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "point_status")
    @Enumerated(EnumType.STRING)
    private PointStatus pointStatus;

    @Column(name = "point_amount")
    private Long pointAmount;

    @Builder
    public PointEvent(PointStatus pointStatus, long pointAmount) {
        this.pointStatus = pointStatus;
        this.pointAmount = pointAmount;
    }
}
