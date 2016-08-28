package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2016-08-28.
 * Blog : http://jojoldu.tistory.com
 * Github : http://github.com/jojoldu
 */

@SpringBootApplication
@RestController //Controller 역할도 같이 함 (분리하는게 원칙이지만 프로토타이핑이라 그냥 여기에 추가
public class SwaggerApplication {

	@Autowired
	private MemberRepository memberRepository; //Service Layer는 프로토타이핑이라 생략

	public static void main(String[] args) {
		SpringApplication.run(SwaggerApplication.class, args);
	}

	@RequestMapping("/")
	public String home() {
		return "Hello World";
	}

	@RequestMapping("/view/member")
	public String viewMember() {
		return "Member.html View!";
	}

	@RequestMapping(value = "/api/members", method = RequestMethod.GET)
	public List<Member> findMembers(){
		return memberRepository.findAll();
	}

	@RequestMapping(value = "/api/member/{idx}", method = RequestMethod.GET)
	public Member findMember(@PathVariable long idx){
		return memberRepository.findOne(idx);
	}

	@RequestMapping(value = "/api/member", method = RequestMethod.POST)
	public Member saveMember(@RequestBody Member member){
		return memberRepository.save(member);
	}

	@RequestMapping(value = "/api/member", method = RequestMethod.PUT)
	public Member updateMember(@RequestBody Member member){
		return memberRepository.save(member);
	}

	@RequestMapping(value = "/api/member/{idx}", method = RequestMethod.DELETE)
	public String deleteMember(@PathVariable long idx){
		memberRepository.delete(idx);
		return "delete";
	}
}
