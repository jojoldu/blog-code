package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SpringBootApplication
@Controller
public class CompareBootApplication {

	@Autowired
	private ReadingListRepository readingListRepository;

	@Autowired
	private Service service;

	public static void main(String[] args) {
		SpringApplication.run(CompareBootApplication.class, args);
	}

	@RequestMapping(value = "/")
	public String index(Reader reader, Model model){
		model.addAttribute("dice", service.getDiceNumber());
		model.addAttribute("username", reader.getUsername());
		return "index";
	}

	@RequestMapping(value="/", method = RequestMethod.POST)
	public String login(Reader reader, Book book){
		return "redirect:/";
	}
}
