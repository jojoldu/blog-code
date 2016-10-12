package com.bloccode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Controller
public class Application {

	private static List<Member> members = new ArrayList<>();

	public static void main(String[] args) {
		members.add(new Member("jojoldu", "jojoldu@gmail.com"));
		members.add(new Member("github", "github@github.com"));
		members.add(new Member("okky", "okky@okky.com"));

		SpringApplication.run(Application.class, args);
	}

	//@RequestMapping(value = "/", method = RequestMethod.GET)가 GetMapping("/") 가 됨
	@GetMapping("/")
	public String index(){
		return "index";
	}

	@GetMapping("/members")
	@ResponseBody
	public List<Member> getMembers() {
		return members;
	}

	@PostMapping("/member")
	@ResponseBody
	public boolean addMember(Member member) {
		members.add(member);
		return true;
	}
}
