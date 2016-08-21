package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
public class CompareBootApplication {

	@Autowired
	private Service service;

	public static void main(String[] args) {
		SpringApplication.run(CompareBootApplication.class, args);
	}

	@RequestMapping(value = "/")
	public String index(Model model){
		model.addAttribute("dice", service.getDiceNumber());
		return "index";
	}
}
