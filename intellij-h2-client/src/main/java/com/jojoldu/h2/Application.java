package com.jojoldu.h2;

import com.jojoldu.h2.domain.Item;
import com.jojoldu.h2.domain.ItemRepository;
import com.jojoldu.h2.domain.Shop;
import com.jojoldu.h2.domain.ShopRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Override
	public void run(String... args) throws Exception {
		shopRepository.save(new Shop("jojoldu", "jojoldu.tistory.com"));
		itemRepository.save(new Item("jojoldu's Github", 1000));
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
