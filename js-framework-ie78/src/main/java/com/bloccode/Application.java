package com.bloccode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	@Autowired
	Environment env;

	//@RequestMapping(value = "/", method = RequestMethod.GET)가 GetMapping("/") 가 됨
	@GetMapping("/")
	public String index(Model model){
		//activeProfiles는 profile이 없을 경우 size가 0이 되므로, 이를 보정하는 삼항연산자
		String profile = env.getActiveProfiles().length > 0? env.getActiveProfiles()[0] : "default";
		model.addAttribute("profile", profile);
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
