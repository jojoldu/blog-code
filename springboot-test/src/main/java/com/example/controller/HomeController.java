package com.example.controller;

import com.example.domain.post.Job;
import com.example.exception.PostNotFoundException;
import com.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by jojoldu@gmail.com on 2016-09-03.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */
@Controller
public class HomeController {

    @Autowired
    private PostService postService;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello World";
    }

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("jobs", postService.getJobList());
        model.addAttribute("techs", postService.getTechList());
        model.addAttribute("essays", postService.getEssayList());
        return "home";
    }

    @RequestMapping(value = "/job", method = RequestMethod.POST)
    public boolean addJob(@RequestBody Job job) {
        return postService.addJob(job);
    }

    @RequestMapping(value="/job/{idx}")
    public Job getJob(@PathVariable long idx) {
        return Optional.ofNullable(this.postService.getJob(idx))
                .orElseThrow(() -> new PostNotFoundException(idx)); // this.postService.getJob(idx)가 null일 경우 PostNotFoundException 발생
    }

}
