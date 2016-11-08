package com.blogcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@Controller
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    private Random random = new Random();

	@GetMapping("/")
	public String index(Model model) {
        // 기존의 날짜형식의 파라미터
        //String param = Long.toString(new Date().getTime());

        int param = random.nextInt(1000);
        model.addAttribute("param", param);
		return "index";
	}
}
