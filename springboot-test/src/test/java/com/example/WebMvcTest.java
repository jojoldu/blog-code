package com.example;

import com.example.controller.HomeController;
import com.example.domain.post.Job;
import com.example.domain.post.Tech;
import com.example.exception.PostNotFoundException;
import com.example.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Created by jojoldu@gmail.com on 2016-09-03.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@RunWith(SpringRunner.class)
@org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest(HomeController.class)
public class WebMvcTest {

    @Autowired
    private MockMvc mvc; // Test를 위한 SpringMVC 엔진을 탑제한 가짜(Mock) MVC객체

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
                .andExpect(model().attribute("techs", contains(techs[0]))) // techs model이 "OKKY" 라는 객체를 가지고 있는지 확인
                .andExpect(model().attribute("essays", is(empty()))); // 빈 Collection인지 확인
    }

    @Test
    public void test_Job데이터입력하기() throws Exception {
        mvc.perform(post("/job")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("content", "많이 와주세요! http://jojoldu.tistory.com"))
                .andExpect(status().is3xxRedirection()) // 302 redirection이 발생했는지 확인
                .andExpect(header().string("Location", "/")) // location이 "/" 인지 확인
                .andDo(MockMvcResultHandlers.print()); // test 응답 결과에 대한 모든 내용 출력
    }

    @Test
    public void test_Json결과비교하기() throws Exception {
        Job job = new Job("많이 와주세요! http://jojoldu.tistory.com", LocalDateTime.now(), new ArrayList<>());

        given(this.postService.getJob(1))
                .willReturn(job);

        mvc.perform(get("/job/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("많이 와주세요! http://jojoldu.tistory.com"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void test_Exception체크() throws Exception {
        given(this.postService.getJob(1)) // getJob 메소드에 인자값 1이 입력될 경우
                .willReturn(null); // exception 발생을 위해 null 리턴

        mvc.perform(get("/job/1")) // /job/1 을 호출할 경우
                .andExpect(status().isNotFound()); // not found exception이 나오는지 아닌지 테스트
    }

    @Test
    public void test_orElseThrow() throws Exception {
        Optional.ofNullable(null).orElseThrow(()-> new PostNotFoundException(1));
    }

}
