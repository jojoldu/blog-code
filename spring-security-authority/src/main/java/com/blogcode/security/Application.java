package com.blogcode.security;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {

	private static final String PROPERTIES = "spring.config.location=classpath:/google.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class)
				.properties(PROPERTIES)
				.run(args);
	}
}
