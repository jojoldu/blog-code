package com.blogcode.jpa.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2017. 7. 21.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface AcademyRepository extends JpaRepository<Academy, Long>{

    /**
     * 1. join fetch를 통한 조회
     */
    @Query("select a from Academy a join fetch a.subjects")
    List<Academy> findAllJoinFetch();

    /**
     * 2. @EntityGraph
     */

    @EntityGraph(attributePaths = "subjects")
    @Query("select a from Academy a")
    List<Academy> findAllEntityGraph();

    /**
     * 3. join fetch + distinct 를 통한 조회
     */
    @Query("select DISTINCT a from Academy a join fetch a.subjects")
    List<Academy> findAllJoinFetchDistinct();

    /**
     * 4. @EntityGraph + distinct 를 통한 조회
     */

    @EntityGraph(attributePaths = "subjects")
    @Query("select DISTINCT a from Academy a")
    List<Academy> findAllEntityGraphDistinct();

}
