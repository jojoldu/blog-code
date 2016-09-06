package com.example;

import com.example.controller.HomeController;
import com.example.domain.post.Essay;
import com.example.domain.post.Job;
import com.example.domain.post.Tech;
import com.example.service.PostService;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;
/**
 * Created by jojoldu@gmail.com on 2016-09-03.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest(HomeController.class)
public class WebMvcTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void test_샘플() throws Exception {
        this.mvc.perform(get("/hello").accept(MediaType.TEXT_PLAIN)) // /hello 라는 url로 text/plain 타입을 요청
                .andExpect(status().isOk()) // 위 요청에 따라 결과가 status는 200이며
                .andExpect(content().string("Hello World"));  // response body에 "Hello World" 가 있는지 검증
    }

    @Test
    public void test_View검증() throws Exception {
        this.mvc.perform(get("/")) // "/" 로 url 호출
                .andExpect(status().isOk()) // 위 요청에 따라 결과가 status는 200이며
                .andExpect(view().name("home"));  // 호출한 view의 이름이 home인지 확인 (확장자는 생략)
    }

    @MockBean // postService에 가짜 Bean을 등록
    private PostService postService;

    @Test
    public void test_Model검증및ServiceMocking() throws Exception {
        Job[] jobs = {new Job("잡플래닛", LocalDateTime.now(), new ArrayList<>())};
        Tech[] techs = {new Tech("OKKY", LocalDateTime.now(), new ArrayList<>())};

        given(this.postService.getJobList()) // this.postService.getJobList 메소드를 실행하면
                .willReturn(Arrays.asList(jobs)); // Arrays.asList(jobs) 를 리턴해줘라.

        given(this.postService.getTechList())
                .willReturn(Arrays.asList(techs));

        given(this.postService.getEssayList())
                .willReturn(new ArrayList<>());

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("jobs")) // model에 "jobs" 라는 key가 존재하는지 확인
                .andExpect(model().attribute("jobs", IsCollectionWithSize.hasSize(1))) // jobs model의 size가 1인지 확인
                .andExpect(model().attribute("techs", contains(techs[0]))); // techs model이 "OKKY" 라는 객체를 가지고 있는지 확인

    }
}
