package com.jojoldu.blogcode.querydsl.domain.pointevent;

import com.jojoldu.blogcode.querydsl.domain.academy.Academy;
import com.jojoldu.blogcode.querydsl.dto.AcademyTeacher;
import com.jojoldu.blogcode.querydsl.dto.PointCalculateAmount;
import com.jojoldu.blogcode.querydsl.dto.StudentCount;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2018-12-29
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface PointEventRepositoryCustom {

    List<PointCalculateAmount> calculateAmounts();
}
