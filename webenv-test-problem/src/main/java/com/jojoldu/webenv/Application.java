package com.jojoldu.webenv;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {

	public static final String CONFIG_LOCATIONS = "spring.config.location=classpath:/api/external.yml";

	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class)
				.properties(CONFIG_LOCATIONS)
				.run(args);
	}
}
