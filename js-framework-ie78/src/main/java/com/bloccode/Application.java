package com.bloccode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Controller
public class Application {

	private static List<Member> members = new ArrayList<>();

	public static void main(String[] args) {
		members.add(new Member(0, "jojoldu", "jojoldu@gmail.com"));
		members.add(new Member(1, "github", "github@github.com"));
		members.add(new Member(2, "okky", "okky@okky.com"));

		SpringApplication.run(Application.class, args);
	}

	//@RequestMapping(value = "/", method = RequestMethod.GET)가 GetMapping("/") 가 됨
	@GetMapping("/")
	public String index(){
		return "index";
	}

	@GetMapping("/member")
	@ResponseBody
	public List<Member> getMembers() {
		return members;
	}

	@PostMapping("/member")
	@ResponseBody
	public boolean addMember(@RequestBody Member member) {
		member.setIdx(members.size());
		members.add(member);
		return true;
	}

	@DeleteMapping("/member/{idx}")
	@ResponseBody
	public boolean deleteMember(@PathVariable long idx){
		members.remove(idx);
		return true;
	}
}
