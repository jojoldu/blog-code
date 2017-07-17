package com.blogcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
public class Application {

	private MemberService memberService;

	public Application(MemberService memberService) {
		this.memberService = memberService;
	}

	private List<Member> generate(){
		List<Member> members = new ArrayList<>();

		for(int i=0; i<10;i++){
			members.add(new Member("name_"+i));
		}

		return members;
	}

	@GetMapping("/no-trans")
	public void noTransaction(){
		memberService.saveNoTransactional(generate());
	}

	@GetMapping("/trans")
	public void transaction(){
		memberService.saveTransactional(generate());
	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
