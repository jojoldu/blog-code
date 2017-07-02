package com.jojoldu;

import com.github.jojoldu.mapper.EnumMapper;
import com.github.jojoldu.mapper.EnumMapperType;
import com.github.jojoldu.mapper.EnumMapperValue;
import com.jojoldu.case4.FeeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.EnumMap;
import java.util.List;

@SpringBootApplication
@RestController
public class Application {

	@Autowired
	private EnumMapper enumMapper;

	@Bean
	public EnumMapper enumMapper() {
		EnumMapper enumMapper = new EnumMapper();

		enumMapper.put("FeeType", FeeType.class);

		return enumMapper;
	}

	@GetMapping("/categories")
	public List<EnumMapperValue> getCategories(){
		return enumMapper.get("FeeType");
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
