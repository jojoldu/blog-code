package com.bloccode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SpringBootApplication
@Controller
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	/*
		@RequestMapping(value = "/", method = RequestMethod.GET)가 GetMapping("/") 가 됨
	 */

	@GetMapping("/")
	public String index(){
		return "index";
	}
}
