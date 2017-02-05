package com.blogcode;

import com.blogcode.old.OldCompanyContract;
import com.blogcode.old.OldCompanyContractRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private OldCompanyContractRepository oldCompanyContractRepository;

	private OldCompanyContract oldCompanyContract;

	@Before
	private void setup() {
		oldCompanyContract = new OldCompanyContract();
	}

	@Test
	public void add() {

	}

}
