package com.blogcode;

import com.blogcode.example1.domain.Person;
import com.blogcode.example1.domain.PersonRepository;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@EnableBatchProcessing
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private PersonRepository personRepository;

	public Application(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@GetMapping("/")
	public List<Person> getAll() {
		return personRepository.findAll();
	}

}
