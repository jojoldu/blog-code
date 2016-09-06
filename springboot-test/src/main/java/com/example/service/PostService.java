package com.example.service;

import com.example.domain.post.Essay;
import com.example.domain.post.Job;
import com.example.domain.post.Tech;

import java.util.List;
import java.util.Optional;

/**
 * Created by jojoldu@gmail.com on 2016-09-03.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

public interface PostService {

    List<Job> getJobList();
    List<Tech> getTechList();
    List<Essay> getEssayList();

    Optional<Job> getJob(long idx);
    boolean addJob(Job job);
}
