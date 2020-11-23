package com.jojoldu.blogcode.querydsl.domain.academy;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2018-12-29
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface AcademyAdminRepository extends JpaRepository<Academy, Long>, AcademyAdminRepositoryCustom {
}
