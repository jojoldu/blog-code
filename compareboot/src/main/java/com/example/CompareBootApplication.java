package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class CompareBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompareBootApplication.class, args);
	}

	@RequestMapping(value = "/")
	public String index(){
		return "index";
	}
}
