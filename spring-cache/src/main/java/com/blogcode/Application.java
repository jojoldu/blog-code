package com.blogcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@EnableCaching // 어노테이션을 이용한 캐시기능을 사용하겠다는 선언
@Controller
public class Application {

	private static Logger logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	MemberRepository memberRepository;

	@GetMapping("/member/nocache/{name}")
	@ResponseBody
	public Member getNoCacheMember(@PathVariable String name){

		long start = System.currentTimeMillis(); // 수행시간 측정
		Member member = memberRepository.findByNameNoCache(name); // db 조회
		long end = System.currentTimeMillis();

		logger.info(name+ "의 NoCache 수행시간 : "+ Long.toString(end-start));

		return member;
	}

	@GetMapping("/member/cache/{name}")
	@ResponseBody
	public Member getCacheMember(@PathVariable String name){

		long start = System.currentTimeMillis(); // 수행시간 측정
		Member member = memberRepository.findByNameCache(name); // db 조회
		long end = System.currentTimeMillis();

		logger.info(name+ "의 Cache 수행시간 : "+ Long.toString(end-start));

		return member;
	}

	@GetMapping("/member/refresh/{name}")
	@ResponseBody
	public String refresh(@PathVariable String name){
		memberRepository.refresh(name); // 캐시제거
		return "cache clear!";
	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/")
	@ResponseBody
	public String index(){
		return "HelloWorld";
	}
}
