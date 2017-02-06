package com.blogcode;

import com.blogcode.before.Contract;
import com.blogcode.before.ContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private ContractRepository stringRepository;

	private Contract contract;

	@Before
	public void setup() {
		contract = new Contract(
				"우아한짐카",
				1.0,
				"percent",
				"round"
		);
	}

	@Test
	public void add() {
		stringRepository.save(contract);
		Contract saved = stringRepository.findAll().get(0);
		assertThat(saved.getCommission(), is(1.0));
	}

	@Test
	public void findByCommissionType() {

	}
}
