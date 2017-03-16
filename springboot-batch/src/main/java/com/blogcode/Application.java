package com.blogcode;

import com.blogcode.domain.Person;
import com.blogcode.domain.PersonRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
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
